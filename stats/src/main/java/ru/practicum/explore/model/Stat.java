package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats", indexes = {@Index(name = "APP_URI_INDEX", columnList = "APP, URI, IP")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}