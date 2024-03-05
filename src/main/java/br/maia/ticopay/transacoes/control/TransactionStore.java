package br.maia.ticopay.transacoes.control;

import br.maia.ticopay.carteira.control.CarteiraStore;
import br.maia.ticopay.transacoes.boundary.TransactionsResource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.util.Map;
import java.util.UUID;

@Dependent
@Transactional(Transactional.TxType.MANDATORY)
public class TransactionStore {

    @Inject
    @Named("transactions")
    Map<UUID, TransactionsResource.Transaction> transactions;

    @Inject
    CarteiraStore carteiraStore;

    @Inject
    Event<TransferEvent> onTransfer;

    @Inject
    Event<DepositEvent> onDeposit;

    @Inject
    Event<WithdrawalEvent> onWithdrawal;

    public record TransferEvent(UUID payer, UUID payee, Double value) {}
    record WithdrawalEvent(UUID payee, Double value) {}
    record DepositEvent(UUID payer, Double value) {}

    public void postTransaction(UUID transactionId, UUID payer, UUID payee, Double value) {
        carteiraStore.depositar(payer, value);
        carteiraStore.sacar(payee, value);
        TransactionsResource.Transaction transfer = new TransactionsResource.Transaction(transactionId, payer, payee, value);
        transactions.put(transactionId, transfer);
        onTransfer.fire(new TransferEvent(payer, payee, value));
    }

    public void depositar(UUID wallet, Double value) {
        carteiraStore.depositar(wallet, value);
        TransactionsResource.Transaction transaction = new TransactionsResource.Transaction(UUID.randomUUID(), null, wallet, value);
        onDeposit.fire(new DepositEvent(wallet, value));
    }

    public void sacar(UUID wallet, Double value) {
        carteiraStore.sacar(wallet, value);
        TransactionsResource.Transaction transaction = new TransactionsResource.Transaction(UUID.randomUUID(), wallet, null, value);
        transactions.put(transaction.id(), transaction);
        onWithdrawal.fire(new WithdrawalEvent(wallet, value));
    }
    TransactionsResource.Transaction get(UUID transactionId) {
        return transactions.get(transactionId);
    }
}
