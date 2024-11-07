package org.yakdanol.task5_6.model.history;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.yakdanol.task5_6.model.entity.Location;

import java.time.LocalDateTime;

@Entity
@Table(name = "location_history")
@Getter
@Setter
public class LocationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long locationId;
    private String slug;
    private String name;
    private LocalDateTime createdAt;

    public static LocationHistory from(Location location) {
        LocationHistory snapshot = new LocationHistory();
        snapshot.setLocationId(location.getId());
        snapshot.setSlug(location.getSlug());
        snapshot.setName(location.getName());
        snapshot.setCreatedAt(LocalDateTime.now());
        return snapshot;
    }
}
