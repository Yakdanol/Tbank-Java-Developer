package org.yakdanol.task5_6.model.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.model.entity.Event;
import org.yakdanol.task5_6.model.entity.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EventSpecificationDAO {

    /**
     * Метод для создания спецификации для поиска событий.
     * @param name      имя события
     * @param location  локация события
     * @param fromDate  начальная дата
     * @param toDate    конечная дата
     * @return спецификация для поиска событий
     */
    public Specification<Event> buildEventSpecification(String name, Location location, Date fromDate, Date toDate) {
        List<Specification<Event>> specifications = new ArrayList<>();

        if (name != null) {
            specifications.add((Specification<Event>) (event, query, criteriaBuilder) ->
                    criteriaBuilder.equal(event.get("name"), name));
        }

        if (location != null) {
            specifications.add((Specification<Event>) (event, query, criteriaBuilder) ->
                    criteriaBuilder.equal(event.get("location"), location));
        }

        if (fromDate != null && toDate != null) {
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

