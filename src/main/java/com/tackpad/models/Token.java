package com.tackpad.models;


import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.CreateBusinessUserForm;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity(name = "token")
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue
    public Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    @Column(nullable = false)
    public String value;

    @ManyToOne(optional = true)
    public User user;
}
