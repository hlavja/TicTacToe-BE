package cz.hlavja.service.impl;

import cz.hlavja.service.MoveService;
import cz.hlavja.domain.Move;
import cz.hlavja.repository.MoveRepository;
import cz.hlavja.service.dto.MoveDTO;
import cz.hlavja.service.mapper.MoveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Move}.
 */
@Service
@Transactional
public class MoveServiceImpl implements MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveServiceImpl.class);

    private final MoveRepository moveRepository;

    private final MoveMapper moveMapper;

    public MoveServiceImpl(MoveRepository moveRepository, MoveMapper moveMapper) {
        this.moveRepository = moveRepository;
        this.moveMapper = moveMapper;
    }

    @Override
    public MoveDTO save(MoveDTO moveDTO) {
        log.debug("Request to save Move : {}", moveDTO);
        Move move = moveMapper.toEntity(moveDTO);
        move = moveRepository.save(move);
        return moveMapper.toDto(move);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MoveDTO> findAll() {
        log.debug("Request to get all Moves");
        return moveRepository.findAll().stream()
            .map(moveMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MoveDTO> findOne(Long id) {
        log.debug("Request to get Move : {}", id);
        return moveRepository.findById(id)
            .map(moveMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Move : {}", id);
        moveRepository.deleteById(id);
    }
}
