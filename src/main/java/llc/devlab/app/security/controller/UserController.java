package llc.devlab.app.security.controller;

import llc.devlab.app.security.model.dto.UserDto;
import llc.devlab.app.security.model.dto.UserUpdateRequest;
import llc.devlab.app.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api/users"))
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasPermission('USER', 'READ')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('USER', 'READ')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasPermission('USER', 'UPDATE')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }
}
