package com.martinez.dentist.users.controllers;

import com.martinez.dentist.users.repositories.UserIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserIMPL userService;
    @PostMapping(path = "/save")
    public ResponseEntity<String> save(@RequestBody UserDTO userDTO) {
        try {
            userService.addUser(userDTO);
            return ResponseEntity.ok("El usuario fue creado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocurrió un error al guardar el usuario: " + e.getMessage());
        }
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO)
    {
        LoginResponse loginResponse = userService.loginUser(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}
