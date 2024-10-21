package org.yakdanol.task5_6.repository;

import org.yakdanol.task5_6.exception.CategoryNotFoundException;
import org.yakdanol.task5_6.exception.EmptyRepositoryException;
import org.yakdanol.task5_6.model.Category;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class CategoryRepository {
    private final ConcurrentMap<Long, Category> categoryMap = new ConcurrentHashMap<>();

    public ConcurrentMap<Long, Category> findAll() {
        if (categoryMap.isEmpty()) {
            throw new EmptyRepositoryException("Category repository");
        }
        return categoryMap;
    }

    public Category findById(Long id) {
        Category category = categoryMap.get(id);
        if (category == null) {
            throw new CategoryNotFoundException(id);
        }
        return category;
    }

    public Category save(Long id, Category category) {
        categoryMap.put(id, category);
        return category;
    }

    public void deleteById(Long id) {
        if (!categoryMap.containsKey(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryMap.remove(id);
    }
}
