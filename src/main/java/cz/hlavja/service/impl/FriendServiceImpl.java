package cz.hlavja.service.impl;

import cz.hlavja.service.FriendService;
import cz.hlavja.domain.Friend;
import cz.hlavja.repository.FriendRepository;
import cz.hlavja.service.UserService;
import cz.hlavja.service.dto.FriendDTO;
import cz.hlavja.service.dto.UserDTO;
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

    private final UserService userService;

    public FriendServiceImpl(FriendRepository friendRepository, FriendMapper friendMapper, UserService userService) {
        this.friendRepository = friendRepository;
        this.friendMapper = friendMapper;
        this.userService = userService;
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

    @Override
    @Transactional
    public boolean removeFriend(Long friendId){
        UserDTO loggedUser = userService.getUserWithAuthorities().map(UserDTO::new).orElse(null);
        if (loggedUser != null){
            friendRepository.deleteFriend(loggedUser.getId(), friendId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public FriendDTO addFriend(String addFriendLogin){
        Optional<UserDTO> loggedUser = userService.getUserWithAuthorities().map(UserDTO::new);
        Optional<UserDTO> newFriend = userService.getUserWithAuthoritiesByLogin(addFriendLogin).map(UserDTO::new);
        if( loggedUser.isPresent() && newFriend.isPresent()){
            FriendDTO newFriendship = new FriendDTO();
            newFriendship.setUserId(newFriend.get().getId());
            newFriendship.setFriendWithId(loggedUser.get().getId());

            Friend friend = friendMapper.toEntity(newFriendship);
            friend = friendRepository.save(friend);
            return friendMapper.toDto(friend);
        } else {
            return null;
        }
    }
}
