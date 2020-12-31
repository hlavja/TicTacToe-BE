package cz.hlavja.service;

import cz.hlavja.service.dto.GameDTO;
import cz.hlavja.service.dto.MessageDTO;
import cz.hlavja.service.dto.MoveDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link cz.hlavja.domain.Game}.
 */
public interface GameService {

    /**
     * Save a game.
     *
     * @param gameDTO the entity to save.
     * @return the persisted entity.
     */
    GameDTO save(GameDTO gameDTO);


    /**
     * Get the "id" game.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GameDTO> findOne(Long id);

    /**
     * Delete the "id" game.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    /**
     * Return all games by game status
     *
     * @param gameStatus status of game
     * @return the list of entities
     */
    List<GameDTO> findByGameStatus(String gameStatus);

    /**
     * Challenge player to game
     *
     * @param opponentLogin login of challenged player
     * @return message to send to opponent
     */
    MessageDTO challengeGame(String opponentLogin);

    /**
     * Accept challenged game.
     *
     * @param opponentLogin login of accepted player invitation
     * @return message to send to opponent
     */
    MessageDTO acceptGame(String opponentLogin);

    /**
     *  Create new game.
     *
     * @param opponentLogin login of opponent
     * @return prepared game in DB
     */
    GameDTO prepareGame(String opponentLogin);

    /**
     * Reject game invitation.
     *
     * @param opponentLogin login of oponent
     * @return message to sent to opponent
     */
    MessageDTO rejectGame(String opponentLogin);

    /**
     * Create new move to game.
     *
     * @param id of game
     * @param move newly placed move
     * @return message with game state
     */
    MessageDTO move(Long id, MoveDTO move);

    /**
     * Give up in game.
     *
     * @param id of game
     * @return message for opponent
     */
    MessageDTO giveUp(Long id);
}
