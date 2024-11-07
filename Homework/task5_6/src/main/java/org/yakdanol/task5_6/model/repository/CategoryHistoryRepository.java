package org.yakdanol.task5_6.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yakdanol.task5_6.model.history.CategoryHistory;

@Repository
public interface CategoryHistoryRepository extends JpaRepository<CategoryHistory, Long> {
}
