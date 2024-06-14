package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Dependent
public class WalletStore {

    @Transactional
    public void depositar(Long payer, BigDecimal value) {
        Wallet wallet = getWallet(payer);
        wallet.setBalance(wallet.getBalance().add(value));
    }

    private Wallet getWallet(Long payer) {
        return
            Optional.ofNullable(
                this.findById(payer)
            ).orElseThrow(
                () -> new WalletNotFoundException(
                    "Wallet with id %d not found".formatted(payer)
                )
            );
    }

    @Transactional
    public void sacar(Long payee, BigDecimal value) {
        Wallet wallet = getWallet(payee);
        if (value.compareTo(wallet.getBalance()) > 0) {
            throw new InsufficientFundsException();
        }
        wallet.setBalance(wallet.getBalance().subtract(value));
    }

    @Inject
    EntityManager entityManager;

    @Transactional
    Wallet findById(long l) {
        return entityManager.find(Wallet.class, l);
    }

    public BigDecimal getBalance(Long user1Id) {
        return findById(user1Id).getBalance();
    }

}
