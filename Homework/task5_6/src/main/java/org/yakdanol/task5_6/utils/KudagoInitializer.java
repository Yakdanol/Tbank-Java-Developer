package org.yakdanol.task5_6.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yakdanol.task5_6.service.command.Command;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class KudagoInitializer {

    private final List<Command> commands;

    @Autowired
    public KudagoInitializer(List<Command> commands) {
        this.commands = commands;
    }

    @PostConstruct
    public void init() {
        System.out.println("Starting data initialization using Command pattern...");

        // Выполнение каждой команды из списка
        commands.forEach(Command::execute);

        System.out.println("Data initialization completed.");
    }
}
