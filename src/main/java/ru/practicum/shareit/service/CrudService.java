package ru.practicum.shareit.service;

public interface CrudService <Id, Dto, CreateRequestDto, UpdateRequestDto> {
    Dto create(CreateRequestDto request);
    Dto read(Long id);
    Dto update(Long id, UpdateRequestDto request);
    void delete(Id id);
}
