package com.example.bank.aop;

import com.example.bank.service.BankingService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(TransactionLoggingAspect.class);
    private static final String TRANSACTION_METHODS =
            "execution(* com.example.bank.service.BankingService.deposit(..)) || " +
            "execution(* com.example.bank.service.BankingService.withdraw(..))";

    @Before(TRANSACTION_METHODS)
    public void logBeforeTransaction(JoinPoint joinPoint) {
        logger.info("Transaction started: method={}, amount={}",
                joinPoint.getSignature().getName(), joinPoint.getArgs()[0]);
    }

    @AfterReturning(pointcut = TRANSACTION_METHODS, returning = "result")
    public void logSuccessfulTransaction(JoinPoint joinPoint, Object result) {
        logger.info("Transaction successful: method={}, new balance={}",
                joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = TRANSACTION_METHODS, throwing = "exception")
    public void logFailedTransaction(JoinPoint joinPoint, Throwable exception) {
        logger.error("Transaction failed: method={}, reason={}",
                joinPoint.getSignature().getName(), exception.getMessage());
    }
}
