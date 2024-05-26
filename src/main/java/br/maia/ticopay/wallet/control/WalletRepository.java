package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WalletRepository implements PanacheRepository<Wallet> {

}
