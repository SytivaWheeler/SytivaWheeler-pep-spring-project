package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUserException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;


@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /*
     * Adds the account passed in the parameter to the database.
     * Throws a DuplicateUserException if an account with the username already exists in the
     * database. Returns an Account object.
     */
    public Account persistAccount(Account account){
        Account checkedAccount = accountRepository.getAccountByUsername(account.getUsername());
        if(checkedAccount == null){
            if(account.getUsername() != "" && account.getPassword().length() >= 4){
                return accountRepository.save(account);
            }else{
                return null;
            }
        }else{
            throw new DuplicateUserException();
        }       
    }

    /*
     * Checks the database for the account passed in the parameter and checks if the user entered
     * the correct password for the account. If the username entered does not match an account and/or
     * the password doesnt match, an UnauthorizedException is thrown.
     */
    public Account loginAttempt(Account account){
        Account attemptedAccount = accountRepository.getAccountByUsername(account.getUsername());
        if(attemptedAccount != null){
            if(attemptedAccount.getUsername().equals(account.getUsername()) && 
            attemptedAccount.getPassword().equals(account.getPassword()))
            {
                return attemptedAccount;
            }else{
                throw new UnauthorizedException();
            }
        }else{
            throw new UnauthorizedException();
        }
    }

}
