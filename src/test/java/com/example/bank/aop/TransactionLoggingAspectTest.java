package com.example.bank.aop;

import com.example.bank.service.BankingService;
import com.example.bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "banking.console.enabled=false")
@ExtendWith(OutputCaptureExtension.class)
class TransactionLoggingAspectTest {
    @Autowired
    private BankingService bankingService;

    @Test
    void aspectLogsBeforeSuccessAndFailure(CapturedOutput output) {
        assertTrue(AopUtils.isAopProxy(bankingService), "BankingService should be wrapped by Spring AOP");
        assertEquals(0, new BigDecimal("1025.00").compareTo(
                bankingService.deposit(new BigDecimal("25.00"))));
        assertEquals(0, new BigDecimal("1000.00").compareTo(
                bankingService.withdraw(new BigDecimal("25.00"))));
        assertThrows(InsufficientFundsException.class,
                () -> bankingService.withdraw(new BigDecimal("1000.01")));

        assertTrue(output.getAll().contains("Transaction started: method=deposit, amount=25.00"));
        assertTrue(output.getAll().contains("Transaction successful: method=deposit, new balance=1025.00"));
        assertTrue(output.getAll().contains("Transaction failed: method=withdraw, reason=Withdrawal failed:"));
    }
}
