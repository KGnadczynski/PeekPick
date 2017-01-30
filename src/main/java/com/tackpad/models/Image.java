package com.tackpad.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.ImageType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Przemysław Żynis on 05.12.2016.
 */
@Entity
@Table
public class Image {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public Message message;

    @ManyToOne
    public Company company;

    @Column(nullable = false, length = 200)
    public String imageUrl;

    @Column(nullable = false, length = 200)
    public String imageId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    public Date date = new Date();

    /** Typ.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType type = ImageType.MAIN;

}
