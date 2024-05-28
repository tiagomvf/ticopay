package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;

@Dependent
@Transactional(Transactional.TxType.MANDATORY)
public class WalletRepository implements PanacheRepository<Wallet> { }
