package cz.hlavja.service.mapper;


import cz.hlavja.domain.*;
import cz.hlavja.service.dto.MoveDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Move} and its DTO {@link MoveDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, GameMapper.class})
public interface MoveMapper extends EntityMapper<MoveDTO, Move> {

    @Mapping(source = "player.id", target = "playerId")
    @Mapping(source = "game.id", target = "gameId")
    MoveDTO toDto(Move move);

    @Mapping(source = "playerId", target = "player")
    @Mapping(source = "gameId", target = "game")
    Move toEntity(MoveDTO moveDTO);

    default Move fromId(Long id) {
        if (id == null) {
            return null;
        }
        Move move = new Move();
        move.setId(id);
        return move;
    }
}
