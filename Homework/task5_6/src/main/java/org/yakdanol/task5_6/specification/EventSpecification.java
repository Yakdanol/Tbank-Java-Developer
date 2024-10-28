package org.yakdanol.task5_6.specification;

import org.springframework.data.jpa.domain.Specification;
import org.yakdanol.task5_6.model.entity.Event;
import org.yakdanol.task5_6.model.entity.Location;

import java.time.LocalDateTime;

public class EventSpecification {

    public static Specification<Event> hasName(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }

    public static Specification<Event> hasLocation(Location location) {
        return (root, query, builder) -> builder.equal(root.get("location"), location);
    }

    public static Specification<Event> dateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, builder) -> builder.between(root.get("date"), fromDate, toDate);
    }
}
