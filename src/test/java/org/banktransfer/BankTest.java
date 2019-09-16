package org.banktransfer;

import org.banktransfer.model.Account;
import org.banktransfer.model.User;
import org.banktransfer.service.Bank;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BankTest {

    @Test
    public void addUser() {
        final Map<User, List<Account>> expected = Map.of(
                new User("name1", "100"), new ArrayList<>(),
                new User("name2", "200"), new ArrayList<>(),
                new User("name3", "300"), new ArrayList<>()
        );


        final Bank bank = new Bank();
        bank.addUser(new User("name1", "100"));
        bank.addUser(new User("name2", "200"));
        bank.addUser(new User("name3", "300"));
        final Map<User, List<Account>> result = bank.getMap();

        assertThat(result, is(expected));
    }

    @Test
    public void deleteUser() {
        final Map<User, List<Account>> expected = Map.of(
                new User("name1", "100"), new ArrayList<>()
        );

        final Bank bank = new Bank();
        bank.addUser(new User("name1", "100"));
        bank.addUser(new User("name2", "200"));
        bank.deleteUser(new User("name2", "200"));
        final Map<User, List<Account>> result = bank.getMap();

        assertThat(result, is(expected));
    }

    @Test
    public void addAccountToUser() {
        final Map<User, List<Account>> expected = Map.of(
                new User("name1", "100"), List.of(new Account(new BigDecimal(10000), "1001"))
        );

        final Bank bank = new Bank();
        bank.addUser(new User("name1", "100"));
        bank.addAccountToUser("100", new Account(new BigDecimal(10000), "1001"));
        final Map<User, List<Account>> result = bank.getMap();

        assertThat(result, is(expected));
    }

    @Test
    public void deleteAccountFromUser() {
        final Map<User, List<Account>> expected = Map.of(
                new User("name1", "100"),
                List.of(new Account(new BigDecimal(10000), "1002"))
        );

        final Bank bank = new Bank();
        bank.addUser(new User("name1", "100"));
        bank.addAccountToUser("100", new Account(new BigDecimal(10000), "1001"));
        bank.addAccountToUser("100", new Account(new BigDecimal(10000), "1002"));
        bank.deleteAccountFromUser("100", new Account("1001"));
        final Map<User, List<Account>> result = bank.getMap();

        assertThat(result, is(expected));
    }

    @Test
    public void getUserAccounts() {
        final List<Account> expected = List.of(
                new Account(new BigDecimal(10000), "1001"),
                new Account(new BigDecimal(10000), "1002")
        );

        final Bank bank = new Bank();
        bank.addUser(new User("name1", "100"));
        bank.addAccountToUser("100", new Account(new BigDecimal(10000), "1001"));
        bank.addAccountToUser("100", new Account(new BigDecimal(10000), "1002"));
        final List<Account> result = bank.getUserAccounts("100");

        assertThat(result, is(expected));
    }

    @Test
    public void transferMoneyPositiveCase() {
        final Bank bank = new Bank();
        final User sender = new User("senderName", "senderPassport");
        final String senderRequisites = "1001";
        final User recipient = new User("recipientName", "recipientPassport");
        final String recipientRequisites = "1002";
        bank.addUser(sender);
        bank.addAccountToUser(sender.getPassport(), new Account(new BigDecimal(10000), senderRequisites));
        bank.addUser(recipient);
        bank.addAccountToUser(recipient.getPassport(), new Account(new BigDecimal(10000), recipientRequisites));

        final boolean hasTransferred = bank.transferMoney(
                sender.getPassport(),
                senderRequisites,
                recipient.getPassport(),
                recipientRequisites,
                new BigDecimal(500)
        );

        assertThat(hasTransferred, is(true));

        final Account senderAccount = bank.getUserAccount(sender.getPassport(), senderRequisites);
        final Account recipientAccount = bank.getUserAccount(recipient.getPassport(), recipientRequisites);

        assertThat(senderAccount.getValue(), is(new BigDecimal(9500)));
        assertThat(recipientAccount.getValue(), is(new BigDecimal(10500)));
    }

    @Test
    public void transferMoneySourceAccountNotFound() {
        final Bank bank = new Bank();
        final User sender = new User("senderName", "senderPassport");
        final String senderRequisites = "1001";
        final User recipient = new User("recipientName", "recipientPassport");
        final String recipientRequisites = "1002";
        bank.addUser(sender);
        bank.addAccountToUser(sender.getPassport(), new Account(new BigDecimal(10000), senderRequisites));
        bank.addUser(recipient);
        bank.addAccountToUser(recipient.getPassport(), new Account(new BigDecimal(10000), recipientRequisites));

        final boolean hasTransferred = bank.transferMoney(
                sender.getPassport(),
                "not found",
                recipient.getPassport(),
                recipientRequisites,
                new BigDecimal(500)
        );

        assertThat(hasTransferred, is(false));

        final Account senderAccount = bank.getUserAccount(sender.getPassport(), senderRequisites);
        final Account recipientAccount = bank.getUserAccount(recipient.getPassport(), recipientRequisites);

        assertThat(senderAccount.getValue(), is(new BigDecimal(10000)));
        assertThat(recipientAccount.getValue(), is(new BigDecimal(10000)));
    }

    @Test
    public void transferMoneyDestinationAccountNotFound() {
        final Bank bank = new Bank();
        final User sender = new User("senderName", "senderPassport");
        final String senderRequisites = "1001";
        final User recipient = new User("recipientName", "recipientPassport");
        final String recipientRequisites = "1002";
        bank.addUser(sender);
        bank.addAccountToUser(sender.getPassport(), new Account(new BigDecimal(10000), senderRequisites));
        bank.addUser(recipient);
        bank.addAccountToUser(recipient.getPassport(), new Account(new BigDecimal(10000), recipientRequisites));

        final boolean hasTransferred = bank.transferMoney(
                sender.getPassport(),
                senderRequisites,
                recipient.getPassport(),
                "not found",
                new BigDecimal(500)
        );

        assertThat(hasTransferred, is(false));

        final Account senderAccount = bank.getUserAccount(sender.getPassport(), senderRequisites);
        final Account recipientAccount = bank.getUserAccount(recipient.getPassport(), recipientRequisites);

        assertThat(senderAccount.getValue(), is(new BigDecimal(10000)));
        assertThat(recipientAccount.getValue(), is(new BigDecimal(10000)));
    }

    @Test
    public void transferMoneyNotEnoughMoneyInSourceAccount() {
        final Bank bank = new Bank();
        final User sender = new User("senderName", "senderPassport");
        final String senderRequisites = "1001";
        final User recipient = new User("recipientName", "recipientPassport");
        final String recipientRequisites = "1002";
        bank.addUser(sender);
        bank.addAccountToUser(sender.getPassport(), new Account(new BigDecimal(10000), senderRequisites));
        bank.addUser(recipient);
        bank.addAccountToUser(recipient.getPassport(), new Account(new BigDecimal(10000), recipientRequisites));

        final boolean hasTransferred = bank.transferMoney(
                sender.getPassport(),
                senderRequisites,
                recipient.getPassport(),
                recipientRequisites,
                new BigDecimal(20000)
        );

        assertThat(hasTransferred, is(false));

        final Account senderAccount = bank.getUserAccount(sender.getPassport(), senderRequisites);
        final Account recipientAccount = bank.getUserAccount(recipient.getPassport(), recipientRequisites);

        assertThat(senderAccount.getValue(), is(new BigDecimal(10000)));
        assertThat(recipientAccount.getValue(), is(new BigDecimal(10000)));
    }
}
