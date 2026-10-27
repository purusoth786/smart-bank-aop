package com.example.bank.service;

import com.example.bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankingServiceTest {
    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        bankingService = new BankingService();
    }

    @Test
    void startsWithOneThousandAndUpdatesBalanceAfterDepositAndWithdrawal() {
        assertMoney("1000.00", bankingService.getBalance());
        assertMoney("1250.00", bankingService.deposit(new BigDecimal("250.00")));
        assertMoney("1100.00", bankingService.withdraw(new BigDecimal("150.00")));
        assertMoney("1100.00", bankingService.getBalance());
    }

    @Test
    void rejectsWithdrawalAboveAvailableBalanceWithoutChangingBalance() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                () -> bankingService.withdraw(new BigDecimal("1000.01")));
        assertEquals("Withdrawal failed: requested amount ₹1000.01 exceeds available balance ₹1000.00.",
                exception.getMessage());
        assertMoney("1000.00", bankingService.getBalance());
    }

    @Test
    void rejectsZeroOrNegativeTransactionAmounts() {
        assertThrows(IllegalArgumentException.class,
                () -> bankingService.deposit(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> bankingService.withdraw(new BigDecimal("-1.00")));
        assertMoney("1000.00", bankingService.getBalance());
    }

    private void assertMoney(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }
}
