package ru.practicum.explore.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.StatInDto;

import java.util.List;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class AdminStatClient extends BaseClient {

    @Autowired
    public AdminStatClient(@Value("${stats-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public ResponseEntity<Object> getHits(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public void saveHit(StatInDto statInDto) {
        post("/hit", statInDto);
    }
}