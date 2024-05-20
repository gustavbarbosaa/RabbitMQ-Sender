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
        System.out.println(rabbitMQConfig.getRegisterExchangeName());
        System.out.println(rabbitMQConfig.getRegisterQueueName());
        System.out.println(userModel.getEmail());
        rabbitTemplate.convertAndSend(rabbitMQConfig.getRegisterExchangeName(), rabbitMQConfig.getRegisterRoutingKey(), userModel.getEmail());
        return userRepository.save(userModel);
    }

    public UserModel editUser(Long id, UserModel userModel) {
        Optional<UserModel> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            UserModel existingUser = optionalUser.get();

            if (!Objects.equals(userModel.getName(), existingUser.getName())) {
                existingUser.setName(userModel.getName());
            }

            if (!Objects.equals(userModel.getAge(), existingUser.getAge())) {
                existingUser.setAge(userModel.getAge());
            }

            if (!Objects.equals(userModel.getEmail(), existingUser.getEmail())) {
                existingUser.setEmail(userModel.getEmail());
            }

            if (!Objects.equals(userModel.getPassword(), existingUser.getPassword())) {
                String password = userModel.getPassword();
                existingUser.setPassword(bCryptPasswordEncoder.encode(password));

            }

            if (!Objects.equals(userModel.getAddress(), existingUser.getAddress())) {
                existingUser.setAddress(userModel.getAddress());
            }

            rabbitTemplate.convertAndSend(rabbitMQConfig.getEditExchangeName(), rabbitMQConfig.getEditRoutingKey(), existingUser);
            return userRepository.save(existingUser);
        }
        return null;
    }
}
