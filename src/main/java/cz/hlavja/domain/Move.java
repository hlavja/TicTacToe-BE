package cz.hlavja.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * The Move entity.\n@author hlavja
 */
@Entity
@Table(name = "move")
public class Move implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "board_x")
    private Integer boardX;

    @Column(name = "board_y")
    private Integer boardY;

    @Column(name = "created")
    private Instant created;

    @ManyToOne
    @JsonIgnoreProperties(value = "moves", allowSetters = true)
    private User player;

    @ManyToOne
    @JsonIgnoreProperties(value = "moves", allowSetters = true)
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBoardX() {
        return boardX;
    }

    public Move boardX(Integer boardX) {
        this.boardX = boardX;
        return this;
    }

    public void setBoardX(Integer boardX) {
        this.boardX = boardX;
    }

    public Integer getBoardY() {
        return boardY;
    }

    public Move boardY(Integer boardY) {
        this.boardY = boardY;
        return this;
    }

    public void setBoardY(Integer boardY) {
        this.boardY = boardY;
    }

    public Instant getCreated() {
        return created;
    }

    public Move created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public User getPlayer() {
        return player;
    }

    public Move player(User user) {
        this.player = user;
        return this;
    }

    public void setPlayer(User user) {
        this.player = user;
    }

    public Game getGame() {
        return game;
    }

    public Move game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Move)) {
            return false;
        }
        return id != null && id.equals(((Move) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Move{" +
            "id=" + getId() +
            ", boardX=" + getBoardX() +
            ", boardY=" + getBoardY() +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
