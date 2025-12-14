package ru.practicum.shareit.feature.item.dal;

import ru.practicum.shareit.feature.item.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findByItemId(Long itemId);
}
