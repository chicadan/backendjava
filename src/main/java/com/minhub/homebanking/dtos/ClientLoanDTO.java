package com.minhub.homebanking.dtos;

import com.minhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private Long id;
    private Long loanId;
    private String name;
    private Double amount;
    private Integer payment;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payment = clientLoan.getPayment();

    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }
}
