package com.minhub.homebanking.models;

import jdk.jfr.Description;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Double amount;

    private String text;
    private LocalDateTime transactionDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction(){
    }


    public Transaction(TransactionType type, Double amount, String text, LocalDateTime transactionDate, Account account) {
        this.type = type;
        this.amount = amount;
        this.text = text;
        this.transactionDate = transactionDate;
    }
}




