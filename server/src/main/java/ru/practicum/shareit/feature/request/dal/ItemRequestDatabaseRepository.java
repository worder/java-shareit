package ru.practicum.shareit.feature.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.request.model.ItemRequest;

@Repository
public interface ItemRequestDatabaseRepository extends ItemRequestRepository, JpaRepository<ItemRequest, Long> {
}
