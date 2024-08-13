package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.entity.Message;
import com.example.service.MessageService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    
    @PostMapping("/register")
    public ResponseEntity postRegister(@RequestBody Account account){
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null){ 
            if (addedAccount.getUsername() == "duplicate"){ //Username already exists, Failure. //Janky, find a better way?
                return ResponseEntity.status(409).body(null);
            }
            else{ //Sucess
                return ResponseEntity.status(200).body(addedAccount);
            }
        }
        else{ //Failure of any other reason
            return ResponseEntity.status(400).body(null);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity validateLogin(@RequestBody Account account){
        Account extantAccount = accountService.validateLogin(account);
        if(extantAccount != null){ // Account exists
            return ResponseEntity.status(200).body(extantAccount);
        }else{ //Account does not exist
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("messages")
    public ResponseEntity postMessage(@RequestBody Message message){
        Message postedMessage = messageService.addMessage(message);
        if(postedMessage != null){ //Posted Successfully
            return ResponseEntity.status(200).body(postedMessage);
        }else{ //Post Failed
            return ResponseEntity.status(400).body(null);
        }
    }
    
    @GetMapping("/messages")
    public ResponseEntity getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);   
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable int messageId){
        Message message =  messageService.getMessageByID(messageId);
        return ResponseEntity.status(200).body(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity DeleteMessageById(@PathVariable int messageId){
        int affectedRows =  messageService.deleteMessageByID(messageId);
        if (affectedRows > 0){ //Successful Delete
            return ResponseEntity.status(200).body(affectedRows);
        }
        else{ //Nothing deleted
            return ResponseEntity.status(200).body(null);
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity patchMessagesById(@PathVariable int messageId, @RequestBody Message message){
        String message_text = message.getMessageText();
        int affectedRows = messageService.patchMessageByID(messageId, message_text);
        if (affectedRows > 0){ //Successful Delete
            return ResponseEntity.status(200).body(affectedRows);
        }
        else{ //Nothing deleted
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesById(@PathVariable int accountId){
        List<Message> messages = messageService.getAllMessagesByAccountID(accountId);
        return ResponseEntity.status(200).body(messages);
    }
}

