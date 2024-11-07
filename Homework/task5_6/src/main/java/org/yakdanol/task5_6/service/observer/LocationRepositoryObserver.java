package org.yakdanol.task5_6.service.observer;

import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.model.entity.Location;
import org.yakdanol.task5_6.model.repository.LocationRepository;

@Component
public class LocationRepositoryObserver extends RepositoryObserver<Location> {

    public LocationRepositoryObserver(LocationRepository repository) {
        super(repository);
    }
}
