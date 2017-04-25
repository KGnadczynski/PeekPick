package com.tackpad.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "Company_credit")
@Getter
@Setter
public class CompanyCredit {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    @Valid
    private Company company;

    @Column(nullable = false)
    @NotNull
    private int credit = 15;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateForFreePlanDate = new Date();

}
