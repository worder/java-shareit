package ru.practicum.shareit.common.service;

public interface CrudService<ID, D, C, U> {
    D create(C request);

    D read(ID id);

    D update(ID id, U request);

    void delete(ID id);
}
