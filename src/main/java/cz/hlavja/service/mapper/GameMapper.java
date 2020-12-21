package cz.hlavja.service.mapper;


import cz.hlavja.domain.*;
import cz.hlavja.service.dto.GameDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface GameMapper extends EntityMapper<GameDTO, Game> {

    @Mapping(source = "firstPlayer.id", target = "firstPlayerId")
    @Mapping(source = "secondPlayer.id", target = "secondPlayerId")
    @Mapping(source = "winningPlayer.id", target = "winningPlayerId")
    @Mapping(source = "firstPlayer.login", target = "firstPlayerLogin")
    @Mapping(source = "secondPlayer.login", target = "secondPlayerLogin")
    @Mapping(source = "winningPlayer.login", target = "winningPlayerLogin")
    GameDTO toDto(Game game);

    @Mapping(source = "firstPlayerId", target = "firstPlayer")
    @Mapping(source = "secondPlayerId", target = "secondPlayer")
    @Mapping(source = "winningPlayerId", target = "winningPlayer")
    Game toEntity(GameDTO gameDTO);

    default Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
