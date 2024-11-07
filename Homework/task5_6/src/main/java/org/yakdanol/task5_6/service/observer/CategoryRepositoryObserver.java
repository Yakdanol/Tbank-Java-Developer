package org.yakdanol.task5_6.service.observer;

import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.model.entity.Category;
import org.yakdanol.task5_6.model.repository.CategoryRepository;

@Component
public class CategoryRepositoryObserver extends RepositoryObserver<Category> {

    public CategoryRepositoryObserver(CategoryRepository repository) {
        super(repository);
    }
}
