package org.yakdanol.task5_6.model.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yakdanol.task5_6.model.entity.Event;
import org.yakdanol.task5_6.model.entity.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAll(Specification<Event> specification);

    static Specification<Event> buildEventSpecification(String name, Location location, Date fromDate, Date toDate) {
        List<Specification<Event>> specifications = new ArrayList<>();

        if (name != null) {
            specifications.add((Specification<Event>) (event, query, criteriaBuilder) ->
                    criteriaBuilder.equal(event.get("name"), name));
        }

        if (location != null) {
            specifications.add((Specification<Event>) (event, query, criteriaBuilder) ->
                    criteriaBuilder.equal(event.get("location"), name));
        }

        if ((fromDate != null) && (toDate != null)) {
            specifications.add((Specification<Event>) (event, query, criteriaBuilder) ->
                    criteriaBuilder.between(event.get("date"), fromDate, toDate));
        } else if (fromDate != null) {
            specifications.add((event, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(event.get("date"), fromDate));
        } else if (toDate != null) {
            specifications.add((event, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(event.get("date"), toDate));
        }

        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
