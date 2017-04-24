package com.tackpad.models;

import com.tackpad.models.enums.BusinessPlanStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "Business_plan")
public class BusinessPlan {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotNull
    private BigDecimal nettValue;

    @Column(nullable = false)
    @NotNull
    private BigDecimal grossValue;

    @Column(nullable = false)
    @NotNull
    private int messageCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BusinessPlanStatus status = BusinessPlanStatus.ACTIVE;

}
