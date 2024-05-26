package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class CarteiraStore {

    @Inject
    WalletRepository repository;

    Wallet getWalletOf(Long ownerId) {
        Wallet wallet = repository.findById(ownerId);
        if(wallet != null) return wallet;
        wallet = new Wallet();
        wallet.setOwnerId(ownerId);
        wallet.setBalance(BigDecimal.ZERO);
        repository.persist(wallet);
        return wallet;
    }

    @Transactional
    public void depositar(Long payer, BigDecimal value) {
        Wallet wallet = getWalletOf(payer);
        wallet.setBalance(wallet.getBalance().add(value));
    }

    @Transactional
    public void sacar(Long payee, BigDecimal value) {
        Wallet wallet = getWalletOf(payee);
        if(value.compareTo(wallet.getBalance()) > 0){
            throw new InsufficientFundsException();
        }
        wallet.setBalance(wallet.getBalance().subtract(value));
    }
}
