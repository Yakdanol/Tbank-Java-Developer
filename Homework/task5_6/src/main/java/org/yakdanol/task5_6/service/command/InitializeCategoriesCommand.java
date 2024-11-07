package org.yakdanol.task5_6.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.service.CategoryService;

@Component
public class InitializeCategoriesCommand implements Command {

    private final CategoryService categoryService;

    @Autowired
    public InitializeCategoriesCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void execute() {
        categoryService.initializeCategoriesFromExternalAPI();
    }
}
