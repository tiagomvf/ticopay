package br.maia.ticopay;

import br.maia.ticopay.carteira.entity.Carteira;
import br.maia.ticopay.transacoes.boundary.TransactionsResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class GlobalStoreProvider {

    Map<UUID, TransactionsResource.Transaction> transactions = new HashMap<>();
    Map<UUID, Carteira> carteiras = new HashMap<>();

    @Produces()
    @Named("transactions")
    public Map<UUID, TransactionsResource.Transaction> getTransactions() {
        return transactions;
    }

    @Produces()
    @Named("carteiras")
    public Map<UUID, Carteira> getCarteiras() {
        return carteiras;
    }
}
