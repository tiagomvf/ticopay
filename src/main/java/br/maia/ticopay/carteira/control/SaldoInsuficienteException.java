package br.maia.ticopay.carteira.control;

import jakarta.ws.rs.BadRequestException;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super("Saldo insuficiente");
    }
}
