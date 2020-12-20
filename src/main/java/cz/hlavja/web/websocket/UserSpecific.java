package cz.hlavja.web.websocket;

import cz.hlavja.service.dto.GameMessageDTO;
import cz.hlavja.web.websocket.dto.ActivityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class UserSpecific {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /*@MessageMapping("/secured/game")
    public void sendSpecific(@Payload ActivityDTO activityDTO, Principal user, @Header("simpSessionId") String sessionId) throws Exception {
        OutputMessage out = new OutputMessage(
            msg.getFrom(),
            msg.getText(),
            new SimpleDateFormat("HH:mm").format(new Date()));
        simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/secured/user/queue/specific-user", out);
    }*/

    @MessageMapping("/secured/player-action")
    public void startGame(@Payload GameMessageDTO gameMessage, Principal user,  @Header("simpSessionId") String sessionId){
        GameMessageDTO newMessage = new GameMessageDTO();
        newMessage.setMessageType("CREATE_GAME");
        newMessage.setSenderLogin(gameMessage.getSenderLogin());
        newMessage.setOpponentLogin(gameMessage.getOpponentLogin());
        simpMessagingTemplate.convertAndSendToUser(gameMessage.getOpponentLogin(), "/secured/user/queue/specific-user", newMessage);
    }

    @GetMapping("/aa")
    public void startGame2(@Payload GameMessageDTO gameMessage, Principal user,  @Header("simpSessionId") String sessionId){
        GameMessageDTO newMessage = new GameMessageDTO();
        newMessage.setMessageType("CREATE_GAME");
        newMessage.setSenderLogin(gameMessage.getSenderLogin());
        newMessage.setOpponentLogin(gameMessage.getOpponentLogin());
        simpMessagingTemplate.convertAndSendToUser("user", "/secured/user/queue/specific-user", newMessage);
    }
}
