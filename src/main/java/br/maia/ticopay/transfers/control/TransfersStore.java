package br.maia.ticopay.transfers.control;

import br.maia.ticopay.transfers.entity.Transfer;
import br.maia.ticopay.wallet.control.CarteiraStore;
import br.maia.ticopay.transfers.boundary.TransfersResource;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Predicate;

@Dependent
@Transactional(Transactional.TxType.MANDATORY)
public class TransfersStore {

    @Inject
    TransferRepository repository;

    @Inject
    CarteiraStore walletStore;

    @Inject
    Event<TransferEvent> onTransfer;

    public Transfer get(Long transactionId) {
        return repository.findById(transactionId);
    }

    public record TransferEvent(Long payer, Long payee, BigDecimal value) {}

    public Transfer postTransfer(long payer, long payee, BigDecimal value) {
        walletStore.depositar(payee, value);
        walletStore.sacar(payer, value);
        Transfer transfer = new Transfer(payer, payee, value);
        validar(transfer);
        repository.persist(transfer);
        onTransfer.fire(new TransferEvent(payer, payee, value));
        return transfer;
    }

    @Retry(abortOn = {WebApplicationException.class})
    void validar(Transfer transfer) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://util.devi.tools/api/v2/authorize"))
                .build();
            try {
                HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                JsonObject jsonObject = Json.createReader(response.body()).readObject();
                switch (response.statusCode()) {
                    case 200: {
                        boolean valid = processValidation(jsonObject);
                        if(!valid) {
                            throw new WebApplicationException("Transfer not authorized", 401);
                        }
                        break;
                    }
                    case 401:
                    case 403: {
                        throw new WebApplicationException("Transfer not authorized", response.statusCode());
                    }
                    default: throw new ServerErrorException(
                        "Transfer could not be authorized (http response: %d)".formatted(response.statusCode()), 500
                    );
                }
            } catch (IOException e) {
                throw new TransferAuthenticationException(
                    "An IOException occurred when validating the transfer.", e
                );
            } catch (InterruptedException e) {
                throw new TransferAuthenticationException(
                    "An InterruptedException occurred when validating the transfer.", e
                );
            }
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
