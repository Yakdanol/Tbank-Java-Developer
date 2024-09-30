package org.yakdanol.homework.repository;

import org.yakdanol.homework.model.Category;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class CategoryRepository {
    private final ConcurrentMap<Long, Category> categoryMap = new ConcurrentHashMap<>();

    public ConcurrentMap<Long, Category> findAll() {
        return categoryMap;
    }

    public Category findById(Long id) {
        return categoryMap.get(id);
    }

    public Category save(Long id, Category category) {
        categoryMap.put(id, category);
        return category;
    }

    public void deleteById(Long id) {
        categoryMap.remove(id);
    }
}
