package ru.practicum.explore.controller.admin_part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsAdminController {
//    private final AdminStatsClient adminStatsClient;
//
//    @GetMapping("/admin/stats")
//    public ResponseEntity<Object> getHits(
//            @NotNull @RequestParam(name = "start") String start,
//            @NotNull @RequestParam(name = "end") String end,
//            @Valid @RequestParam(name = "uris", defaultValue = "", required = false) List<String> uris,
//            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
//        return adminStatsClient.getHits(start, end, uris, unique);
//    }
}