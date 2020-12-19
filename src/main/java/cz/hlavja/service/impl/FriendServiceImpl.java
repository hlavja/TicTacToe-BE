package cz.hlavja.service.impl;

import cz.hlavja.service.FriendService;
import cz.hlavja.domain.Friend;
import cz.hlavja.repository.FriendRepository;
import cz.hlavja.service.dto.FriendDTO;
import cz.hlavja.service.mapper.FriendMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Friend}.
 */
@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    private final Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

    private final FriendRepository friendRepository;

    private final FriendMapper friendMapper;

    public FriendServiceImpl(FriendRepository friendRepository, FriendMapper friendMapper) {
        this.friendRepository = friendRepository;
        this.friendMapper = friendMapper;
    }

    @Override
    public FriendDTO save(FriendDTO friendDTO) {
        log.debug("Request to save Friend : {}", friendDTO);
        Friend friend = friendMapper.toEntity(friendDTO);
        friend = friendRepository.save(friend);
        return friendMapper.toDto(friend);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendDTO> findAll() {
        log.debug("Request to get all Friends");
        return friendRepository.findAll().stream()
            .map(friendMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendDTO> findByUserIdOrFriendWithId(Long userId) {
        log.debug("Request to get all Friends");
        return friendRepository.findByUserIdOrFriendWithId(userId, userId).stream()
            .map(friendMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FriendDTO> findOne(Long id) {
        log.debug("Request to get Friend : {}", id);
        return friendRepository.findById(id)
            .map(friendMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Friend : {}", id);
        friendRepository.deleteById(id);
    }
}
