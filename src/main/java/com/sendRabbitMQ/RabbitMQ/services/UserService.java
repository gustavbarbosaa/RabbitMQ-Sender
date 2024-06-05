package com.sendRabbitMQ.RabbitMQ.services;

import com.sendRabbitMQ.RabbitMQ.configs.RabbitMQConfig;
import com.sendRabbitMQ.RabbitMQ.models.UserModel;
import com.sendRabbitMQ.RabbitMQ.repositories.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    public UserModel registerUser(UserModel userModel) {
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        rabbitTemplate.convertAndSend(rabbitMQConfig.getRegisterExchangeName(), rabbitMQConfig.getRegisterRoutingKey(), userModel.getEmail());
        return userRepository.save(userModel);
    }

    public UserModel editUser(Long id, UserModel userModel) {
        Optional<UserModel> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            return null;
        }

        UserModel user = userOptional.get();

        System.out.println(user);

        if (userModel.getName() != null) {
            user.setName(userModel.getName());
        }
        if (userModel.getAge() != null) {
            user.setAge(userModel.getAge());
        }
        if (userModel.getEmail() != null) {
            user.setEmail(userModel.getEmail());
        }
        if (userModel.getPassword() != null) {
            user.setPassword(userModel.getPassword());
        }
        if (userModel.getAddress() != null) {
            user.setAddress(userModel.getAddress());
        }
        return userRepository.save(user);
    }
}
