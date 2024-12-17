package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.entity.Account;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /*
     * 
     */
    public Message persistMessage(Message message){
        int postedby = message.getPostedBy();
        Optional<Account> accountOfPoster = accountRepository.getAccountByID(postedby);
        if(accountOfPoster.isPresent()){
            if( !(message.getMessageText().equals("")) && 
            (message.getMessageText().length() <= 255 ) )
            {
                return messageRepository.save(message);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageByMID(String messageid){
        int intMessageId = Integer.valueOf(messageid);
        Optional<Message> optionalMessage = messageRepository.getMessageByID(intMessageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }else{
            return null;
        }
    }

    public int deleteMessage(String messageId){
        int intMessageId = Integer.valueOf(messageId);
        return messageRepository.deleteMessage(intMessageId);
    }

    public int updateMessage(String newMessageText, String messageId) {
        int intMessageId = Integer.valueOf(messageId);
        if( !(newMessageText.equals("")) && 
            newMessageText.length() <= 255)
        {
            return messageRepository.updateMessage(newMessageText, intMessageId);
        }else{
            return 0;
        }
        
    }

    public List<Message> getAllMessagesByUser(String accountId) {
        int intAccountId = Integer.valueOf(accountId);
        return messageRepository.getAllMessagesByUser(intAccountId);
    }
}
