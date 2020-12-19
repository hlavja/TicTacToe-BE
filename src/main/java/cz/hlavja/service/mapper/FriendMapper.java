package cz.hlavja.service.mapper;


import cz.hlavja.domain.*;
import cz.hlavja.service.dto.FriendDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Friend} and its DTO {@link FriendDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FriendMapper extends EntityMapper<FriendDTO, Friend> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "friendWith.id", target = "friendWithId")
    FriendDTO toDto(Friend friend);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "friendWithId", target = "friendWith")
    Friend toEntity(FriendDTO friendDTO);

    default Friend fromId(Long id) {
        if (id == null) {
            return null;
        }
        Friend friend = new Friend();
        friend.setId(id);
        return friend;
    }
}
