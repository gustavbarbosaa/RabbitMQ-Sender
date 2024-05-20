package com.sendRabbitMQ.RabbitMQ.repositories;

import com.sendRabbitMQ.RabbitMQ.models.Address;
import com.sendRabbitMQ.RabbitMQ.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByName(String name);
    Optional<UserModel> findByAge(int age);
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByAddress(Address address);
}
