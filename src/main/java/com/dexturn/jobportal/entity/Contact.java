package com.dexturn.jobportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NamedQueries(
        @NamedQuery(name="Contact.updateStatusById",
                query = "UPDATE Contact c SET c.status = :status, c.updatedAt= CURRENT_TIMESTAMP, c.updatedBy= :updatedBy WHERE c.id =: id")
)
public class  Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false)
    private String status;


}