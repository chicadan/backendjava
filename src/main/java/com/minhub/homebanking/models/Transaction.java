package com.minhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

@Enumerated
    private TransactionType type;

    private Double amount;
    private String description;
    private LocalDateTime transactionDate;


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name ="account_id")
    private Account account;


    public Transaction() {
    }


    public Transaction(TransactionType type, Double amount, String description, LocalDateTime transactionDate) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.account = account;
    }

    public void setAccount(Account account) {
        this.account = account;

    }


    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setText(String text) {
        this.description = text;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDAte(LocalDateTime transactionDAte) {
        this.transactionDate = transactionDAte;
    }
}

