package ru.practicum.explore.model.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private Float rate;
}