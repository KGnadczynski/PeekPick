package com.tackpad.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class MessageLocation {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String city;

    @Column
    public String street;

    @Column
    public String streetNo;
    
    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public Double latitude;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public Double longitude;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public String address;

    @Column(nullable = false)
    @NotNull(groups = Message.CreateMessageValidation.class)
    public String name;
    
}
