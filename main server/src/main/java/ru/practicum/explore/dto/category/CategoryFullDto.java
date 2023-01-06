package ru.practicum.explore.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryFullDto {
    @Positive
    private Long id;
    @NotBlank
    private String name;
}