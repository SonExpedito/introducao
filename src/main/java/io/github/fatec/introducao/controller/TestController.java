package io.github.fatec.introducao.controller;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.fatec.introducao.dto.UserDataDTO;
import io.github.fatec.introducao.dto.UserUpdateResponseDTO;

@RestController
@RequestMapping("/teste")
public class TestController {

    // Simulação de banco de dados em memória
    private final Map<String, UserDataDTO> userDatabase = new ConcurrentHashMap<>();

    // GET http://localhost:8080/teste
    @GetMapping
    public String getRoot() {
        return "Rodando";
    }

    // GET http://localhost:8080/teste/random-id
    @GetMapping("/random-id")
    public String gerarId() {
        return UUID.randomUUID().toString();
    }

    // GET http://localhost:8080/teste/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> getUserById(@PathVariable String id) {
        UserDataDTO userData = userDatabase.get(id);

        if (userData == null) {
            return ResponseEntity.notFound().build();
        }

        UserUpdateResponseDTO response =
                new UserUpdateResponseDTO(
                        id,
                        userData.getNome(),
                        userData.getTelefone(),
                        userData.getEndereco()
                );

        return ResponseEntity.ok(response);
    }

    // POST http://localhost:8080/teste
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserUpdateResponseDTO createUser(@RequestBody UserDataDTO userData) {

        String newId = UUID.randomUUID().toString();
        userDatabase.put(newId, userData);

        return new UserUpdateResponseDTO(
                newId,
                userData.getNome(),
                userData.getTelefone(),
                userData.getEndereco()
        );
    }

    // PUT http://localhost:8080/teste/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDTO> updateUser(
            @PathVariable String id,
            @RequestBody UserDataDTO userData) {

        if (!userDatabase.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        userDatabase.put(id, userData);

        UserUpdateResponseDTO response =
                new UserUpdateResponseDTO(
                        id,
                        userData.getNome(),
                        userData.getTelefone(),
                        userData.getEndereco()
                );

        return ResponseEntity.ok(response);
    }

    // DELETE http://localhost:8080/teste/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {

        if (userDatabase.remove(id) == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Usuário com ID " + id + " deletado com sucesso.");
    }
}