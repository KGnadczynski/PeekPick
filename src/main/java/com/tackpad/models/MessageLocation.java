package com.tackpad.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class MessageLocation {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String streetNo;
    
    @Column(nullable = false)
    @NotNull(groups = {Message.CreateMessageValidation.class, Message.UpdateMessageValidation.class})
    private Double latitude;

    @Column(nullable = false)
    @NotNull(groups = {Message.CreateMessageValidation.class, Message.UpdateMessageValidation.class})
    private Double longitude;

    @Column(nullable = false)
    @NotNull(groups = {Message.CreateMessageValidation.class, Message.UpdateMessageValidation.class})
    private String address;

    @Column(nullable = false)
    @NotNull(groups = {Message.CreateMessageValidation.class, Message.UpdateMessageValidation.class})
    private String name;
    
}
