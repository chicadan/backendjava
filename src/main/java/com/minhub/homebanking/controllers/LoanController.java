package com.minhub.homebanking.controllers;

import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.dtos.LoanApplicationDTO;
import com.minhub.homebanking.dtos.LoanDTO;
import com.minhub.homebanking.models.*;
import com.minhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return  loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

            //CLIENT AUTH
            Client client = clientRepository.findByEmail(authentication.getName());
            if(client == null)
                return new ResponseEntity<>("Invalid Client",HttpStatus.NOT_FOUND);

            //LOAN EXIST
            Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
            if (loan == null) {
                 return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
            //CHECK AMOUNT & PAYMENTS != ZERO
            if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0) {
                return new ResponseEntity<>("Invalid loan data", HttpStatus.FORBIDDEN);
            }

            // CHECK AMOUNT < MAX AMOUNT
            if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
                return new ResponseEntity<>("Amount exceeds the allowed limit", HttpStatus.FORBIDDEN);
            }

            //CHECK NUMBER PAYMENTS AVAILABLE
            if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
                return new ResponseEntity<>("Unavailable number of payments", HttpStatus.FORBIDDEN);
            }

            // CHECK EXIST TO ACCOUNT NUMBER
            Account toAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
            if (toAccount == null) {
                return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
            }

            //CHECK TO ACCOUNT NUMBER BELONG TO CLIENT AUTH
            if (!client.getAccounts().contains(toAccount)) {
                return new ResponseEntity<>("Unauthorized to use destination account", HttpStatus.FORBIDDEN);
            }

            //NEW LOAN
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()* 1.2, loanApplicationDTO.getPayments());// +20% TAX
            //ADD
            loan.addClientLoan(clientLoan);
            client.addClientLoan(clientLoan);
            clientLoanRepository.save(clientLoan);


            //NEW TRANSACTION
            Transaction creditTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), "Loan  " + clientLoan.getLoan().getName() + " Approved", LocalDateTime.now());
            //ADD
            toAccount.addTransaction(creditTransaction);

            //UPDATE BALANCE
            toAccount.setBalance(toAccount.getBalance()+ loanApplicationDTO.getAmount());

            // SAVE
            transactionRepository.save(creditTransaction);
            accountRepository.save(toAccount);

            //SAVE
            clientLoanRepository.save(clientLoan);

            return new ResponseEntity<>("Request Created", HttpStatus.CREATED);
        }


}
