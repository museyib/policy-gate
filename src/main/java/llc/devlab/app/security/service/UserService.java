package llc.devlab.app.security.service;

import llc.devlab.app.exception.ForbiddenException;
import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.mapper.UserMapper;
import llc.devlab.app.security.model.dto.UserDto;
import llc.devlab.app.security.model.dto.UserUpdateRequest;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.model.enums.UserType;
import llc.devlab.app.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static llc.devlab.app.config.CacheConfig.CacheNames.*;
import static llc.devlab.app.security.model.enums.UserType.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getById(Long id) {
        return userMapper.toDto(loadAppUser(id));
    }

    @Transactional
    @CacheEvict(value = {HAS_PERMISSION, ROLE_PERMISSIONS, USER_PERMISSIONS}, allEntries = true)
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        AppUser appUser = loadAppUser(id);

        userMapper.updateEntity(request, appUser);
        return userMapper.toDto(appUser);
    }

    @Transactional
    @CacheEvict(value = {HAS_PERMISSION, ROLE_PERMISSIONS, USER_PERMISSIONS}, allEntries = true)
    public UserDto changeUserType(Long id, UserType userType) {
        AppUser appUser = loadAppUser(id);

        if (appUser.isRootUser() && USER.equals(userType)) {
            throw new ForbiddenException("Root user cannot be changed");
        }
        appUser.setUserType(userType);
        return userMapper.toDto(appUser);
    }

    public UserDto getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private AppUser loadAppUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
