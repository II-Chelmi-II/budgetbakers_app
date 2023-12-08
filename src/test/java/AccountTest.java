import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.michel.model.*;

class AccountTest {

    @Test
    void performTransaction_creditTransaction_balanceUpdated() {
        Account account = createTestAccount();
        double initialBalance = account.getBalance().getAmount();

        account.performTransaction("Crédit", 50.0, TransactionType.CREDIT);

        assertEquals(initialBalance + 50.0, account.getBalance().getAmount());
    }

    @Test
    void performTransaction_debitTransactionWithSufficientBalance_balanceUpdated() {
        Account account = createTestAccount();
        double initialBalance = account.getBalance().getAmount();

        account.performTransaction("Débit", 30.0, TransactionType.DEBIT);

        assertEquals(initialBalance - 30.0, account.getBalance().getAmount());
    }

    @Test
    void performTransaction_debitTransactionInsufficientBalance_throwException() {
        Account account = createTestAccount();

        assertThrows(IllegalArgumentException.class,
                () -> account.performTransaction("Débit", 150.0, TransactionType.DEBIT));
    }

    @Test
    void getBalanceAtDateTime_balanceAtSpecificDateTime_returnCorrectBalance() {
        Account account = createTestAccount();
        LocalDateTime dateTime = LocalDateTime.now();

        double balanceAtDateTime = account.getBalanceAtDateTime(dateTime);

        assertEquals(account.getBalance().getAmount(), balanceAtDateTime);
    }

    @Test
    void getBalanceHistory_balanceHistoryInTimeInterval_returnCorrectHistory() {
        Account account = createTestAccount();
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        List<Double> balanceHistory = account.getBalanceHistory(startDate, endDate);

        assertEquals(2, balanceHistory.size());
    }

    @Test
    void transferMoney_validTransferFromSourceToTarget_accountsUpdated() {
        Account sourceAccount = createTestAccount();
        Account targetAccount = createTestAccount();
        double transferAmount = 50.0;

        Account.transferMoney(sourceAccount, targetAccount, transferAmount);

        assertEquals(sourceAccount.getBalance().getAmount() - transferAmount, sourceAccount.getBalance().getAmount());
        assertEquals(targetAccount.getBalance().getAmount() + transferAmount, targetAccount.getBalance().getAmount());
    }

    @Test
    void transferMoney_transferToSameAccount_throwException() {
        Account account = createTestAccount();
        double transferAmount = 50.0;

        assertThrows(IllegalArgumentException.class,
                () -> Account.transferMoney(account, account, transferAmount));
    }

    private Account createTestAccount() {
        return new Account(
                1,
                "Test Account",
                new Balance(100.0, LocalDateTime.now().toString()),
                List.of(),
                new Currency(1, "Euro", "EUR"),
                AccountType.BANQUE
        );
    }

}
