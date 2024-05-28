package br.maia.ticopay.wallet;

import br.maia.ticopay.wallet.control.WalletNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WalletNotFoundExceptionMapper implements ExceptionMapper<WalletNotFoundException> {
    @Override
    public Response toResponse(WalletNotFoundException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
