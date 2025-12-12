package ru.practicum.shareit.feature.request.dal;

import ru.practicum.shareit.feature.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository {
    ItemRequest save(ItemRequest item);

    Optional<ItemRequest> findById(Long itemRequestId);

    List<ItemRequest> findByRequesterIdOrderByCreated(Long requesterId);

    List<ItemRequest> findAllByOrderByCreated();
}
