package cz.hlavja.service.impl;

import cz.hlavja.config.Constants;
import cz.hlavja.domain.User;
import cz.hlavja.repository.UserRepository;
import cz.hlavja.service.GameService;
import cz.hlavja.domain.Game;
import cz.hlavja.repository.GameRepository;
import cz.hlavja.service.MoveService;
import cz.hlavja.service.UserService;
import cz.hlavja.service.dto.GameDTO;
import cz.hlavja.service.dto.MessageDTO;
import cz.hlavja.service.dto.MoveDTO;
import cz.hlavja.service.dto.UserDTO;
import cz.hlavja.service.mapper.GameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
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

    private final MoveService moveService;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper, UserService userService, UserRepository userRepository, MoveService moveService) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.moveService = moveService;
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
            Random random = new Random();
            if (random.nextBoolean()){
                newGame.setTurnUserId(newGame.getFirstPlayerId());
            } else {
                newGame.setTurnUserId(newGame.getSecondPlayerId());
            }
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
    @Transactional
    public MessageDTO move(Long id, MoveDTO move){
        // move out of board
        if (move.getBoardY() > Constants.BOARD_SIZE || move.getBoardX() > Constants.BOARD_SIZE){
            return null;
        }
        Optional<GameDTO> game = gameRepository.findById(id).map(gameMapper::toDto);
        //check if game exists
        if (!game.isPresent()){
            return null;
        }
        GameDTO actualGame = game.get();
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser == null) {
            return null;
        }
        // user is in game
        if (actualGame.getFirstPlayerId().equals(loggedUser.getId()) || actualGame.getSecondPlayerId().equals(loggedUser.getId())){
            // user is on turn
            if (actualGame.getTurnUserId().equals(loggedUser.getId())){
                List<MoveDTO> gameMoves = moveService.findMovesInGame(id);
                // check if move can be placed
                if (gameMoves.stream().anyMatch(m -> m.getBoardX().equals(move.getBoardX()) && m.getBoardY().equals(move.getBoardY()))){
                    return null;
                }
                move.setCreated(Instant.now());
                MoveDTO newMove = moveService.save(move);
                gameMoves.add(newMove);
                // check if player won
                checkGameState(gameMoves.stream().filter(m -> m.getPlayerId().equals(loggedUser.getId())).collect(Collectors.toList()), actualGame, newMove, loggedUser);
                // board is full and no one won
                if (gameMoves.size() == (Constants.BOARD_SIZE * Constants.BOARD_SIZE) && actualGame.getGameStatus().equals(Constants.RUNNING_GAME)){
                    actualGame.setGameStatus(Constants.ENDED_GAME);
                    actualGame.setResult(Constants.DRAFT_GAME);
                    save(actualGame);
                }
                return createMessage(loggedUser, actualGame, Constants.ADD_MOVE, newMove);
            }
        }
        return null;
    }

    private void checkGameState(List<MoveDTO> gameMoves, GameDTO actualGame, MoveDTO newMove, UserDTO loggedUser) {
        int[][] board = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for (MoveDTO move: gameMoves) {
            board[move.getBoardY()][move.getBoardX()] = 1;
        }

        //check col
        for(int i = 0; i < Constants.BOARD_SIZE; i++) {
            if(board[newMove.getBoardY()][i] != 1)
                break;
            if(i == 2){
                setGameToWinState(actualGame, loggedUser);
            }
        }
        //check row
        for(int i = 0; i < Constants.BOARD_SIZE; i++) {
            if(board[i][newMove.getBoardX()] != 1)
                break;
            if(i == 2){
                setGameToWinState(actualGame, loggedUser);
            }
        }
        //check diag
        if(newMove.getBoardY().equals(newMove.getBoardX())) {
            //we're on a diagonal
            for(int i = 0; i < Constants.BOARD_SIZE; i++){
                if(board[i][i] != 1)
                    break;
                if(i == 2){
                    setGameToWinState(actualGame, loggedUser);
                }
            }
        }
        //check anti diag
        if(newMove.getBoardY() + newMove.getBoardX() == Constants.BOARD_SIZE - 1) {
            for(int i = 0; i < Constants.BOARD_SIZE; i++){
                if(board[i][(Constants.BOARD_SIZE - 1) - i] != 1)
                    break;
                if(i == 2){
                    setGameToWinState(actualGame, loggedUser);
                }
            }
        }

        if (actualGame.getGameStatus().equals(Constants.RUNNING_GAME)) {
            actualGame = setTurnToNextPlayer(actualGame);
        }
        save(actualGame);
    }

    private void setGameToWinState(GameDTO actualGame, UserDTO loggedUser) {
        actualGame.setGameStatus(Constants.ENDED_GAME);
        actualGame.setResult(Constants.WON_GAME);
        actualGame.setWinningPlayerId(loggedUser.getId());
        actualGame.setWinningPlayerLogin(loggedUser.getLogin());
    }

    private GameDTO setTurnToNextPlayer(GameDTO actualGame) {
        if (actualGame.getTurnUserId().equals(actualGame.getFirstPlayerId())){
            actualGame.setTurnUserId(actualGame.getSecondPlayerId());
        } else {
            actualGame.setTurnUserId(actualGame.getFirstPlayerId());
        }
        return save(actualGame);
    }

    @Override
    @Transactional
    public MessageDTO giveUp(Long gameId) {
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser == null){
            return null;
        }
        Optional<GameDTO> game = gameRepository.findById(gameId).map(gameMapper::toDto);
        //check if game exists
        if (!game.isPresent()){
            return null;
        }
        GameDTO actualGame = game.get();
        actualGame.setGameStatus(Constants.ENDED_GAME);
        actualGame.setResult(Constants.WON_GAME);
        if (actualGame.getFirstPlayerId().equals(loggedUser.getId())){
            actualGame.setWinningPlayerId(actualGame.getSecondPlayerId());
            actualGame.setWinningPlayerLogin(actualGame.getSecondPlayerLogin());
        } else {
            actualGame.setWinningPlayerId(actualGame.getFirstPlayerId());
            actualGame.setWinningPlayerLogin(actualGame.getFirstPlayerLogin());
        }
        save(actualGame);
        return createMessage(loggedUser, actualGame, Constants.GIVE_UP, null);
    }

    private MessageDTO createMessage(UserDTO loggedUser, GameDTO actualGame, String messageType, MoveDTO newMove) {
        MessageDTO message = new MessageDTO();
        message.setMessageType(messageType);
        message.setGame(actualGame);
        message.setSenderLogin(loggedUser.getLogin());
        message.setNewMove(newMove);
        if (actualGame.getFirstPlayerId().equals(loggedUser.getId())){
            message.setOpponentLogin(actualGame.getSecondPlayerLogin());
        } else {
            message.setOpponentLogin(actualGame.getFirstPlayerLogin());
        }
        return message;
    }

}
