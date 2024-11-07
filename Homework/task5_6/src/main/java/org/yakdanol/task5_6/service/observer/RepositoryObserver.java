package org.yakdanol.task5_6.service.observer;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class RepositoryObserver<T> implements Observer<T> {

    private final JpaRepository<T, Long> repository;

    protected RepositoryObserver(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    @Override
    public void update(T entity) {
        repository.save(entity);
    }
}
