package com.minhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="client_id")
    private Client client;


    public Account(String number, LocalDate creationDate, Double balance) {

        this.id = getId();
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;

    }


    public Account() {

    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();

    public void addTransaction (Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

}

