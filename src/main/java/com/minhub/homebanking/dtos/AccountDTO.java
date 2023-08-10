package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {

    private Long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    public AccountDTO (Account account){
        id = account.getId();
        number = account.getNumber();
        creationDate = account.getCreationDate();
        balance = account.getBalance();

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
}
