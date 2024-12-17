package com.example.repository;

import com.example.entity.Message;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Qualifier("messages")
@Repository
public interface MessageRepository extends JpaRepository <Message, Long>{

    /*
     * Queries the database for a message using its message ID. Returns an 
     * Optional Message object.
     */
    @Query("select u from Message u where u.messageId = ?1")
    Optional<Message> getMessageByID(@Param("messageId") int messageId);

    /*
     * Deletes a message from teh database using it Message ID. Returns the number
     * of rows affected as an int.
     */
    @Modifying
    @Transactional
    @Query("delete from Message u where u.messageId = ?1")
    int deleteMessage(int messageId);

    /*
     * Updates the message text of a message in the database using its message ID.
     * Returns the number of rows affected as an int.
     */
    @Modifying
    @Transactional
    @Query("update Message u set u.messageText = ?1 where u.messageId = ?2")
    int updateMessage(String messageText, int messageId);

    /*
     * Queries the database for all the messages posted by a specific user.
     * Returns a list of Message objects containing the retrieved messages.
     */
    @Query("select u from Message u where u.postedBy = ?1")
    List<Message> getAllMessagesByUser(int intAccountId);

}
