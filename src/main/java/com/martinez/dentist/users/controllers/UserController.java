package com.martinez.dentist.users.controllers;

import com.martinez.dentist.users.models.User;
import com.martinez.dentist.users.repositories.UserRepository;
import com.martinez.dentist.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/save")
    @Transactional
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody UserRequestDTO dto,
                                             @RequestParam("adminUsername") String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Usuario autenticador no encontrado"));

        return ResponseEntity.ok(userService.updateUser(id, dto, admin));
    }

}
