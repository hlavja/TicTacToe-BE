package cz.hlavja.web.rest;

import cz.hlavja.TicTacToeApp;
import cz.hlavja.domain.Move;
import cz.hlavja.repository.MoveRepository;
import cz.hlavja.service.MoveService;
import cz.hlavja.service.dto.MoveDTO;
import cz.hlavja.service.mapper.MoveMapper;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MoveResource} REST controller.
 */
@SpringBootTest(classes = TicTacToeApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MoveResourceIT {

    private static final Integer DEFAULT_BOARD_X = 1;
    private static final Integer UPDATED_BOARD_X = 2;

    private static final Integer DEFAULT_BOARD_Y = 1;
    private static final Integer UPDATED_BOARD_Y = 2;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private MoveService moveService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoveMockMvc;

    private Move move;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Move createEntity(EntityManager em) {
        Move move = new Move()
            .boardX(DEFAULT_BOARD_X)
            .boardY(DEFAULT_BOARD_Y)
            .created(DEFAULT_CREATED);
        return move;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Move createUpdatedEntity(EntityManager em) {
        Move move = new Move()
            .boardX(UPDATED_BOARD_X)
            .boardY(UPDATED_BOARD_Y)
            .created(UPDATED_CREATED);
        return move;
    }

    @BeforeEach
    public void initTest() {
        move = createEntity(em);
    }

    @Test
    @Transactional
    public void createMove() throws Exception {
        int databaseSizeBeforeCreate = moveRepository.findAll().size();
        // Create the Move
        MoveDTO moveDTO = moveMapper.toDto(move);
        restMoveMockMvc.perform(post("/api/moves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
            .andExpect(status().isCreated());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeCreate + 1);
        Move testMove = moveList.get(moveList.size() - 1);
        assertThat(testMove.getBoardX()).isEqualTo(DEFAULT_BOARD_X);
        assertThat(testMove.getBoardY()).isEqualTo(DEFAULT_BOARD_Y);
        assertThat(testMove.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    public void createMoveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moveRepository.findAll().size();

        // Create the Move with an existing ID
        move.setId(1L);
        MoveDTO moveDTO = moveMapper.toDto(move);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoveMockMvc.perform(post("/api/moves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMoves() throws Exception {
        // Initialize the database
        moveRepository.saveAndFlush(move);

        // Get all the moveList
        restMoveMockMvc.perform(get("/api/moves?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(move.getId().intValue())))
            .andExpect(jsonPath("$.[*].boardX").value(hasItem(DEFAULT_BOARD_X)))
            .andExpect(jsonPath("$.[*].boardY").value(hasItem(DEFAULT_BOARD_Y)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getMove() throws Exception {
        // Initialize the database
        moveRepository.saveAndFlush(move);

        // Get the move
        restMoveMockMvc.perform(get("/api/moves/{id}", move.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(move.getId().intValue()))
            .andExpect(jsonPath("$.boardX").value(DEFAULT_BOARD_X))
            .andExpect(jsonPath("$.boardY").value(DEFAULT_BOARD_Y))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMove() throws Exception {
        // Get the move
        restMoveMockMvc.perform(get("/api/moves/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMove() throws Exception {
        // Initialize the database
        moveRepository.saveAndFlush(move);

        int databaseSizeBeforeUpdate = moveRepository.findAll().size();

        // Update the move
        Move updatedMove = moveRepository.findById(move.getId()).get();
        // Disconnect from session so that the updates on updatedMove are not directly saved in db
        em.detach(updatedMove);
        updatedMove
            .boardX(UPDATED_BOARD_X)
            .boardY(UPDATED_BOARD_Y)
            .created(UPDATED_CREATED);
        MoveDTO moveDTO = moveMapper.toDto(updatedMove);

        restMoveMockMvc.perform(put("/api/moves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
            .andExpect(status().isOk());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeUpdate);
        Move testMove = moveList.get(moveList.size() - 1);
        assertThat(testMove.getBoardX()).isEqualTo(UPDATED_BOARD_X);
        assertThat(testMove.getBoardY()).isEqualTo(UPDATED_BOARD_Y);
        assertThat(testMove.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingMove() throws Exception {
        int databaseSizeBeforeUpdate = moveRepository.findAll().size();

        // Create the Move
        MoveDTO moveDTO = moveMapper.toDto(move);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoveMockMvc.perform(put("/api/moves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Move in the database
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMove() throws Exception {
        // Initialize the database
        moveRepository.saveAndFlush(move);

        int databaseSizeBeforeDelete = moveRepository.findAll().size();

        // Delete the move
        restMoveMockMvc.perform(delete("/api/moves/{id}", move.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Move> moveList = moveRepository.findAll();
        assertThat(moveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
