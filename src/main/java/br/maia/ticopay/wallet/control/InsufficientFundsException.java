package br.maia.ticopay.wallet.control;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Saldo insuficiente");
    }
}
