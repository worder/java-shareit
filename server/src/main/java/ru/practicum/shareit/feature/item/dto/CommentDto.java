package ru.practicum.shareit.feature.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.feature.item.model.Comment;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentDto {
     Long id;
     String text;
     String authorName;
     LocalDateTime created;

     public static CommentDto fromModel(Comment comment) {
         return CommentDto.builder()
                 .id(comment.getId())
                 .text(comment.getText())
                 .authorName(comment.getAuthor().getName())
                 .created(comment.getCreated())
                 .build();
     }
}
