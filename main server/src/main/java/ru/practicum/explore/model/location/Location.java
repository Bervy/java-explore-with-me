package ru.practicum.explore.model.location;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "LOCATIONS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float lat;
    private Float lon;
}