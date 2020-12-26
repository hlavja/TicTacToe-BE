package cz.hlavja.service.impl;

import cz.hlavja.config.Constants;
import cz.hlavja.domain.User;
import cz.hlavja.repository.UserRepository;
import cz.hlavja.service.GameService;
import cz.hlavja.domain.Game;
import cz.hlavja.repository.GameRepository;
import cz.hlavja.service.UserService;
import cz.hlavja.service.dto.GameDTO;
import cz.hlavja.service.dto.MessageDTO;
import cz.hlavja.service.dto.MoveDTO;
import cz.hlavja.service.dto.UserDTO;
import cz.hlavja.service.mapper.GameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Game}.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper, UserService userService, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public GameDTO save(GameDTO gameDTO) {
        log.debug("Request to save Game : {}", gameDTO);
        Game game = gameMapper.toEntity(gameDTO);
        game = gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameDTO> findAll() {
        log.debug("Request to get all Games");
        return gameRepository.findAll().stream()
            .map(gameMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameDTO> findByGameStatus(String gameStatus) {
        log.debug("Request to get all Games");
        return gameRepository.findByGameStatusOrderByCreatedDesc(gameStatus).stream()
            .map(gameMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<GameDTO> findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        return gameRepository.findById(id)
            .map(gameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.deleteById(id);
    }

    @Override
    public MessageDTO challengeGame(String opponentLogin){
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null){
            MessageDTO message = new MessageDTO();
            message.setMessageType(Constants.GAME_CHALLENGE);
            message.setSenderLogin(loggedUser.getLogin());
            message.setOpponentLogin(opponentLogin);
            return message;
        } else {
            return null;
        }
    }

    @Override
    public MessageDTO acceptGame(String opponentLogin){
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null){
            MessageDTO message = new MessageDTO();
            message.setMessageType(Constants.GAME_ACCEPTED);
            message.setSenderLogin(loggedUser.getLogin());
            message.setOpponentLogin(opponentLogin);
            return message;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public GameDTO prepareGame(String opponentLogin){
        Optional<User> opponent = userRepository.findOneByLogin(opponentLogin);
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null && opponent.isPresent()){
            GameDTO newGame = new GameDTO();
            newGame.setCreated(Instant.now());
            newGame.setFirstPlayerId(opponent.get().getId());
            newGame.setSecondPlayerId(loggedUser.getId());
            newGame.setGameStatus(Constants.RUNNING_GAME);
            newGame.setGameType(Constants.STANDARD_GAME);
            return save(newGame);
        } else {
            return null;
        }
    }

    @Override
    public MessageDTO rejectGame(String opponentLogin){
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null) {
            MessageDTO message = new MessageDTO();
            message.setMessageType(Constants.GAME_REJECTED);
            message.setSenderLogin(loggedUser.getLogin());
            message.setOpponentLogin(opponentLogin);
            return message;
        } else {
            return null;
        }
    }

    @Override
    public MoveDTO move(Long id, MoveDTO move){
        //TODO check if player is in game
        //TODO check if player is on turn
        //TODO check if can place move
        //TODO check state of game (won, tie, next turn)
        return new MoveDTO();
    }
}
