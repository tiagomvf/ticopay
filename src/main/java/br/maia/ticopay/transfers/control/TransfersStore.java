package br.maia.ticopay.transfers.control;

import br.maia.ticopay.actors.control.UserStore;
import br.maia.ticopay.actors.entity.Actor;
import br.maia.ticopay.actors.entity.Merchant;
import br.maia.ticopay.transfers.entity.Transfer;
import br.maia.ticopay.wallet.control.WalletNotFoundException;
import br.maia.ticopay.wallet.control.WalletStore;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Dependent
public class TransfersStore {

    @Inject
    TransferRepository repository;

    @Inject
    WalletStore walletStore;

    public Transfer get(Long transactionId) {
        return repository.findById(transactionId);
    }

    @Inject
    @Channel("transfer-created")
    Emitter<Transfer> emitter;

    @Inject
    UserStore userStore;

    @Transactional()
    public Transfer postTransfer(long payer, long payee, BigDecimal value) {
        assertExistenceOf(payer, payee);
        assertPayerIsNotMerchant(payer);
        Transfer transfer = new Transfer(payer, payee, value);
        validar(transfer);
        repository.persist(transfer);
        walletStore.depositar(payee, value);
        walletStore.sacar(payer, value);
        emitter.send(transfer);
        return transfer;
    }

    private void assertPayerIsNotMerchant(long payer) {
        Actor actor = userStore.findById(payer);
        if(actor instanceof Merchant) {
            // todo: create correct Exception
            throw new MerchantCannotSendMoneyException("Merchant cannot send money.");
        }
    }

    private void assertExistenceOf(long payer, long payee) {
        boolean notFound =
            Stream.of(payer, payee)
                .map(userStore::findById)
                .anyMatch(Objects::isNull);
        if(notFound) {
            throw new WalletNotFoundException("Wallet with not found.");
        }
    }

    Predicate<Transfer> authorizationHandler = (t) -> {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://util.devi.tools/api/v2/authorize"))
                .build();
            try {
                HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                JsonObject jsonObject = Json.createReader(response.body()).readObject();
                return switch (response.statusCode()) {
                    case 200 -> processValidation(jsonObject);
                    case 401, 403 -> false;
                    default -> throw new ServerErrorException(
                        "Transfer could not be authorized (http response: %d)".formatted(response.statusCode()), 500
                    );
                };
            } catch (IOException e) {
                throw new RuntimeException(
                    "An IOException occurred when validating the transfer.", e
                );
            } catch (InterruptedException e) {
                throw new RuntimeException(
                    "An InterruptedException occurred when validating the transfer.", e
                );
            }
        }

    };

    @Retry(abortOn = {WebApplicationException.class})
    void validar(Transfer transfer) {
        if(!authorizationHandler.test(transfer)) {
            throw new TransferNotAuthorizedException();
        }
    }

    private static boolean processValidation(JsonObject jsonObject) {
        Predicate<JsonObject> success =
            (JsonObject j) -> "success".equals(j.getString("status"));
        Predicate<JsonObject> authorized =
            (JsonObject j) -> j.getJsonObject("data").getBoolean( "authorization");
        return success.and(authorized).test(jsonObject);
    }
}
