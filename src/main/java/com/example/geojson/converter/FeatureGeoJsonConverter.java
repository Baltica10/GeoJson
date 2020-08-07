package com.example.geojson.converter;

import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;

/**
 * Конвертер для перевода объектов {@link SimpleFeature} из/в {@code geojson}
 * при получении запросе или отправке ответа
 */
@Slf4j
@Component
public class FeatureGeoJsonConverter extends AbstractConverter<SimpleFeature> {

    /**
     * Проверка, поддерживает ли переданный класс {@param clazz} данный конвертер
     *
     * @param clazz класс которую нужно проверить
     * @return {@code true} если подходит, иначе {@code false}
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return SimpleFeature.class.isAssignableFrom(clazz);
    }

    /**
     * Чтение данных из потока {@param inputMessage} и конвертация в {@link SimpleFeature}
     *
     * @param clazz        класс {@link SimpleFeature}
     * @param inputMessage поток из которого считываются данные
     * @return сконвертированные данные
     * @throws IOException при ошибке во время чтения
     */
    @Override
    protected SimpleFeature readInternal(Class<? extends SimpleFeature> clazz, HttpInputMessage inputMessage) throws IOException {
        log.debug("Converting GoeJSON to SimpleFeature object");
        String result = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), CHARSET));
        return featureJSON.readFeature(result);
    }

    /**
     * Конвертация данных в {@code GeoJSON} и запись в исходящии поток {@param outputMessage}
     *
     * @param simpleFeature данные, которые необходимо сконвертировать и отправить
     * @param outputMessage исходящии поток, в которую необходимо записать данные
     * @throws IOException                     ошибка при записи
     * @throws HttpMessageNotWritableException ошибка во время записи
     */
    @Override
    protected void writeInternal(SimpleFeature simpleFeature, HttpOutputMessage outputMessage) throws IOException {
        log.debug("Converting SimpleFeature object to GeoJSON");
        String result = featureJSON.toString(simpleFeature);
        FileCopyUtils.copy(result, new OutputStreamWriter(outputMessage.getBody(), CHARSET));
    }
}
