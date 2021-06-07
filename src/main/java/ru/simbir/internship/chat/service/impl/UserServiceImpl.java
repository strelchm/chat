package ru.simbir.internship.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.simbir.internship.chat.domain.User;
import ru.simbir.internship.chat.domain.UserAppRole;
import ru.simbir.internship.chat.domain.UserStatus;
import ru.simbir.internship.chat.dto.UserDto;
import ru.simbir.internship.chat.exception.AccessDeniedException;
import ru.simbir.internship.chat.exception.BadRequestException;
import ru.simbir.internship.chat.exception.NotFoundException;
import ru.simbir.internship.chat.repository.UserRepository;
import ru.simbir.internship.chat.service.UserService;
import ru.simbir.internship.chat.util.MappingUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.userRepository = repository;
        this.encoder = encoder;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(MappingUtil::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(UUID id) {
        return MappingUtil.mapToUserDto(getUserById(id));
    }

    @Override
    public UUID add(UserDto dto) {
        if (dto.getUserAppRole() == null) {
            dto.setUserAppRole(UserAppRole.CLIENT);
        } else if (dto.getUserAppRole() == UserAppRole.ADMIN) {
            throw new BadRequestException("Can't register admin during standard registration attempt");
        }

        dto.setPassword(encoder.encode(dto.getPassword()));
        dto.setStatus(UserStatus.ACTIVE);
        return userRepository.save(MappingUtil.mapToUserEntity(dto)).getId();
    }

    @Override
    public UserDto edit(UserDto dto, UserDto userDto) {
        if (!dto.getId().equals(userDto.getId()) && userDto.getUserAppRole() != UserAppRole.ADMIN) { // редактировать можно только самому себя или админу
            throw new AccessDeniedException();
        }

        User user = getUserById(dto.getId());

        if (dto.getLogin() != null && !user.getLogin().equals(dto.getLogin())) {
            user.setLogin(dto.getLogin());
        }

        if (dto.getStatus() != null && !user.getStatus().equals(dto.getStatus())) {
            user.setStatus(dto.getStatus());
        }

        return MappingUtil.mapToUserDto(userRepository.save(user));
    }

    @Override
    public void delete(UUID id, UserDto userDto) {
        User user = getUserById(id);

        if (id.equals(userDto.getId()) || userDto.getUserAppRole() == UserAppRole.ADMIN) { // удалить можно только самому себя или админу
            userRepository.deleteById(id);
        } else {
            throw new AccessDeniedException();
        }


    }

    @Override
    @Secured("ROLE_ADMIN")
    public void blockUser(UUID userId, UserDto userDto) {
        changeUserStatusByAdmin(userId, userDto, UserStatus.GLOBAL_BLOCKED);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public void unblockUser(UUID userId, UserDto userDto) {
        changeUserStatusByAdmin(userId, userDto, UserStatus.ACTIVE);
    }

    private void changeUserStatusByAdmin(UUID userId, UserDto userDto, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public Optional<UserDto> getUserByLogin(String login) {
        User user = userRepository.findByLogin(login).orElse(null);
        return Optional.ofNullable(MappingUtil.mapToUserDto(user));
    }
}
