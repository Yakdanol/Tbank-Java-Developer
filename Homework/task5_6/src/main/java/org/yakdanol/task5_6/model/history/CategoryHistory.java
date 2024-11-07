package org.yakdanol.task5_6.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.yakdanol.task5_6.model.entity.Category;

import java.time.LocalDateTime;

@Entity
@Table(name = "category_history")
@Getter
@Setter
public class CategoryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long categoryId;
    private String slug;
    private String name;
    private LocalDateTime createdAt;

    public static CategoryHistory from(Category category) {
        CategoryHistory snapshot = new CategoryHistory();
        snapshot.setCategoryId(category.getId());
        snapshot.setSlug(category.getSlug());
        snapshot.setName(category.getName());
        snapshot.setCreatedAt(LocalDateTime.now());
        return snapshot;
    }
}
