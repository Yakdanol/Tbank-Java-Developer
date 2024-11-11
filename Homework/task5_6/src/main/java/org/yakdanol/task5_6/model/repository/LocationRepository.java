package org.yakdanol.task5_6.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yakdanol.task5_6.model.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    void save(String slug, Location location);
}
