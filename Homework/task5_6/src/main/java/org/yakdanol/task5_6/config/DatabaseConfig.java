package org.yakdanol.task5_6.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "org.yakdanol.task5_6.model.repository")
@EnableTransactionManagement
public class DatabaseConfig {
}
