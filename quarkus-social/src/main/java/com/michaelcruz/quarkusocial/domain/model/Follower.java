package com.michaelcruz.quarkusocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;

@Entity
@Table(name = "folowers")
@Data
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User follower;
}
