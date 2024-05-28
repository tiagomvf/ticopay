package br.maia.ticopay.transfers.control;

import br.maia.ticopay.transfers.entity.Transfer;
import br.maia.ticopay.wallet.control.CarteiraStore;
import br.maia.ticopay.transfers.boundary.TransfersResource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
        repository.persist(transfer);
        onTransfer.fire(new TransferEvent(payer, payee, value));
        return transfer;
    }

    void validar(Transfer transfer) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().build();
            try {
                HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                JsonObject jsonObject = Json.createReader(response.body()).readObject();
                System.out.println(response.statusCode());
                System.out.println(jsonObject.toString());

            } catch (IOException e) {
                throw new TransferAuthenticationException(
                    "An IOExcetion occurred when validating the transfer.", e
                );
            } catch (InterruptedException e) {
                throw new TransferAuthenticationException(
                    "An InterruptedException occurred when validating the transfer.", e
                );
            }
        }
    }
}
