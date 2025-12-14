package ru.practicum.shareit.feature.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    ResponseEntity<Object> create(Long userId, CreateItemRequest item) {
        return this.post("", userId, item);
    }

    ResponseEntity<Object> findById(Long userId, Long itemId) {
        return this.get("/" + itemId, userId);
    }

    ResponseEntity<Object> update(Long userId, Long itemId, UpdateItemRequest request) {
        return this.patch("/" + itemId, userId, request);
    }

    ResponseEntity<Object> getUserItems(Long userId) {
        return this.get("", userId);
    }

    ResponseEntity<Object> findUserItems(Long userId, String text) {
        return this.get("/search?text={text}", userId, Map.of("text", text));
    }

    ResponseEntity<Object> createComment(Long userId, Long itemId, CreateCommentRequest request) {
        return this.post("/" + itemId + "/comment", userId, request);
    }
}
