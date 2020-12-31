package cz.hlavja.web.rest;

import cz.hlavja.service.FriendService;
import cz.hlavja.service.dto.FriendDTO;

import io.github.jhipster.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link cz.hlavja.domain.Friend}.
 */
@RestController
@RequestMapping("/api")
public class FriendResource {

    private static final String ENTITY_NAME = "friend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FriendService friendService;

    public FriendResource(FriendService friendService) {
        this.friendService = friendService;
    }


    @DeleteMapping("/friends/remove-friend")
    public ResponseEntity<Void> removeFriend(@RequestParam() Long friendId){
        boolean success = friendService.removeFriend(friendId);
        if (success){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/friends/add-friend")
    public ResponseEntity<FriendDTO> addFriend(@RequestParam() String addFriendLogin){
        FriendDTO result = friendService.addFriend(addFriendLogin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
