package com.martinez.dentist.controllers;

import com.martinez.dentist.Dto.UserDTO;
import com.martinez.dentist.models.User;
import com.martinez.dentist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping(path = "/save")
    public ResponseEntity<String> save(@RequestBody UserDTO userDTO) {
        try {
            userService.addUser(userDTO);
            return ResponseEntity.ok("El usuario fue creado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocurrió un error al guardar el usuario: " + e.getMessage());
        }
    }
}
