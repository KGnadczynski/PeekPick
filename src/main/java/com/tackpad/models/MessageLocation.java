package com.tackpad.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class MessageLocation {

    @Id
    @GeneratedValue
    public Long id;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public String city;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public String street;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public String streetNo;
    
    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public Double latitude;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public Double longitude;
    
}
