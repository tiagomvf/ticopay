package br.maia.ticopay.notifications.control;

import br.maia.ticopay.notifications.entity.Notification;
import br.maia.ticopay.transfers.entity.Transfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class NotificationSender {

    @Inject
    Logger logger;

    @Inject
    EntityManager entityManager;

    @Incoming("transfer-created")
    @Transactional
    public CompletionStage<Void> process(Message<Transfer> transferMessage) {
        Transfer transfer = transferMessage.getPayload();
        try {
            this.sendNotification(transfer);
            logger.info("Notification sent");
        } catch (Exception ex) {
            Notification notification = new Notification();
            notification.setPayee(transfer.getPayee());
            notification.setPayer(transfer.getPayer());
            notification.setValue(transfer.getValue());
            notification.setStatus(Notification.Status.ERROR);
            //saving not send notifications...
            entityManager.persist(notification);
            logger.warn("Error sending notification: " + ex.getClass() + ex.getMessage());
        }
        return transferMessage.ack();
    }

    @Retry(maxRetries = 2, delay = 100)
    void sendNotification(Transfer transfer) throws IOException, InterruptedException {
        logger.info("Trying to send notification");
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("https://util.devi.tools/api/v1/notify"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 204) {
                logger.warn("Could not send notification: " + response.body());
                throw new RuntimeException("Could not send notification: " + response.body());
            }
        }
    }

}
