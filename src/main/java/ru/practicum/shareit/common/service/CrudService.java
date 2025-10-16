package ru.practicum.shareit.common.service;

public interface CrudService<ID, D, C, U> {
    D create(C request);

    D read(Long id);

    D update(Long id, U request);

    void delete(ID id);
}
