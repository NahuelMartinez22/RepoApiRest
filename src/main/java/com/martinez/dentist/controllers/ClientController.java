package com.martinez.dentist.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {


    @GetMapping
    public ClientDTO getClient() {
        //clients.put(id, name);
        ClientDTO clientDTO = new ClientDTO("Nahuel","nahuelarroba", 12);
        return clientDTO;
    }

    @PostMapping
    public String createClient(@RequestBody ClientDTO clientDTO) {
        //clients.put(id, name);
        return "Client added successfully!";
    }

}
