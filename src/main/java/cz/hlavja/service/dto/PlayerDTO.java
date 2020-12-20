package cz.hlavja.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {

    private String login;

    private boolean isOnline = false;

    private boolean isAvailable = true;

    private boolean isFriend = false;
}
