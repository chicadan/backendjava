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
        List<Loan> listLoan = loanRepository.findAll();
        return listLoan.stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {


        // Verificar que los datos sean correctos, es decir no estén vacíos, que el monto no sea 0 o que las cuotas no sean 0. OK
        //Verificar que el préstamo exista OK
        //Verificar que el monto solicitado no exceda el monto máximo del préstamo OK
        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo Ok
        //Verificar que la cuenta de destino exista OK
        //Verificar que la cuenta de destino pertenezca al cliente autenticado OK

        // Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        // Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        // Se debe actualizar la cuenta de destino sumando el monto solicitado.*/

   /* GET /api/loans ninguno Json con los préstamos disponibles
    POST /api/loans solicitud de préstamo creado al cliente autenticado
                    transacción creada para cuenta de destino
                    cuenta de destino actualizada con el  monto

    éxito: 201 created
    respuestas de error:
            403 forbidden, si alguno de los datos no es válido
            403 forbidden, si la cuenta de destino no existe
            403 forbidden, si la cuenta de destino no pertenece al cliente autenticado
            403 forbidden, si el préstamo no existe
            403 forbidden, si el monto solicitado supera el monto máximo permitido del préstamo solicitado
            403 forbidden, si la cantidad de cuotas no está  disponible para el préstamo solicitado*/
        // CHECK CLIENT AUTH
                Client client = clientRepository.findByEmail(authentication.getName());

        String toAccountNumber = loanApplicationDTO.getToAccountNumber();
        Account toAccount = accountRepository.findByNumber(toAccountNumber);
        Double originalAmount = loanApplicationDTO.getAmount();
        Double totalAmount = originalAmount * 1.2;



        //CHECK REQUEST BODY EMPTY
                if (loanApplicationDTO.getLoanId()== null  ||loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0 || toAccountNumber.isBlank()) {
                    return new ResponseEntity<>("Invalid loan data", HttpStatus.FORBIDDEN);
                }
        //CHECK LOAN EXISTS
                 if (!loanRepository.existsById(loanApplicationDTO.getLoanId())) {
                    return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
            }

        // CHECK EXIST TO ACCOUNT NUMBER
                     if (!accountRepository.existsByNumber(toAccountNumber)) {
                        return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
                    }
        //CHECK TO ACCOUNT NUMBER BELONG TO CLIENT AUTH

                Client authClient = clientRepository.findByEmail(authentication.getName());
                    if (!authClient.getAccounts().contains(toAccount)) {
                        return new ResponseEntity<>("Unauthorized to use destination account", HttpStatus.FORBIDDEN);
                }

         // CHECK AMOUNT < MAX AMOUNT
                Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
                    if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
                        return new ResponseEntity<>("Amount exceeds the allowed limit", HttpStatus.FORBIDDEN);
                }
        //CHECK NUMBER PAYMENTS AVAILABLE
                    if(loan.getPayments().equals(loanApplicationDTO.getPayments())){
                        return new ResponseEntity<>("Unavailable number of payments", HttpStatus.FORBIDDEN);
            }


        //NEW LOAN
        ClientLoan clientLoan = new ClientLoan(totalAmount, loanApplicationDTO.getPayments());// +20% TAX
        //NEW TRANSACTION
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), "Loan Approved: " + clientLoan.getLoan().getName() + toAccountNumber, LocalDateTime.now());

        //UPDATE BALANCE
        toAccount.setBalance(toAccount.getBalance() + loanApplicationDTO.getAmount());
        toAccount.addTransaction(creditTransaction);

        //ADD LOAN TO CLIENT
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);

        clientLoanRepository.save(clientLoan);
        transactionRepository.save(creditTransaction);
        accountRepository.save(toAccount);


        return new ResponseEntity<>("Request Created", HttpStatus.CREATED);
    }
}
