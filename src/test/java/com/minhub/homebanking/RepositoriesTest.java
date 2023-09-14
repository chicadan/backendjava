package com.minhub.homebanking;

import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;


    //LOAN
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal Loan"))));
    }

    @Test
    public void existMortgageLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Mortgage"))));
    }

    //CLIENT
    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void findClientByEmail() {
        String clientEmail = "memo@mindhub.com";
        Client foundClient = clientRepository.findByEmail(clientEmail);
        assertThat(foundClient, notNullValue());

    }
    //CARD
    @Test
    public void findCardsByClient() {
        String clientEmail = "agon@mindhub.com";
        Client client = clientRepository.findByEmail(clientEmail);
        List<Card> clientCards = cardRepository.findByClient(client);

        assertThat(clientCards, is(not(empty())));

        for (Card card : clientCards) {
            assertThat(card.getClient(), is(equalTo(client)));
        }
    }

    @Test
    public void existCreditCards() {
        List<Card> cards = cardRepository.findAll();

        boolean creditCardExists = cards.stream()
                .anyMatch(card -> card.getType() == CardType.CREDIT);

        assertTrue(creditCardExists);
    }


    //ACCOUNT
    @Test
    public void allAccountsHaveNumber() {
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            assertNotNull(account.getNumber());
        }

    }

    @Test
    public void existAccountNumber() {
        Account account = new Account();
        account.setNumber("VIN001");
        accountRepository.save(account);

        Account savedAccount = accountRepository.findById(account.getId()).orElse(null);
        assertTrue(savedAccount != null && savedAccount.getNumber() != null);
    }

    //TRANSACTION
    @Test
    public void allTransactionsHaveDate() {
        List<Transaction> transactions = transactionRepository.findAll();

        for (Transaction transaction : transactions) {
            assertNotNull(transaction.getTransactionDate());
        }
    }

    @Test
    public void allTransactionsHaveAmount() {
        List<Transaction> transactions = transactionRepository.findAll();

        for (Transaction transaction : transactions) {
            assertNotNull(transaction.getAmount());
        }
    }
}

