package ru.practicum.explore.utils.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.stat.StatFullDto;

@Service
@PropertySource("classpath:application.properties")
public class StatClient extends BaseClient {

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl) {
        super(serverUrl);
    }

    public void saveHit(StatFullDto statInDto) {
        post("/hit", statInDto);
    }
}