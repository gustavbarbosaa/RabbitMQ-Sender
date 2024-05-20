package com.sendRabbitMQ.RabbitMQ.controllers;

import com.sendRabbitMQ.RabbitMQ.configs.RabbitMQConfig;
import com.sendRabbitMQ.RabbitMQ.models.UserModel;
import com.sendRabbitMQ.RabbitMQ.repositories.UserRepository;
import com.sendRabbitMQ.RabbitMQ.services.UserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;


    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() throws Exception {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<UserModel> postUser(@RequestBody UserModel userModel) throws Exception {
        return ResponseEntity.ok().body(userService.registerUser(userModel));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserModel> editUser(@PathVariable Long id, @RequestBody UserModel userModel) throws Exception {
        UserModel userUpdate = userService.editUser(id, userModel);

        if (userUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws Exception {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
