package br.maia.ticopay.transfers.control;

public class TransferAuthenticationException extends RuntimeException {
    public TransferAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransferAuthenticationException(String message) {
        super(message);
    }
}
