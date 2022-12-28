package ru.practicum.explore.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    @NotBlank
    private String title;
    private List<Long> events;
    private Boolean pinned;
}