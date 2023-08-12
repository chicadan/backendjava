import com.minhub.homebanking.dtos.AccountDTO;
import com.minhub.homebanking.dtos.ClientDTO;
import com.minhub.homebanking.models.Account;
import com.minhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api")
public class AccountController {


    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    private List<AccountDTO> getAccount(){

        List<Account> listClient = accountRepository.findAll();


        return listClient.stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    private AccountDTO getClient(@PathVariable Long id){
        return (accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null));
    }









}