package com.tackpad.models;


import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(nullable = false)
    private String value;

    @Column
    private String data;

    @ManyToOne(optional = true)
    private User user;
}
