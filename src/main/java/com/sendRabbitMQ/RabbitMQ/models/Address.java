package com.sendRabbitMQ.RabbitMQ.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String road;
    private String neighborhood;
    private String city;
    private String postal_code;
    private Integer number;
}
