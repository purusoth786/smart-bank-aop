package com.example.bank.console;

import com.example.bank.service.BankingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Scanner;

@Configuration
public class BankingConsoleRunner {

    @Bean
    @ConditionalOnProperty(name = "banking.console.enabled", havingValue = "true", matchIfMissing = true)
    CommandLineRunner bankingConsole(BankingService bankingService) {
        return args -> runMenu(bankingService);
    }

    private void runMenu(BankingService bankingService) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\n=== SMART BANK ===");
            System.out.println("Starting balance: ₹" + bankingService.getBalance().toPlainString());
            boolean running = true;
            while (running) {
                System.out.println("\n1. Check balance\n2. Deposit\n3. Withdraw\n0. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> System.out.println("Current balance: ₹" + bankingService.getBalance().toPlainString());
                        case "2" -> transact(scanner, bankingService, true);
                        case "3" -> transact(scanner, bankingService, false);
                        case "0" -> running = false;
                        default -> System.out.println("Invalid choice. Select 1, 2, 3, or 0.");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Transaction failed: enter a valid numeric amount.");
                } catch (RuntimeException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println("Thank you for using Smart Bank.");
        }
    }

    private void transact(Scanner scanner, BankingService bankingService, boolean deposit) {
        System.out.print("Enter amount: ₹");
        BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
        BigDecimal newBalance = deposit
                ? bankingService.deposit(amount)
                : bankingService.withdraw(amount);
        System.out.println((deposit ? "Deposit" : "Withdrawal")
                + " successful. Current balance: ₹" + newBalance.toPlainString());
    }
}
