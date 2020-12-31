package cz.hlavja.web.rest;

import cz.hlavja.config.Constants;
import cz.hlavja.service.GameService;
import cz.hlavja.service.dto.MessageDTO;
import cz.hlavja.service.dto.MoveDTO;
import cz.hlavja.service.dto.GameDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cz.hlavja.domain.Game}.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;

    public GameResource(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    /**
     * {@code GET  /games} : get all the games.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of games in body.
     */
    @GetMapping("/games")
    public List<GameDTO> getAllGames() {
        log.debug("REST request to get all Games");
        return gameService.findByGameStatus(Constants.ENDED_GAME);
    }

    /**
     * {@code GET  /games/:id} : get the "id" game.
     *
     * @param id the id of the gameDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Optional<GameDTO> gameDTO = gameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameDTO);
    }

    /**
     * {@code GET  /games/challenge} : challenge opponent to the game
     *
     * @param opponentLogin login of opponent to challenge
     * @return status code
     */
    @GetMapping("/games/challenge")
    public ResponseEntity<Void> challengeGame(@RequestParam() String opponentLogin) {
        MessageDTO message =  gameService.challengeGame(opponentLogin);
        if (message != null){
            simpMessagingTemplate.convertAndSendToUser(opponentLogin, "/secured/user/queue/specific-user", message);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * {@code GET  /games/accept} : accept game challenge
     *
     * @param opponentLogin login of opponent in accepted game
     * @return status code
     */
    @GetMapping("/games/accept")
    public ResponseEntity<GameDTO> acceptGame(@RequestParam() String opponentLogin) throws URISyntaxException {
        MessageDTO message = gameService.acceptGame(opponentLogin);
        GameDTO preparedGame = gameService.prepareGame(opponentLogin);
        preparedGame = gameService.findOne(preparedGame.getId()).orElse(null);
        if (message != null && preparedGame != null){
            message.setGame(preparedGame);
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return ResponseEntity.created(new URI("/api/games/" + preparedGame.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, preparedGame.getId().toString()))
                .body(preparedGame);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * {@code GET  /games/reject} : reject game challenge
     *
     * @param opponentLogin login of opponent in rejected game
     * @return message
     */
    @GetMapping("/games/reject")
    public ResponseEntity<MessageDTO> rejectGame(@RequestParam() String opponentLogin){
        MessageDTO message = gameService.rejectGame(opponentLogin);
        if (message != null){
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return ResponseEntity.ok().body(message);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * {@code POST  /games/{id}/move} : Create a new move to the game.
     *
     * @param id of game
     * @param move placed move
     * @return message with updated game status
     */
    @PostMapping("games/{id}/move")
    public ResponseEntity<MessageDTO> addMove(@PathVariable Long id, @RequestBody MoveDTO move) {
        MessageDTO message = gameService.move(id, move);
        if (message != null){
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return ResponseEntity.ok().body(message);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  {@code POST  /games/{id}/give-up} : Give up game.
     *
     * @param id of game
     * @return HttpStatus
     */
    @PostMapping("games/{id}/give-up")
    public ResponseEntity<Void> giveUp(@PathVariable Long id){
        MessageDTO message = gameService.giveUp(id);
        if (message != null){
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
