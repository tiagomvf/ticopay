package br.maia.ticopay.wallet.control;

import br.maia.ticopay.carteira.entity.Carteira;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.util.Map;

@Dependent
public class CarteiraStore {

    @Inject
    @Named("carteiras")
    Map<Long, Carteira> carteiras;

    public BigDecimal getSaldo(Long userId) {
        if(!carteiras.containsKey(userId)) {
            carteiras.put(userId, new Carteira(BigDecimal.ZERO));
        }
        return carteiras.get(Long.valueOf(userId)).saldo();
    }

    public void updateUserWallet(Long payer, BigDecimal novoSaldo) {
        carteiras.put(payer, new Carteira(novoSaldo));
    }

    public void depositar(Long payer, BigDecimal value) {
        var novoSaldo = getSaldo(payer).add(value);
        updateUserWallet(payer, novoSaldo);
    }

    public void sacar(Long payee, BigDecimal value) {
        BigDecimal oldBalace = getSaldo(payee);
        if(oldBalace.compareTo(value) < 1) {
            throw new InsufficientFundsException();
        }
        var newBalace = oldBalace.subtract(value);
        updateUserWallet(payee, newBalace);
    }
}
