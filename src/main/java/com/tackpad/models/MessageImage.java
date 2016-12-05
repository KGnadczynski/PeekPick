package com.tackpad.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Przemysław Żynis on 05.12.2016.
 */
@Entity
@Table
public class MessageImage {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public Message message;

    @Column(nullable = false, length = 200)
    public String imageUrl;

    @Column(nullable = false, length = 200)
    public String imageId;

}
