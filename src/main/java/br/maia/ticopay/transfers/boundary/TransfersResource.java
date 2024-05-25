package br.maia.ticopay.transfers.boundary;

import br.maia.ticopay.transfers.control.TransfersStore;
import br.maia.ticopay.transfers.entity.Transfer;
import br.maia.ticopay.wallet.control.InsufficientFundsException;
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

import java.math.BigDecimal;
import java.net.URI;

@Transactional
@RequestScoped
@Path("/transfer")
public class TransfersResource {

    @Inject
    TransfersStore store;

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTransfer(Payload payload, @Context UriInfo uriInfo) {
        System.out.println("teste");
        Transfer transfer = postTransfer(payload);
        URI location = getLocation(uriInfo, transfer.id());
        return Response.created(location).build();
    }

    private Transfer postTransfer(Payload payload) {
        try {
            return store.postTransfer(payload.payer(), payload.payee(), payload.value());
        } catch (InsufficientFundsException e) {
            throw new BadRequestException(e);
        }
    }

    private static URI getLocation(UriInfo uriInfo, Long transactionId) {
        return uriInfo.getRequestUriBuilder().segment(transactionId.toString()).build();
    }

    @GET
    @Path("/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transfer getTransfer(@PathParam("transactionId") Long transactionId) {
        return store.get(transactionId);
    }

    public record Payload( BigDecimal value, long payer, long payee) { }

}
