package ru.practicum.shareit.feature.item.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.item.model.Item;

import java.util.List;

@Primary
@Repository
public interface DatabaseItemRepository extends ItemRepository, JpaRepository<Item, Long> {
    @Query("""
            SELECT i
            FROM Item i
            WHERE i.owner.id = ?1
            AND i.available = true
            AND (lower(i.name) LIKE lower(concat('%', ?2, '%'))
                OR lower(i.description) LIKE lower(concat('%', ?2, '%')))
            """)
    List<Item> findByNameOrDescription(Long ownerId, String text);
}
