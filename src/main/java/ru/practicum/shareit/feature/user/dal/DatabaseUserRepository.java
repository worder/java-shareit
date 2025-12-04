package ru.practicum.shareit.feature.user.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.user.User;

@Primary
@Repository
public interface DatabaseUserRepository extends UserRepository, JpaRepository<User, Long> {
}
