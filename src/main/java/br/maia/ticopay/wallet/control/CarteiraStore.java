package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Dependent
public class CarteiraStore {

    @Inject
    WalletRepository repository;

    @Transactional
    public void depositar(Long payer, BigDecimal value) {
        Wallet wallet = repository.findById(payer);
        wallet.setBalance(wallet.getBalance().add(value));
    }

    @Transactional
    public void sacar(Long payee, BigDecimal value) {
        Wallet wallet = repository.findById(payee);
        if(value.compareTo(wallet.getBalance()) > 0){
            throw new InsufficientFundsException();
        }
        wallet.setBalance(wallet.getBalance().subtract(value));
    }

    @Transactional
    public Wallet findById(long l) {
        return repository.findById(l);
    }
}
