package br.maia.ticopay.transfers.control;

import br.maia.ticopay.actors.entity.Merchant;
import br.maia.ticopay.actors.entity.User;
import br.maia.ticopay.wallet.control.InsufficientFundsException;
import br.maia.ticopay.wallet.control.WalletNotFoundException;
import br.maia.ticopay.wallet.control.WalletStore;
import br.maia.ticopay.wallet.entity.Wallet;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TransfersStoreTest {

    @Inject
    EntityManager em;

    @Inject
    TransfersStore transfersStore;

    @Inject
    WalletStore walletStore;

    Long user1Id;
    Long user2Id;
    Long merchant1Id;

    @Transactional
    @BeforeEach
    public void setUp() {
        User user1 = new User();
        user1.setCpf("954.272.120-00");
        user1.setName("Jane Doe");
        user1.setEmail("user1@gmail.com");
        user1.setPassword("verystrongpassword");
        em.persist(user1);
        this.user1Id = user1.getId();
        Wallet walletUser1 = new Wallet();
        walletUser1.setOwnerId(user1.getId());
        walletUser1.setBalance(BigDecimal.TEN);
        em.persist(walletUser1);
        User user2 = new User();
        user2.setCpf("697.554.320-86");
        user2.setName("John Doe");
        user2.setEmail("user2@gmail.com");
        user2.setPassword("reallystrongpassword");
        em.persist(user2);
        this.user2Id = user2.getId();
        Wallet walletUser2 = new Wallet();
        walletUser2.setOwnerId(user2.getId());
        walletUser2.setBalance(BigDecimal.TEN);
        em.persist(walletUser2);
        Merchant merchant1 = new Merchant();
        merchant1.setCpf("393.872.440-40");
        merchant1.setName("Karen Doe");
        merchant1.setEmail("merchant1@gmail.com");
        merchant1.setPassword("reallyverystrongpassword");
        em.persist(merchant1);
        this.merchant1Id = merchant1.getId();
        Wallet walletMerchant1 = new Wallet();
        walletMerchant1.setOwnerId(merchant1.getId());
        walletMerchant1.setBalance(BigDecimal.TEN);
        em.persist(walletMerchant1);
    }

    @Transactional
    @AfterEach
    public void tearDown() {
        em.createQuery("delete from Transfer").executeUpdate();
        em.createQuery("delete from Wallet ").executeUpdate();
        em.createQuery("delete from Merchant ").executeUpdate();
        em.createQuery("delete from User ").executeUpdate();
    }

    @Test
    @Transactional
    public void itShouldPersistTransferSuccessfully() {
        transfersStore.authorizationHandler = (x) -> true;
        transfersStore.postTransfer(user1Id, merchant1Id, BigDecimal.TEN);
        assertEquals(0, BigDecimal.ZERO.compareTo(walletStore.getBalance(user1Id)));
        assertEquals(0, BigDecimal.valueOf(20.00).compareTo(walletStore.getBalance(merchant1Id)));
    }

    @Test
    public void itShouldThrowExceptionWhenPayeeWalletNotExisits() {
        transfersStore.authorizationHandler = (x) -> true;
        assertThrowsExactly(
            WalletNotFoundException.class,
            () -> transfersStore.postTransfer(user1Id, 999L, BigDecimal.TEN));
    }

    @Test
    public void itShouldThrowExceptionWhenPayerWalletNotExisits() {
        transfersStore.authorizationHandler = (x) -> true;
        assertThrowsExactly(
            WalletNotFoundException.class,
            () -> transfersStore.postTransfer(999L, merchant1Id, BigDecimal.TEN));
    }

    @Test
    public void itShouldThrowExceptionWhenPayerDoesNotHaveFunds() {
        transfersStore.authorizationHandler = (x) -> true;
        assertThrowsExactly(
            InsufficientFundsException.class,
            () -> transfersStore.postTransfer(user1Id, user2Id, new BigDecimal(11)));
    }

    @Test
    public void itShouldNotAllowMerchantAsPayer() {
        transfersStore.authorizationHandler = (x) -> true;
        MerchantCannotSendMoneyException exception = assertThrowsExactly(
            MerchantCannotSendMoneyException.class,
            () -> transfersStore.postTransfer(merchant1Id, user1Id, BigDecimal.TEN)
        );
        assertEquals("Merchant cannot send money.", exception.getMessage());
    }

    @Test
    public void itShouldThrowExceptionWhenTransferNotAuthorized() {
        transfersStore.authorizationHandler = (x) -> false;
        TransferNotAuthorizedException exception = assertThrowsExactly(
            TransferNotAuthorizedException.class,
            () -> transfersStore.postTransfer(user1Id, merchant1Id, BigDecimal.TEN)
        );
    }
}
