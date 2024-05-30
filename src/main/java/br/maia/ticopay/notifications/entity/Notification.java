package br.maia.ticopay.notifications.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Notification {
    public enum Status {
        SENT, NOT_SENT, ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(nullable = false, updatable = false)
    Long payer;
    @Column(nullable = false, updatable = false)
    Long payee;
    @Column(nullable = false, updatable = false)
    BigDecimal value;
    @Column(nullable = false, updatable = true)
    Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Notification() {
        status = Status.NOT_SENT;
    }

    public Notification(Long payer, Long payee, BigDecimal value) {
        this();
        this.payer = payer;
        this.payee = payee;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
