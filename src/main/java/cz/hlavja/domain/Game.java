package cz.hlavja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * The Game entity.\n@author hlavja
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created")
    private Instant created;

    @Column(name = "game_status")
    private String gameStatus;

    @Column(name = "game_type")
    private String gameType;

    @Column(name = "result")
    private String result;

    @ManyToOne
    @JsonIgnoreProperties(value = "games", allowSetters = true)
    private User firstPlayer;

    @ManyToOne
    @JsonIgnoreProperties(value = "games", allowSetters = true)
    private User secondPlayer;

    @ManyToOne
    @JsonIgnoreProperties(value = "games", allowSetters = true)
    private User winningPlayer;

    @ManyToOne
    @JsonIgnoreProperties(value = "games", allowSetters = true)
    private User turnUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public Game created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public Game gameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameType() {
        return gameType;
    }

    public Game gameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getResult() {
        return result;
    }

    public Game result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public Game firstPlayer(User user) {
        this.firstPlayer = user;
        return this;
    }

    public void setFirstPlayer(User user) {
        this.firstPlayer = user;
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    public Game secondPlayer(User user) {
        this.secondPlayer = user;
        return this;
    }

    public void setSecondPlayer(User user) {
        this.secondPlayer = user;
    }

    public User getWinningPlayer() {
        return winningPlayer;
    }

    public Game winningPlayer(User user) {
        this.winningPlayer = user;
        return this;
    }

    public void setWinningPlayer(User user) {
        this.winningPlayer = user;
    }

    public User getTurnUser() {
        return turnUser;
    }

    public Game turnUser(User user) {
        this.turnUser = user;
        return this;
    }

    public void setTurnUser(User user) {
        this.turnUser = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", gameStatus='" + getGameStatus() + "'" +
            ", gameType='" + getGameType() + "'" +
            ", result='" + getResult() + "'" +
            "}";
    }
}
