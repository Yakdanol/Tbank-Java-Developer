package org.yakdanol.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yakdanol.homework.service.CityService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CityServiceTest {

    @Autowired
    private CityService cityService;

    @Test
    void testProcessValidCity() throws IOException {
        String jsonFilePath = "src/test/resources/city.json";
        String expectedXmlFilePath = "src/test/resources/city.xml";

        cityService.processJson(jsonFilePath);

        File xmlFile = new File(expectedXmlFilePath);
        assertTrue(xmlFile.exists(), "XML файл должен быть создан.");

        // Чтение и проверка содержимого XML файла
        String xmlContent = new String(Files.readAllBytes(xmlFile.toPath()));
        assertTrue(xmlContent.contains("<slug>spb</slug>"), "XML должен содержать slug spb");
        assertTrue(xmlContent.contains("<lat>59.939095</lat>"), "XML должен содержать lat 59.939095");
        assertTrue(xmlContent.contains("<lon>30.315868</lon>"), "XML должен содержать lon 30.315868");
    }

    @Test
    void testProcessInvalidCity() {
        String jsonFilePath = "src/test/resources/city_error.json";
        String expectedXmlFilePath = "src/test/resources/city_error.xml";

        cityService.processJson(jsonFilePath);

        File xmlFile = new File(expectedXmlFilePath);
        assertFalse(xmlFile.exists(), "XML файл не должен быть создан при ошибке в JSON.");
    }
}
