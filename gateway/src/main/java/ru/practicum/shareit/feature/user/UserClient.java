package ru.practicum.shareit.feature.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.feature.user.dto.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.UpdateUserRequest;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> create(CreateUserRequest request) {
        return this.post("", request);
    }

    public ResponseEntity<Object> findById(Long userId) {
        return this.get("/" + userId);
    }

    public ResponseEntity<Object> update(Long userId, UpdateUserRequest request) {
        return this.patch("/" + userId, request);
    }

    public void delete(Long userId) {
        this.delete("/" + userId);
    }
}
