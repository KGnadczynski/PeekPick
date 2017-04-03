package com.tackpad.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.ImageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Message message;

    @ManyToOne
    private Company company;

    @Column(nullable = false, length = 200)
    private String imageUrl;

    @Column(nullable = false, length = 200)
    private String imageId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(nullable = false, columnDefinition = "TIMESTAMPTZ(3)")
    private Date date = new Date();

    /** Typ.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType type = ImageType.MAIN;

}
