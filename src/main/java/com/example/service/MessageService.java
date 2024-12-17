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
     * Adds the message passed in the parameter to the database. If the message text 
     * is longer than 255 characters or is empty, the method returns null. Otherwise, it
     * returns a Message object.
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
    
    /*
     *  Returns a List of all messages in the database.
     */
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    /*
     * Retrieves a message from the database using its message ID.
     * Returns it as a Message object if found. Returns null otherwise.
     */
    public Message getMessageByMID(String messageid){
        int intMessageId = Integer.valueOf(messageid);
        Optional<Message> optionalMessage = messageRepository.getMessageByID(intMessageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }else{
            return null;
        }
    }

    /*
     * Deletes a message from the database using its message id if it exists.
     * Returns the number of rows affected.
     */
    public int deleteMessage(String messageId){
        int intMessageId = Integer.valueOf(messageId);
        return messageRepository.deleteMessage(intMessageId);
    }

    /*
     * Updates the text of a message in the database if it exists. Returns the
     * number of rows affected.
     */
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

    /*
     * Retrieves all the messages made by a specific user in the database using
     * the users account ID. Returns a list of Message objects with said retrieved 
     * messages.
     */
    public List<Message> getAllMessagesByUser(String accountId) {
        int intAccountId = Integer.valueOf(accountId);
        return messageRepository.getAllMessagesByUser(intAccountId);
    }
}
