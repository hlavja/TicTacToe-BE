package cz.hlavja.service.dto;

import io.swagger.annotations.ApiModel;
import java.time.Instant;
import java.io.Serializable;

/**
 * A DTO for the {@link cz.hlavja.domain.Move} entity.
 */
@ApiModel(description = "The Move entity.\n@author hlavja")
public class MoveDTO implements Serializable {
    
    private Long id;

    private Integer boardX;

    private Integer boardY;

    private Instant created;


    private Long playerId;

    private Long gameId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBoardX() {
        return boardX;
    }

    public void setBoardX(Integer boardX) {
        this.boardX = boardX;
    }

    public Integer getBoardY() {
        return boardY;
    }

    public void setBoardY(Integer boardY) {
        this.boardY = boardY;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long userId) {
        this.playerId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoveDTO)) {
            return false;
        }

        return id != null && id.equals(((MoveDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoveDTO{" +
            "id=" + getId() +
            ", boardX=" + getBoardX() +
            ", boardY=" + getBoardY() +
            ", created='" + getCreated() + "'" +
            ", playerId=" + getPlayerId() +
            ", gameId=" + getGameId() +
            "}";
    }
}
