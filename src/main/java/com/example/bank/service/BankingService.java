package com.example.bank.service;

import com.example.bank.exception.InsufficientFundsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankingService {
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private BigDecimal balance = INITIAL_BALANCE;

    public synchronized BigDecimal deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
        return balance;
    }

    public synchronized BigDecimal withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException(amount, balance);
        }
        balance = balance.subtract(amount);
        return balance;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero.");
        }
    }
}
