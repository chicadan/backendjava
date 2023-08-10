package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Transaction;
import com.minhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private LocalDateTime date;
    private Double amount;
    private TransactionType type; // Agregar el campo type

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getTransactionDate();
        this.amount = transaction.getAmount();
        this.type = transaction.getType(); // Asignar el tipo desde la entidad Transaction
    }

    public Long getId() {
        return id;
    }



    public LocalDateTime getTransactionDate() {
        return date;
    }


    public Double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }


}



