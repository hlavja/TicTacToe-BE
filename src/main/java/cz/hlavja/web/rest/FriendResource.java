package cz.hlavja.web.rest;

import cz.hlavja.domain.Friend;
import cz.hlavja.service.FriendService;
import cz.hlavja.service.UserService;
import cz.hlavja.service.dto.UserDTO;
import cz.hlavja.web.rest.errors.BadRequestAlertException;
import cz.hlavja.service.dto.FriendDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cz.hlavja.domain.Friend}.
 */
@RestController
@RequestMapping("/api")
public class FriendResource {

    private final Logger log = LoggerFactory.getLogger(FriendResource.class);

    private static final String ENTITY_NAME = "friend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FriendService friendService;

    public FriendResource(FriendService friendService, UserService userService) {
        this.friendService = friendService;
    }

    /**
     * {@code POST  /friends} : Create a new friend.
     *
     * @param friendDTO the friendDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new friendDTO, or with status {@code 400 (Bad Request)} if the friend has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/friends")
    public ResponseEntity<FriendDTO> createFriend(@RequestBody FriendDTO friendDTO) throws URISyntaxException {
        log.debug("REST request to save Friend : {}", friendDTO);
        if (friendDTO.getId() != null) {
            throw new BadRequestAlertException("A new friend cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FriendDTO result = friendService.save(friendDTO);
        return ResponseEntity.created(new URI("/api/friends/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /friends} : Updates an existing friend.
     *
     * @param friendDTO the friendDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friendDTO,
     * or with status {@code 400 (Bad Request)} if the friendDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the friendDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/friends")
    public ResponseEntity<FriendDTO> updateFriend(@RequestBody FriendDTO friendDTO) throws URISyntaxException {
        log.debug("REST request to update Friend : {}", friendDTO);
        if (friendDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FriendDTO result = friendService.save(friendDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, friendDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /friends} : get all the friends.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of friends in body.
     */
    @GetMapping("/friends")
    public List<FriendDTO> getAllFriends() {
        log.debug("REST request to get all Friends");
        return friendService.findAll();
    }

    /**
     * {@code GET  /friends/:id} : get the "id" friend.
     *
     * @param id the id of the friendDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the friendDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/friends/{id}")
    public ResponseEntity<FriendDTO> getFriend(@PathVariable Long id) {
        log.debug("REST request to get Friend : {}", id);
        Optional<FriendDTO> friendDTO = friendService.findOne(id);
        return ResponseUtil.wrapOrNotFound(friendDTO);
    }

    /**
     * {@code DELETE  /friends/:id} : delete the "id" friend.
     *
     * @param id the id of the friendDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id) {
        log.debug("REST request to delete Friend : {}", id);
        friendService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/friends/remove-friend")
    public ResponseEntity<Void> removeFriend(@RequestParam() Long friendId){
        boolean success = friendService.removeFriend(friendId);
        //TODO mby send websocket message to trigger refresh friends
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
