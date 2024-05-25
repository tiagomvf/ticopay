package br.maia.ticopay;

import br.maia.ticopay.carteira.entity.Carteira;
import br.maia.ticopay.transfers.boundary.TransfersResource;
import br.maia.ticopay.transfers.entity.Transfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class GlobalStoreProvider {

    Map<Long, Transfer> transactions = new HashMap<>();
    Map<Long, Carteira> carteiras = new HashMap<>();

    @Produces()
    @Named("transactions")
    public Map<Long, Transfer> getTransfers() {
        return transactions;
    }

    @Produces()
    @Named("carteiras")
    public Map<Long, Carteira> getCarteiras() {
        return carteiras;
    }
}
