package ru.practicum.shareit.feature.item.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.item.model.Item;

import java.util.List;

@Primary
@Repository
public interface DatabaseItemRepository extends ItemRepository, JpaRepository<Item, Long> {
    @Query("""
            SELECT i
            FROM Item i
            WHERE i.owner.id = :ownerId
                AND i.available = true
                AND (lower(i.name) LIKE lower(concat('%', :text, '%'))
                    OR lower(i.description) LIKE lower(concat('%', :text, '%')))
            """)
    List<Item> findByNameOrDescription(@Param("ownerId") Long ownerId,
                                       @Param("text") String text);
}
