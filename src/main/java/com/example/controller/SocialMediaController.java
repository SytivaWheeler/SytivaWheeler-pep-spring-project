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
    
    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account){
        Account registeredAccount = accountService.persistAccount(account);
        if(registeredAccount != null){
            return ResponseEntity.status(200).body(registeredAccount);
        }else{
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account){
        Account attemptedLogin = accountService.loginAttempt(account);
        return ResponseEntity.status(200).body(attemptedLogin);
    }

    @PostMapping("/messages")
    public ResponseEntity postNewMessage(@RequestBody Message message){
        Message newMessage = messageService.persistMessage(message);
        if(newMessage != null){
            return ResponseEntity.status(200).body(message);
        }else{
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity getAllMessages(){
        List<Message> messageList = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messageList);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageByID(@PathVariable String messageId){
        Message returnedMessage = messageService.getMessageByMID(messageId);
        return ResponseEntity.status(200).body(returnedMessage);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable String messageId){
        int rowsUpdated = messageService.deleteMessage(messageId);
        if(rowsUpdated > 0){
            return ResponseEntity.status(200).body(rowsUpdated); 
        }else{
            return ResponseEntity.status(200).build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessage(@RequestBody Message message, @PathVariable String messageId){
        int rowsUpdated = messageService.updateMessage(message.getMessageText(), messageId);
        
        if(rowsUpdated > 0){
            return ResponseEntity.status(200).body(rowsUpdated);
        }else{
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesByUser(@PathVariable String accountId){
        List<Message> messageList = messageService.getAllMessagesByUser(accountId);
        return ResponseEntity.status(200).body(messageList);
    }

    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity DupeUserException(DuplicateUserException ex){
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity UnauthException(UnauthorizedException ex){
        return ResponseEntity.status(401).build();
    }

}
