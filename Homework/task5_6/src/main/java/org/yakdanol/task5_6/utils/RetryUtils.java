package org.yakdanol.task5_6.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryUtils {

    /**
     * Метод для выполнения задачи с повторными попытками в случае ошибки.
     *
     * @param task       задача, которая должна быть выполнена
     * @param maxRetries максимальное количество попыток
     * @param taskName   имя задачи для логирования
     * @param <T>        тип результата задачи
     */
    public static <T> void retryWithMaxAttempts(Supplier<T> task, int maxRetries, String taskName) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                log.info("Attempting to execute task: {}", taskName);
                task.get();
                return;
            } catch (Exception e) {
                retryCount++;
                log.error("Error executing task '{}'. Attempt {} of {}. Error: {}", taskName, retryCount, maxRetries, e.getMessage());
                if (retryCount >= maxRetries) {
                    log.error("Failed to execute task '{}' after {} attempts", taskName, maxRetries);
                }
            }
        }

    }
}
