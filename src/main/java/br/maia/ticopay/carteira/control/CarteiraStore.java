package br.maia.ticopay.carteira.control;

import br.maia.ticopay.carteira.entity.Carteira;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Map;
import java.util.UUID;

@Dependent
public class CarteiraStore {

    @Inject
    @Named("carteiras")
    Map<UUID, Carteira> carteiras;

    public Double getSaldo(UUID userId) {
        if(!carteiras.containsKey(userId)) {
            carteiras.put(userId, new Carteira(0.0));
        }
        return carteiras.get(userId).saldo();
    }

    public void updateUserWallet(UUID payer, double novoSaldo) {
        carteiras.put(payer, new Carteira(novoSaldo));
    }

    public void depositar(UUID payer, Double value) {
        var novoSaldo = getSaldo(payer) + value;
        updateUserWallet(payer, novoSaldo);
    }

    public void sacar(UUID payee, Double value) {
        if(getSaldo(payee) < value) {
            throw new SaldoInsuficienteException();
        }
        var novoSaldo = getSaldo(payee) - value;
        updateUserWallet(payee, novoSaldo);
    }
}
