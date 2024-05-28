package br.maia.ticopay.transfers.control;

import br.maia.ticopay.transfers.entity.Transfer;
import br.maia.ticopay.wallet.control.CarteiraStore;
import br.maia.ticopay.transfers.boundary.TransfersResource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

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

}
