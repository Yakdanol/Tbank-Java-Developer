package org.yakdanol.task5_6.service.observer;

import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.model.repository.CategoryHistoryRepository;
import org.yakdanol.task5_6.model.entity.Category;
import org.yakdanol.task5_6.model.history.CategoryHistory;

@Component
public class CategoryHistoryObserver implements Observer<Category> {

    private final CategoryHistoryRepository historyRepository;

    public CategoryHistoryObserver(CategoryHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public void update(Category entity) {
        CategoryHistory snapshot = CategoryHistory.from(entity);
        historyRepository.save(snapshot);
    }
}
