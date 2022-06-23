package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.*;
import ru.simbir.internship.chat.repository.BlockedUserRepository;
import ru.simbir.internship.chat.repository.UserRepository;
import ru.simbir.internship.chat.repository.UserRoomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class BlockedUserService {
    private final BlockedUserRepository blockedUserRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Autowired
    public BlockedUserService(UserRepository userRepository, BlockedUserRepository blockedUserRepository,
                              UserRoomRepository userRoomRepository) {
        this.blockedUserRepository = blockedUserRepository;
        this.userRepository = userRepository;
        this.userRoomRepository = userRoomRepository;
        updateBlackList();
    }

    private void updateBlackList() {
        List<User> blockedUsers = userRepository.findAllByStatus(UserStatus.GLOBAL_BLOCKED);
        blockedUserRepository.deleteAll();
        blockedUserRepository.saveAll(blockedUsers.stream()
                .map(v -> new BlockedUser(v.getId())).collect(Collectors.toList()));
    }

    @Scheduled(fixedRate = 2 * 60 * 1000L) // 2 min
    public void unblockTask() {
        List<UserRoom> blockMembers = userRoomRepository.findAllByUserRoomRole(UserRoomRole.BLOCKED_USER);
        blockMembers.forEach(v -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime blockTime = v.getBlockedTime();
            if (blockTime.isEqual(now) || blockTime.isBefore(now)) {
                v.setUserRoomRole(v.getOldRole() == UserRoomRole.BLOCKED_USER ? UserRoomRole.USER : v.getOldRole());
                userRoomRepository.save(v);
            }
        });
    }
}
