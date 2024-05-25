package br.maia.ticopay.transfers.entity;

import java.math.BigDecimal;

public record Transfer(Long id, Long payer, Long payee, BigDecimal value) { }
