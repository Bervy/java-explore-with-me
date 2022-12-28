package ru.practicum.explore.model.category;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORIES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
}