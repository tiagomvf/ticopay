package br.maia.ticopay.notificacoes.control;

import br.maia.ticopay.transacoes.control.TransactionStore;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;

public class TransactionObserver {

    public void notifyPayee(@Observes(during = TransactionPhase.AFTER_SUCCESS) TransactionStore.TransferEvent transfer) {
        String mensagem = "notificacao para %s: recebeu %.2f de %s".formatted(transfer.payee(), transfer.value(),
            transfer.payer());
        System.out.println(mensagem);
    }

    public void notifyPayer(@Observes(during = TransactionPhase.AFTER_SUCCESS) TransactionStore.TransferEvent transfer) {
        String mensagem = "notificacao para %s: pagou %.2f para %s".formatted(transfer.payer(), transfer.value(),
            transfer.payee());
        System.out.println(mensagem);
    }
}
