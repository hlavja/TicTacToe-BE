package cz.hlavja.service;

import cz.hlavja.service.dto.MoveDTO;

import java.util.List;

/**
 * Service Interface for managing {@link cz.hlavja.domain.Move}.
 */
public interface MoveService {

    /**
     * Save a move.
     *
     * @param moveDTO the entity to save.
     * @return the persisted entity.
     */
    MoveDTO save(MoveDTO moveDTO);

    /**
     * Delete the "id" move.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<MoveDTO> findMovesInGame(Long id);
}
