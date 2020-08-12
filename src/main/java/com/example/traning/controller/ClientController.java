package com.example.traning.controller;

import com.example.traning.entity.Client;
import com.example.traning.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("client/id/{id}")
    public Client getClientById(@PathVariable Long id){
        Optional<Client> clientOptional = clientRepository.findById(id);
       if(clientOptional.isPresent()){
           return clientOptional.get();
       } else {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Client not found!");
       }

    }

    @PostMapping("client")
    public Client addClient (@RequestBody String name){

        Client client = new Client();
        client.setName(name);
        return clientRepository.save(client);

    }

    @PutMapping("client/id/{id}")
    public Client putClient (@PathVariable Long id, @RequestBody String name){
        Optional<Client> clientOptional = clientRepository.findById(id);
        if(clientOptional.isPresent()){
            Client client = clientOptional.get();
            client.setName(name);
            return clientRepository.save(client);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Client not found!");
        }

    }

    @DeleteMapping("client/id/{id}")
    public Client deleteClient (@PathVariable Long id){
        Optional<Client> clientOptional = clientRepository.findById(id);
        if(clientOptional.isPresent()){
            Client client = clientOptional.get();
            clientRepository.delete(client);
            return client;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Client not found!");
        }

    }

}

