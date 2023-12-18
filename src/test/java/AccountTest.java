import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.michel.model.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void testPerformTransaction() {
        // Créer un compte pour le test
        Account account = new Account(1, "John Doe", new Balance(1000.0, LocalDateTime.now().toString()), null, new Currency(1, "USD", "$"), AccountType.CHECKING);

        // Effectuer une transaction de crédit
        account.performTransaction("Salaire", 500.0, TransactionType.CREDIT);

        // Vérifier le solde après la transaction de crédit
        assertEquals(1500.0, account.getBalance().getAmount());

        // Effectuer une transaction de débit
        account.performTransaction("Achat en ligne", 200.0, TransactionType.DEBIT, Account.TransactionCategory.ACHATS_EN_LIGNE);

        // Vérifier le solde après la transaction de débit
        assertEquals(1300.0, account.getBalance().getAmount());
    }

    @Test
    void testGetBalanceAtDateTime() {
        // Créer un compte pour le test
        Account account = new Account(1, "Jane Doe", new Balance(1000.0, LocalDateTime.now().toString()), null, new Currency(1, "USD", "$"), AccountType.CHECKING);

        // Effectuer une transaction de crédit
        account.performTransaction("Dépôt initial", 500.0, TransactionType.CREDIT);

        // Obtener le solde à un moment spécifique après la transaction
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        double balanceAtDateTime = account.getBalanceAtDateTime(dateTime);

        // Vérifier le solde attendu
        assertEquals(1500.0, balanceAtDateTime);
    }

    @Test
    void testTransferMoney() {
        // Créer deux comptes pour le test
        Account sourceAccount = new Account(1, "Alice", new Balance(1000.0, LocalDateTime.now().toString()), null, new Currency(1, "USD", "$"), AccountType.CHECKING);
        Account targetAccount = new Account(2, "Bob", new Balance(500.0, LocalDateTime.now().toString()), null, new Currency(1, "USD", "$"), AccountType.CHECKING);

        // Effectuer un transfert d'argent
        Account.transferMoney(sourceAccount, targetAccount, 200.0);

        // Vérifier le solde du compte source après le transfert
        assertEquals(800.0, sourceAccount.getBalance().getAmount());

        // Vérifier le solde du compte cible après le transfert
        assertEquals(700.0, targetAccount.getBalance().getAmount());
    }

    @Test
    void testGetBalanceHistory() {
        // Créer un compte pour le test
        Account account = new Account(1, "Bob Smith", new Balance(1000.0, LocalDateTime.now().toString()), null, new Currency(1, "USD", "$"), AccountType.CHECKING);

        // Effectuer des transactions sur une période
        account.performTransaction("Achat", 200.0, TransactionType.DEBIT, Account.TransactionCategory.ACHATS_EN_LIGNE);
        account.performTransaction("Salaire", 500.0, TransactionType.CREDIT);

        // Obtener l'historique du solde entre deux dates
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        List<Double> balanceHistory = account.getBalanceHistory(startDate, endDate);

        // Afficher l'historique du solde
        System.out.println("Balance History:");
        for (Double balance : balanceHistory) {
            System.out.println(balance);
        }
    }
}
