package ru.practicum.shareit.feature.item.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.item.model.Comment;

@Repository
@Primary
public interface DatabaseCommentRepository extends CommentRepository, JpaRepository<Comment, Long> {
}
