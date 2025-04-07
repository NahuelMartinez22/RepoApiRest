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
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponse loginResponse = userService.loginUser(loginDTO);

            switch (loginResponse.getMessage()) {
                case "Login Success":
                    return ResponseEntity.ok(loginResponse);

                case "User not found":
                    return ResponseEntity.status(404).body(loginResponse);

                case "Password does not match":
                    return ResponseEntity.status(400).body(loginResponse);

                default:
                    return ResponseEntity.status(500).body(loginResponse);
            }
        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse("An error occurred: " + e.getMessage(), false);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
