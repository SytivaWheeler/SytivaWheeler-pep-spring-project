package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Conflict;


import com.example.SocialMediaApp;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.*;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    
    /*
     * Post handler to add a new account to the database. Returns a 200
     * status ResponseEntity with the account added to the database in 
     * the body on success. DupeUserException handler is used if the
     * username conflicts with one currently in the database. Any other
     * issues resulting in the login being unsuccessful return a 400 
     * status ResponseEntity with no body.
     */
    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account){
        Account registeredAccount = accountService.persistAccount(account);
        if(registeredAccount != null){
            return ResponseEntity.status(200).body(registeredAccount);
        }else{
            return ResponseEntity.status(400).build();
        }
    }

    /*
     * Post handler that takes in login info as an Account object and 
     * checks if it matches an account in the database. Returns a 200
     * status ResponseEntity with the retrieved account in the body.
     * If the login fails, the Unauthorized exception handler is used.
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account){
        Account attemptedLogin = accountService.loginAttempt(account);
        return ResponseEntity.status(200).body(attemptedLogin);
    }

    /*
     * Post handler to add new messages to the database. Returns a 200 
     * status ResponseEntity with the new message in the body. Otherwise
     * returns a 400 status ResponseEntity with no body.
     */
    @PostMapping("/messages")
    public ResponseEntity postNewMessage(@RequestBody Message message){
        Message newMessage = messageService.persistMessage(message);
        if(newMessage != null){
            return ResponseEntity.status(200).body(message);
        }else{
            return ResponseEntity.status(400).build();
        }
    }

    /*
     * Get handler to retrieve all message from the database. Returns 
     * a 200 status ResponseEntity with all messages in the database 
     * in the body. Body is empty if there are no messages to be 
     * retrieved.
     */
    @GetMapping("/messages")
    public ResponseEntity getAllMessages(){
        List<Message> messageList = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messageList);
    }

    /*
     * Get handler to retrieve messages by their ID. Always returns a
     * 200 status ResponseEntity with the message in the body. Body is 
     * empty if there was no such message in the database.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageByID(@PathVariable String messageId){
        Message returnedMessage = messageService.getMessageByMID(messageId);
        return ResponseEntity.status(200).body(returnedMessage);
    }

    /*
     * Delete handler to delete messages from the database using their message 
     * id. Returns 200 status ResponseEntity with number of rows affected on 
     * success, 200 status ResponseEntity with no body otherwise.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable String messageId){
        int rowsUpdated = messageService.deleteMessage(messageId);
        if(rowsUpdated > 0){
            return ResponseEntity.status(200).body(rowsUpdated); 
        }else{
            return ResponseEntity.status(200).build();
        }
    }

    /*
     * Patch handler to update messages in the database using their message id.
     * Returns 200 status ResponseEntity with number rows affected in body if update 
     * was successful, 400 status with no body otherwise.
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessage(@RequestBody Message message, @PathVariable String messageId){
        int rowsUpdated = messageService.updateMessage(message.getMessageText(), messageId);
        
        if(rowsUpdated > 0){
            return ResponseEntity.status(200).body(rowsUpdated);
        }else{
            return ResponseEntity.status(400).build();
        }
    }

    /*
     * Get Handler to get all messages made by a specific user. Always returns 200 
     * status ResponseEntity with list of messages in the body. List and Body will 
     * be empty if there are no such messages.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesByUser(@PathVariable String accountId){
        List<Message> messageList = messageService.getAllMessagesByUser(accountId);
        return ResponseEntity.status(200).body(messageList);
    }

    /*
     * Exception handler to deal with duplicate user registration.
     */
    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity DupeUserException(DuplicateUserException ex){
        return ResponseEntity.status(409).build();
    }

    /*
     * Exception handler to deal with invalid login attempts.
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity UnauthException(UnauthorizedException ex){
        return ResponseEntity.status(401).build();
    }

}
