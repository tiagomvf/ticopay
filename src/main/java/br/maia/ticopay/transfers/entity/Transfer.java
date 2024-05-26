package br.maia.ticopay.transfers.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long payer;
    Long payee;
    BigDecimal value;

    public Transfer(){
        super();
    }

    public Transfer(Long payer, Long payee, BigDecimal value) {
        this.payer = payer;
        this.payee = payee;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Long getPayer() {
        return payer;
    }

    public void setPayer(Long payer) {
        this.payer = payer;
    }

    public Long getPayee() {
        return payee;
    }

    public void setPayee(Long payee) {
        this.payee = payee;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
