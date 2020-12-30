package cz.hlavja.repository;

import cz.hlavja.domain.Move;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Move entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    @Query("select move from Move move where move.player.login = ?#{principal.username}")
    List<Move> findByPlayerIsCurrentUser();

    List<Move> findByGameId(Long id);
}
