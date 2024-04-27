package com.clear.solutions.user.management.system.service.base;

import com.clear.solutions.user.management.system.service.base.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional
public class BaseService<T extends BaseEntity, V extends CrudRepository<T, Long>> {

    private final V crudRepository;

    public T save(T entity)  {
        Long entityId = entity.getId();
        return Objects.isNull(entityId)? crudRepository.save(entity) : update(entity);
    }

    private T update(T entity) {
        Long entityId = entity.getId();

        T dbEntity = crudRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException(entityId));
        updateEntityProperties(entity, dbEntity);

        return crudRepository.save(dbEntity);
    }

    private void updateEntityProperties(T entity, T dbEntity) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true);

        modelMapper.map(entity, dbEntity);
    }

}