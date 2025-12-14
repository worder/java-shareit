package ru.practicum.shareit.feature.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.feature.request.dto.CreateItemRequestDto;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    ResponseEntity<Object> create(Long userId, CreateItemRequestDto request) {
        return this.post("", userId, request);
    }

    ResponseEntity<Object> findById(Long requestId) {
        return this.get("/" + requestId);
    }

    ResponseEntity<Object> findAllByRequesterId(Long userId) {
        return this.get("", userId);
    }

    ResponseEntity<Object> findAll() {
        return this.get("/all");
    }
}
