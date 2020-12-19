package cz.hlavja.web.rest;

import cz.hlavja.TicTacToeApp;
import cz.hlavja.domain.Friend;
import cz.hlavja.repository.FriendRepository;
import cz.hlavja.service.FriendService;
import cz.hlavja.service.dto.FriendDTO;
import cz.hlavja.service.mapper.FriendMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FriendResource} REST controller.
 */
@SpringBootTest(classes = TicTacToeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FriendResourceIT {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private FriendService friendService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFriendMockMvc;

    private Friend friend;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friend createEntity(EntityManager em) {
        Friend friend = new Friend();
        return friend;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Friend createUpdatedEntity(EntityManager em) {
        Friend friend = new Friend();
        return friend;
    }

    @BeforeEach
    public void initTest() {
        friend = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriend() throws Exception {
        int databaseSizeBeforeCreate = friendRepository.findAll().size();
        // Create the Friend
        FriendDTO friendDTO = friendMapper.toDto(friend);
        restFriendMockMvc.perform(post("/api/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(friendDTO)))
            .andExpect(status().isCreated());

        // Validate the Friend in the database
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList).hasSize(databaseSizeBeforeCreate + 1);
        Friend testFriend = friendList.get(friendList.size() - 1);
    }

    @Test
    @Transactional
    public void createFriendWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = friendRepository.findAll().size();

        // Create the Friend with an existing ID
        friend.setId(1L);
        FriendDTO friendDTO = friendMapper.toDto(friend);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendMockMvc.perform(post("/api/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(friendDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Friend in the database
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFriends() throws Exception {
        // Initialize the database
        friendRepository.saveAndFlush(friend);

        // Get all the friendList
        restFriendMockMvc.perform(get("/api/friends?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friend.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getFriend() throws Exception {
        // Initialize the database
        friendRepository.saveAndFlush(friend);

        // Get the friend
        restFriendMockMvc.perform(get("/api/friends/{id}", friend.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(friend.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFriend() throws Exception {
        // Get the friend
        restFriendMockMvc.perform(get("/api/friends/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriend() throws Exception {
        // Initialize the database
        friendRepository.saveAndFlush(friend);

        int databaseSizeBeforeUpdate = friendRepository.findAll().size();

        // Update the friend
        Friend updatedFriend = friendRepository.findById(friend.getId()).get();
        // Disconnect from session so that the updates on updatedFriend are not directly saved in db
        em.detach(updatedFriend);
        FriendDTO friendDTO = friendMapper.toDto(updatedFriend);

        restFriendMockMvc.perform(put("/api/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(friendDTO)))
            .andExpect(status().isOk());

        // Validate the Friend in the database
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList).hasSize(databaseSizeBeforeUpdate);
        Friend testFriend = friendList.get(friendList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFriend() throws Exception {
        int databaseSizeBeforeUpdate = friendRepository.findAll().size();

        // Create the Friend
        FriendDTO friendDTO = friendMapper.toDto(friend);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendMockMvc.perform(put("/api/friends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(friendDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Friend in the database
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFriend() throws Exception {
        // Initialize the database
        friendRepository.saveAndFlush(friend);

        int databaseSizeBeforeDelete = friendRepository.findAll().size();

        // Delete the friend
        restFriendMockMvc.perform(delete("/api/friends/{id}", friend.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
