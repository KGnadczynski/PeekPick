package com.tackpad.models;

import javax.persistence.*;

/**
 * Oddzial firmy
 */
@Entity
@Table
public class CompanyBranch {

    @Id
    @GeneratedValue
    public Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    public String name;

    /** Miasto.*/
    @Column(nullable = false)
    public String city;

    /** Ulica.*/
    @Column(nullable = false)
    public String street;

    /** Numer lokalu.*/
    @Column(nullable = false)
    public String streetNo;

    /** Długość geograficzna.*/
    @Column
    public Double latitude;

    /** Szerokość geograficzna.*/
    @Column
    public Double longitude;

    @ManyToOne(optional = true)
    public Company company;


}
