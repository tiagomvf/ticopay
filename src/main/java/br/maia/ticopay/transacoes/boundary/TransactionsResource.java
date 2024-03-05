package br.maia.ticopay.transacoes.boundary;

import br.maia.ticopay.carteira.control.SaldoInsuficienteException;
import br.maia.ticopay.transacoes.control.TransactionStore;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.UUID;

@Transactional
@RequestScoped
@Path("/transactions")
public class TransactionsResource {

    @Inject
    TransactionStore store;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTransaction(@Valid Payload payload, @Context UriInfo uriInfo) {
        UUID transactionId = UUID.randomUUID();
        postTransaction(payload, transactionId);
        URI location = getLocation(uriInfo, transactionId);
        return Response.created(location).build();
    }

    private void postTransaction(Payload payload, UUID transactionId) {
        try {
            store.postTransaction(transactionId, payload.payer(), payload.payee(), payload.value());
        } catch (SaldoInsuficienteException e) {
            throw new BadRequestException(e);
        }
    }

    private static URI getLocation(UriInfo uriInfo, UUID transactionId) {
        return uriInfo.getRequestUriBuilder().segment(transactionId.toString()).build();
    }

    @GET
    @Path("/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionStore.Transaction getTransaction(@PathParam("transactionId") UUID transactionId) {
        return store.get(transactionId);
    }

    public record Payload(@NotNull Double value, @NotNull UUID payer, @NotNull UUID payee) { }

    public record Transaction(UUID id, UUID payer, UUID payee, Double value) {
    }
}
