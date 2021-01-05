package cz.hlavja.web.websocket;

import cz.hlavja.config.Constants;
import cz.hlavja.service.UserService;
import cz.hlavja.service.dto.MessageDTO;
import cz.hlavja.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class UserSpecific {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;

    public UserSpecific(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/friends/ask-friend")
    public ResponseEntity<Void> askFriend(@RequestParam() String friendLogin){
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null){
            MessageDTO message = new MessageDTO();
            message.setMessageType(Constants.ADD_FRIEND);
            message.setSenderLogin(loggedUser.getLogin());
            message.setOpponentLogin(friendLogin);
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/games/cancel-challenge")
    public ResponseEntity<Void> cancelChallenge(@RequestParam() String opponentLogin){
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null){
            MessageDTO message = new MessageDTO();
            message.setMessageType(Constants.CHALLENGE_CANCELLED);
            message.setSenderLogin(loggedUser.getLogin());
            message.setOpponentLogin(opponentLogin);
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
