package org.yakdanol.task2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yakdanol.task2.model.City;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class CityService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public void processJson(String jsonFilePath) {
        try {
            // Преобразование пути в абсолютный (если он не абсолютный)
            Path inputPath = Paths.get(jsonFilePath).toAbsolutePath();
            String jsonFilePathAbsolute = inputPath.toString();

            // Проверка, существует ли файл
            File jsonFile = new File(jsonFilePathAbsolute);
            if (!jsonFile.exists()) {
                log.error("Файл не найден: {}", jsonFilePathAbsolute);
                return;
            }

            String xmlOutputPath = jsonFilePathAbsolute.replace(".json", ".xml");
            City city = parseJson(jsonFilePathAbsolute);

            if (city != null) {
                String xmlContent = toXML(city);
                if (xmlContent != null) {
                    saveXmlToFile(xmlContent, xmlOutputPath);
                    log.info("XML успешно сохранен в файл: {}", xmlOutputPath);
                } else {
                    log.warn("Не удалось преобразовать объект City в XML.");
                }
            } else {
                log.warn("Не удалось обработать JSON.");
            }
        } catch (Exception e) {
            log.error("Ошибка при обработке файла: " + jsonFilePath, e);
        }
    }

    private City parseJson(String jsonFilePath) {
        try {
            City city = objectMapper.readValue(new File(jsonFilePath), City.class);
            log.info("JSON успешно распарсился в объект City: {}", city);
            return city;
        } catch (IOException e) {
            log.error("Ошибка при чтении или парсинге файла JSON.", e);
            return null;
        }
    }

    private String toXML(City city) {
        try {
            return xmlMapper.writeValueAsString(city);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при преобразовании объекта City в XML.", e);
            return null;
        }
    }

    private void saveXmlToFile(String xmlContent, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(xmlContent);
            log.debug("XML данные записаны в файл: {}", filePath);
        } catch (IOException e) {
            log.error("Ошибка при сохранении XML в файл.", e);
        }
    }
}

