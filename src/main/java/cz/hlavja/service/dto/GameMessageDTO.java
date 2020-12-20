package cz.hlavja.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameMessageDTO {

    private String senderLogin;

    private String messageType;

    private String opponentLogin;
}
