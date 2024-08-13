package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
@Transactional
public class AccountService {

    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account){
        String username = account.getUsername();
        String password = account.getPassword();
        Account extantAccount = accountRepository.findAccountByUsername(username);

        // Cant have a blank username, and must have a pasword longer than 4 characters. Username must not already exist.
        if (username != "" && password.length() >= 4){ 
            if (extantAccount == null){
                return accountRepository.save(account);
            }
            else{
                return new Account("duplicate","");
            }
        }
        else{
            return null;
        }
        // DO: Exception handling to return different results.
    }

    public Account validateLogin(Account account){
        String username = account.getUsername();
        String password = account.getPassword();
        return accountRepository.findAccountByUsernameAndPassword(username, password);
    }
}
