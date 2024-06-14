package br.maia.ticopay.transfers.control;

class MerchantCannotSendMoneyException extends IllegalArgumentException {
    MerchantCannotSendMoneyException(String message) {
        super(message);
    }
}
