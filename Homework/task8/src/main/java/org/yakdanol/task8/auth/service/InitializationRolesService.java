package org.yakdanol.task8.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yakdanol.task8.auth.model.Role;
import org.yakdanol.task8.auth.repository.RoleRepository;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class InitializationRolesService {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        // Проверка, есть ли роли в базе данных
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "USER"));
            roleRepository.save(new Role(null, "ADMIN"));
        }
    }
}
