package br.maia.ticopay.wallet.control;

import br.maia.ticopay.wallet.entity.Wallet;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
class WalletStoreTest {

    @Inject
    EntityManager em;

    @Inject
    WalletStore walletStore;

    @BeforeEach
    @Transactional
    void setUp() {
        Wallet w1 = new Wallet();
        w1.setBalance(BigDecimal.ONE);
        w1.setOwnerId(1L);
        em.persist(w1);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        Query query = em.createQuery("delete Wallet w where w.ownerId in (1L)");
        query.executeUpdate();
    }

    @Test
    void depositar() {
        walletStore.depositar(1L, BigDecimal.ONE);
        BigDecimal balance = walletStore.findById(1L).getBalance();
        Assertions.assertEquals(0, BigDecimal.TWO.compareTo(balance));
    }

    @Test
    void sacar() {
        walletStore.sacar(1L, BigDecimal.ONE);
        BigDecimal balance = walletStore.findById(1L).getBalance();
        Assertions.assertEquals(0, BigDecimal.ZERO.compareTo(balance));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance(){
        Assertions.assertThrowsExactly(InsufficientFundsException.class, () -> walletStore.sacar(1L,
            BigDecimal.TWO), "Saldo Insuficiente");
        BigDecimal balance = walletStore.findById(1L).getBalance();
        Assertions.assertEquals(1, BigDecimal.TWO.compareTo(balance));
    }

}
