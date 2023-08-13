package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;

    private TransactionType type;

    private Double amount;
    private String description;
    private LocalDateTime date;


    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getTransactionDate();


    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getText() {
        return description;
    }

    public LocalDateTime getTransactionDate() {
        return date;
    }
}



