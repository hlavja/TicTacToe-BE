package cz.hlavja.service;

import cz.hlavja.service.dto.FriendDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link cz.hlavja.domain.Friend}.
 */
public interface FriendService {

    /**
     * Save a friend.
     *
     * @param friendDTO the entity to save.
     * @return the persisted entity.
     */
    FriendDTO save(FriendDTO friendDTO);

    /**
     * Delete the "id" friend.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Return list of friends
     * @param userId id of user
     * @return lost of froends for given player
     */
    List<FriendDTO> findByUserIdOrFriendWithId(Long userId);

    boolean removeFriend(Long friendId);

    FriendDTO addFriend(String addFriendLogin);
}
