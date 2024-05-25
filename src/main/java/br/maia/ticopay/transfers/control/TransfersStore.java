package br.maia.ticopay.transfers.control;

import br.maia.ticopay.transfers.entity.Transfer;
import br.maia.ticopay.wallet.control.CarteiraStore;
import br.maia.ticopay.transfers.boundary.TransfersResource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Dependent
@Transactional(Transactional.TxType.MANDATORY)
public class TransfersStore {

    @Inject
    @Named("transactions")
    Map<Long, Transfer> transfers;

    @Inject
    CarteiraStore walletStore;

    @Inject
    Event<TransferEvent> onTransfer;

    @Inject
    Event<DepositEvent> onDeposit;

    @Inject
    Event<WithdrawalEvent> onWithdrawal;

    public Transfer get(Long transactionId) {
        return transfers.get(transactionId);
    }

    public record TransferEvent(Long payer, Long payee, BigDecimal value) {}
    record WithdrawalEvent(Long payee, BigDecimal value) {}
    record DepositEvent(Long payer, BigDecimal value) {}

    public Transfer postTransfer(long payer, long payee, BigDecimal value) {
        walletStore.depositar(payee, value);
        walletStore.sacar(payer, value);
        Long transactionId = (long) (Math.random() * 100_000_000);
        Transfer transfer = new Transfer(transactionId, payer, payee, value);
        transfers.put(transactionId, transfer);
        onTransfer.fire(new TransferEvent(payer, payee, value));
        return transfer;
    }

    public void depositar(Long wallet, BigDecimal value) {
        walletStore.depositar(wallet, value);
        onDeposit.fire(new DepositEvent(wallet, value));
    }

    public void sacar(Long wallet, BigDecimal value) {
        walletStore.sacar(wallet, value);
        onWithdrawal.fire(new WithdrawalEvent(wallet, value));
    }

}
