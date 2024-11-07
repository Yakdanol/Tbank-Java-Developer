package org.yakdanol.task5_6.service.observer;

import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.model.entity.Location;
import org.yakdanol.task5_6.model.history.LocationHistory;
import org.yakdanol.task5_6.model.repository.LocationHistoryRepository;

@Component
public class LocationHistoryObserver implements Observer<Location> {

    private final LocationHistoryRepository historyRepository;

    public LocationHistoryObserver(LocationHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public void update(Location entity) {
        LocationHistory snapshot = LocationHistory.from(entity);
        historyRepository.save(snapshot);
    }
}
