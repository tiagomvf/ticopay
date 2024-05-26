package br.maia.ticopay.transfers.control;

import br.maia.ticopay.transfers.entity.Transfer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransferRepository implements PanacheRepository<Transfer> {

}
