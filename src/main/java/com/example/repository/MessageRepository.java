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

    @Query("select u from Message u where u.messageId = ?1")
    Optional<Message> getMessageByID(@Param("messageId") int messageId);

    @Modifying
    @Transactional
    @Query("delete from Message u where u.messageId = ?1")
    int deleteMessage(int messageId);

    @Modifying
    @Transactional
    @Query("update Message u set u.messageText = ?1 where u.messageId = ?2")
    int updateMessage(String messageText, int messageId);

    @Query("select u from Message u where u.postedBy = ?1")
    List<Message> getAllMessagesByUser(int intAccountId);

}
