package com.tackpad.models;


import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;

import javax.persistence.*;

@Entity
@Table
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
