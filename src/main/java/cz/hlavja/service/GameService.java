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
     * Get all the games.
     *
     * @return the list of entities.
     */
    List<GameDTO> findAll();


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
     * @param gameStatus status of game
     * @return the list of entities
     */
    List<GameDTO> findByGameStatus(String gameStatus);

    /**
     *
     * @param opponentLogin login of challenged player
     * @return message to send to opponent
     */
    MessageDTO challengeGame(String opponentLogin);

    /**
     *
     * @param opponentLogin login of accepted player invitaion
     * @return message to send to opponent
     */
    MessageDTO acceptGame(String opponentLogin);

    /**
     *
     * @param opponentLogin login of opponent
     * @return prepared game in DB
     */
    GameDTO prepareGame(String opponentLogin);

    /**
     *
     * @param opponentLogin
     * @return
     */
    MessageDTO rejectGame(String opponentLogin);

    MessageDTO move(Long id, MoveDTO move);
}
