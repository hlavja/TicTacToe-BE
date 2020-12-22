package cz.hlavja.service.dto;



/**
 * A DTO representing an message.
 */
public class MessageDTO {

    public MessageDTO() {
        // Empty constructor needed for Jackson.
    }

    private String senderLogin;

    private String messageType;

    private String opponentLogin;

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getOpponentLogin() {
        return opponentLogin;
    }

    public void setOpponentLogin(String opponentLogin) {
        this.opponentLogin = opponentLogin;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "senderLogin='" + senderLogin + '\'' +
            ", opponentLogin='" + opponentLogin + '\'' +
            ", messageType='" + messageType + '\'' +
            "}";
    }
}
