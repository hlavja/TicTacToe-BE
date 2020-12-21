package cz.hlavja.repository;

import cz.hlavja.domain.Friend;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Friend entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select friend from Friend friend where friend.user.login = ?#{principal.username}")
    List<Friend> findByUserIsCurrentUser();

    @Query("select friend from Friend friend where friend.friendWith.login = ?#{principal.username}")
    List<Friend> findByFriendWithIsCurrentUser();

    List<Friend> findByUserIdOrFriendWithId(Long userId, Long userId2);

    @Modifying
    @Query("delete from Friend friend where friend.user.id in (?1, ?2) and friend.friendWith.id in (?1, ?2)")
    void deleteFriend(Long id, Long friendId);
}
