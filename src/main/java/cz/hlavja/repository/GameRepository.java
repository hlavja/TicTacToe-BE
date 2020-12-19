package cz.hlavja.repository;

import cz.hlavja.domain.Game;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select game from Game game where game.firstPlayer.login = ?#{principal.username}")
    List<Game> findByFirstPlayerIsCurrentUser();

    @Query("select game from Game game where game.secondPlayer.login = ?#{principal.username}")
    List<Game> findBySecondPlayerIsCurrentUser();

    @Query("select game from Game game where game.winningPlayer.login = ?#{principal.username}")
    List<Game> findByWinningPlayerIsCurrentUser();

    List<Game> findByGameStatus(String gameStatus);
}
