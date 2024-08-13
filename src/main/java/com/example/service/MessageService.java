package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
@Transactional
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message addMessage(Message message){
        String messageText = message.getMessageText();
        int postedBy = message.getPostedBy();
        Optional<Account> optionalAccount = accountRepository.findById(postedBy);
        // messageText cant be blank, or be over 255 characters. postedBy must refer to an existing user. 
        if (messageText != "" && messageText.length() <= 255 && optionalAccount.isPresent()){
            return messageRepository.save(message);
        }
        else{
            return null;
        }
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageByID(int messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()){
            return optionalMessage.get();
        }
        else{
            return null;
        }
    }

    public int deleteMessageByID(int messageId){
        Message extantMessage = getMessageByID(messageId);
        if (extantMessage != null){
            messageRepository.deleteById(messageId);
            return 1;
        }
        else{
            return 0;
        }        
    }

    public int patchMessageByID(int messageId, String message_text) {  
        Message extantMessage = getMessageByID(messageId);
        // A message with messageId must exist, the new message_text cant be blank or be over 255 characters.
        if (extantMessage != null && message_text != "" && message_text.length() <= 255){
            extantMessage.setMessageText(message_text);
            messageRepository.save(extantMessage);
            return 1;
        }
        else{
            return 0;
        }    
    }

    public List<Message> getAllMessagesByAccountID(int accountId) {
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
