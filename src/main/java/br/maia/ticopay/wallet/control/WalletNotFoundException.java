package br.maia.ticopay.wallet.control;

// todo: Add exception mapper do badrequest
public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
