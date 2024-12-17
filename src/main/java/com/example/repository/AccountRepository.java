package com.example.repository;

import com.example.entity.Account;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Qualifier("accounts")
@Repository
public interface AccountRepository extends JpaRepository <Account, Long>{
    
    /*
     * Queries the database for an account that matches the username passed in the parameter.
     * Returns an Account object.
     */
    @Query("select u from Account u where u.username = ?1")
    Account getAccountByUsername(@Param("usernameVar") String username);

    /*
     * Queries the database for an account that matches the account ID passed in the parameter.
     * Returns an Optional<Account> object.
     */
    @Query("select u from Account u where u.accountId = ?1")
    Optional<Account> getAccountByID(@Param("accountId") int accountId);

}
