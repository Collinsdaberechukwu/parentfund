package org.example.parentfund.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.parentfund.Enum.StudentStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "student_class", nullable = false)
    private String studentClass;

    @Column(name = "is_shared", nullable = false)
    private Boolean isShared;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "password", nullable = false)
    private String password;



    @Enumerated(EnumType.STRING)
    @Column(name = "student_status")
    private StudentStatus studentStatus;


    @ManyToMany
    @JoinTable(
            name = "student_parent",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private List<Parent> parents = new ArrayList<>();




}
