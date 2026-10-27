package com.example.bank.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal requested, BigDecimal available) {
        super("Withdrawal failed: requested amount ₹" + requested.toPlainString()
                + " exceeds available balance ₹" + available.toPlainString() + ".");
    }
}
