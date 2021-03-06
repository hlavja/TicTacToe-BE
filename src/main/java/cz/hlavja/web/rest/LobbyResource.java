package cz.hlavja.web.rest;

import cz.hlavja.config.Constants;
import cz.hlavja.domain.User;
import cz.hlavja.repository.UserRepository;
import cz.hlavja.service.FriendService;
import cz.hlavja.service.GameService;
import cz.hlavja.service.dto.FriendDTO;
import cz.hlavja.service.dto.GameDTO;
import cz.hlavja.service.dto.PlayerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LobbyResource {

    private final SimpUserRegistry simpUserRegistry;
    private final UserRepository userRepository;
    private final GameService gameService;
    private final FriendService friendService;

    /**
     * Return list of players in relation to caller player
     *
     * @param userId id of logged player
     * @return list with information about all users
     */
    @GetMapping("/get-players")
    public List<PlayerDTO> getOnlineUsers(@RequestParam Long userId) {
        List<PlayerDTO> players = new ArrayList<>();
        List<String> onlineUsers = this.simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toList());
        List<FriendDTO> userFriends = this.friendService.findByUserIdOrFriendWithId(userId);
        List<GameDTO> runningGames = this.gameService.findByGameStatus(Constants.RUNNING_GAME);
        List<User> users = userRepository.findAll();

        for (User user: users) {
            // remove caller user
            if (!user.getId().equals(userId)){
                PlayerDTO player = new PlayerDTO();
                player.setLogin(user.getLogin());
                player.setPlayerId(user.getId());
                // check if online

                if (onlineUsers.stream().anyMatch(onlineUser -> user.getLogin().equals(onlineUser))){
                    player.setOnline(true);
                }

                if (player.isOnline()){
                    // check if is in game
                    if (runningGames.stream().anyMatch(game -> game.getFirstPlayerId().equals(user.getId()) || game.getSecondPlayerId().equals(user.getId()))){
                        player.setAvailable(false);
                    }
                }
                // check if is friend
                if(userFriends.stream().anyMatch(friend -> friend.getFriendWithId().equals(user.getId()) || friend.getUserId().equals(user.getId()))){
                    player.setFriend(true);
                }
                players.add(player);
            }
        }
        return players;
    }
}
