package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.math.BigDecimal;
import java.util.Optional;

@Dependent
public class CarteiraStore {

    @Inject
    WalletRepository repository;

    @Transactional
    public void depositar(Long payer, BigDecimal value) {
        Wallet wallet = getWallet(payer);
        wallet.setBalance(wallet.getBalance().add(value));
    }

    private Wallet getWallet(Long payer) {
        Wallet wallet = Optional.ofNullable(repository.findById(payer)).orElseThrow(
            () -> new WalletNotFoundException(
                "Wallet with id %d not found".formatted(payer)
            )
        );
        return wallet;
    }

    @Transactional
    public void sacar(Long payee, BigDecimal value) {
        Wallet wallet = getWallet(payee);
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
