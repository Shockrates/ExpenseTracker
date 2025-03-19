package com.sokratis.ExpenseTracker.Mapper;

import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityMapper<E, D> {

    private final ModelMapper modelMapper;

    public EntityMapper() {
        this.modelMapper = new ModelMapper();
    }

    /**
     * Converts an Entity to its corresponding DTO.
     * @param entity the Entity object
     * @return the mapped DTO
     */
    public D toDTO(E entity) {
        if (entity == null) {
            return null;
        }
        return modelMapper.map(entity, getDTOClass());
    }

    /**
     * Converts a DTO to its corresponding Entity.
     * @param dto the DTO object
     * @return the mapped Entity
     */
    public E toEntity(D dto) {
        if (dto == null) {
            return null;
        }
        return modelMapper.map(dto, getEntityClass());
    }

    /**
     * Converts a list of Entities to a list of DTOs.
     * @param enitytlist the list of Entity objects
     * @return the mapped list of DTOs
     */
    public List<D> toDTOList(List<E> enitytlist) {
        if (enitytlist == null) {
            return null;
        }
        return enitytlist.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
    }

    /**
     * Converts a list of DTOs to a list of Entities.
     * @param dtolist the list of DTO objects
     * @return the mapped list of Entities
     */
    public List<E> toEntityList(List<D> dtolist) {
        if (dtolist == null) {
            return null;
        }
        return dtolist.stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList());
    }

    /**
     * Subclasses should implement this method to provide the DTO class type.
     * @return the DTO class
     */
    protected abstract Class<D> getDTOClass();

    /**
     * Subclasses should implement this method to provide the Entity class type.
     * @return the Entity class
     */
    protected abstract Class<E> getEntityClass();
}


