# Smart Bank AOP

A small terminal-based banking application built with Java 17, Spring Boot, and Spring AOP. The application starts with a balance of **₹1,000.00**. Deposit and withdrawal logic lives in `BankingService`; transaction logging is kept separately in `TransactionLoggingAspect`.

## Requirements

- JDK 17 or later
- Apache Maven 3.6.3 or later
- Internet access the first time Maven downloads the Spring dependencies

## Run from Windows Terminal

1. Extract `Smart_Bank_AOP_Complete.zip`.
2. Open Windows Terminal in the extracted `smart-bank-aop` folder (the folder containing `pom.xml`).
3. Run:

   ```powershell
   mvn spring-boot:run
   ```

4. Use the terminal menu: `1` checks the balance, `2` deposits, `3` withdraws, and `0` exits.

If Maven is not installed, install Apache Maven and ensure `mvn -version` works in Windows Terminal. In Eclipse, use **File → Import → Maven → Existing Maven Projects**, select this folder, then run `SmartBankApplication.java` as a Java application. Make sure Eclipse has a JDK 17+ and Maven support installed.

## Build and test

Run all tests:

```powershell
mvn test
```

Build a runnable application JAR:

```powershell
mvn package
java -jar target/smart-bank-aop-1.0.0.jar
```

The tests cover the initial balance, successful deposits and withdrawals, insufficient-funds rejection, invalid amounts, unchanged balance after failures, Spring AOP proxy activation, and captured before/success/failure logs.

## AOP transaction logs

`TransactionLoggingAspect` applies to `BankingService.deposit` and `BankingService.withdraw`:

- `@Before` logs the method and requested amount before execution.
- `@AfterReturning` logs successful completion and the resulting balance.
- `@AfterThrowing` logs failed transactions and the exception message.

The logs appear in the application terminal. A failed withdrawal does not change the balance and also displays an explanatory message to the user.

## Project structure

```text
smart-bank-aop/
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/example/bank/
    │   ├── SmartBankApplication.java
    │   ├── aop/TransactionLoggingAspect.java
    │   ├── console/BankingConsoleRunner.java
    │   ├── exception/InsufficientFundsException.java
    │   └── service/BankingService.java
    ├── main/resources/application.properties
    └── test/java/com/example/bank/
        ├── aop/TransactionLoggingAspectTest.java
        └── service/BankingServiceTest.java
```
