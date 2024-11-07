package org.yakdanol.task5_6.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.service.LocationService;

@Component
public class InitializeLocationsCommand implements Command {

    private final LocationService locationService;

    @Autowired
    public InitializeLocationsCommand(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public void execute() {
        locationService.initializeLocationsFromExternalAPI();
    }
}
