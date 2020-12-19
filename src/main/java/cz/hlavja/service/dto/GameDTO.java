package cz.hlavja.service.dto;

import io.swagger.annotations.ApiModel;
import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link cz.hlavja.domain.Game} entity.
 */
@ApiModel(description = "The Game entity.\n@author hlavja")
public class GameDTO implements Serializable {
    
    private Long id;

    private Instant created;

    private String gameStatus;

    private String gameType;

    private String result;


    private Long firstPlayerId;

    private Long secondPlayerId;

    private Long winningPlayerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(Long userId) {
        this.firstPlayerId = userId;
    }

    public Long getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(Long userId) {
        this.secondPlayerId = userId;
    }

    public Long getWinningPlayerId() {
        return winningPlayerId;
    }

    public void setWinningPlayerId(Long userId) {
        this.winningPlayerId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameDTO)) {
            return false;
        }

        return id != null && id.equals(((GameDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", gameStatus='" + getGameStatus() + "'" +
            ", gameType='" + getGameType() + "'" +
            ", result='" + getResult() + "'" +
            ", firstPlayerId=" + getFirstPlayerId() +
            ", secondPlayerId=" + getSecondPlayerId() +
            ", winningPlayerId=" + getWinningPlayerId() +
            "}";
    }
}
