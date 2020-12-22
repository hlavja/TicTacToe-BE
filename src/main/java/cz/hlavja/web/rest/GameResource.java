package cz.hlavja.web.rest;

import cz.hlavja.config.Constants;
import cz.hlavja.service.GameService;
import cz.hlavja.service.dto.MessageDTO;
import cz.hlavja.web.rest.errors.BadRequestAlertException;
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
     * {@code POST  /games} : Create a new game.
     *
     * @param gameDTO the gameDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameDTO, or with status {@code 400 (Bad Request)} if the game has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games")
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO gameDTO) throws URISyntaxException {
        log.debug("REST request to save Game : {}", gameDTO);
        if (gameDTO.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity.created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /games} : Updates an existing game.
     *
     * @param gameDTO the gameDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameDTO,
     * or with status {@code 400 (Bad Request)} if the gameDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameDTO couldn't be updated.
     */
    @PutMapping("/games")
    public ResponseEntity<GameDTO> updateGame(@RequestBody GameDTO gameDTO) {
        log.debug("REST request to update Game : {}", gameDTO);
        if (gameDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GameDTO result = gameService.save(gameDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameDTO.getId().toString()))
            .body(result);
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
     * {@code DELETE  /games/:id} : delete the "id" game.
     *
     * @param id the id of the gameDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
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
    public ResponseEntity<MessageDTO> acceptGame(@RequestParam() String opponentLogin) {
        MessageDTO message = gameService.acceptGame(opponentLogin);
        if (message != null){
            simpMessagingTemplate.convertAndSendToUser(message.getOpponentLogin(), "/secured/user/queue/specific-user", message);
            return ResponseEntity.ok().body(message);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
