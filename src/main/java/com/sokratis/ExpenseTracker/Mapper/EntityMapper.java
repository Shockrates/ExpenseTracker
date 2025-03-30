package com.sokratis.ExpenseTracker.Mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class EntityMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    // Convert a single entity to a DTO
    public static <D, E> D toDTO(E entity, Class<D> dtoClass) {
        if (entity == null) {
            return null;
        }
        return modelMapper.map(entity, dtoClass);
    }

    // Convert a single DTO to an entity
    public static <D, E> E toEntity(D dto, Class<E> entityClass) {
        if (dto == null) {
            return null;
        }
        return modelMapper.map(dto, entityClass);
    }

    // Convert a list of entities to a list of DTOs
    public static <D, E> List<D> toDTOList(List<E> entityList, Class<D> dtoClass) {
        if (entityList == null) {
            return null;
        }
        Type listType = new TypeToken<List<D>>() {}.getType();
        return modelMapper.map(entityList, listType);
    }

    // Convert a list of DTOs to a list of entities
    public static <D, E> List<E> toEntityList(List<D> dtoList, Class<E> entityClass) {
        if (dtoList == null) {
            return null;
        }
        Type listType = new TypeToken<List<E>>() {}.getType();
        return modelMapper.map(dtoList, listType);
    }
}
