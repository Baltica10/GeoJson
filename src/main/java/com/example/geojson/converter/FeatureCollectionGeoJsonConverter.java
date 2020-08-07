package com.example.geojson.converter;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;

/**
 * Конвертер для перевода из {@link SimpleFeatureCollection} из/в {@code GeoJSON} при получении
 * запросов от пользователя или при отправке ответа
 */
@Slf4j
@Component
public class FeatureCollectionGeoJsonConverter extends AbstractConverter<FeatureCollection<?, ?>> {

    /**
     * Correct empty feature collection pattern, that expected from us, not {@code []}
     */
    private static final String EMPTY_FEATURE_COLLECTION_VALUE = "{" +
            "\"type\": \"FeatureCollection\"," +
            "\"features\": []" +
            "}";

    /**
     * Проверка, поддерживает ли переданный класс {@param clazz} данный конвертер
     *
     * @param clazz класс которую нужно проверить
     * @return {@code true} если подходит, иначе {@code false}
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return SimpleFeatureCollection.class.isAssignableFrom(clazz);
    }

    /**
     * Чтение данных из потока {@param inputMessage} и конвертация данных в {@link SimpleFeatureCollection}
     *
     * @param clazz        класс {@link SimpleFeatureCollection}
     * @param inputMessage поток, из которого считываются данные
     * @return сконвертированные данные
     * @throws IOException                     ошибка пр чтении
     * @throws HttpMessageNotReadableException ошибка при чтении
     */
    @Override
    protected FeatureCollection<?, ?> readInternal(Class<? extends FeatureCollection<?, ?>> clazz, HttpInputMessage inputMessage) throws IOException {
        // reading directly from input stream returns null, idk why
        String result = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody(), CHARSET));
        return featureJSON.readFeatureCollection(result);
    }

    /**
     * Конвертация данных в {@code GeoJSON} и запись в исходящии поток {@param outputMessage}
     *
     * @param featureCollection данные, которые необходимо сконветировать и отправить
     * @param outputMessage     исходящии поток, в которую необходимо записать данные
     * @throws IOException                     ошибка при записи
     * @throws HttpMessageNotWritableException ошибка при записи
     */
    @Override
    protected void writeInternal(FeatureCollection featureCollection, HttpOutputMessage outputMessage) throws IOException {
        try {
            if (featureCollection.isEmpty()) {
                FileCopyUtils.copy(EMPTY_FEATURE_COLLECTION_VALUE, new OutputStreamWriter(outputMessage.getBody(), CHARSET));
                return;
            }
            log.debug("Writing feature collection: {}", featureCollection);
            featureJSON.writeFeatureCollection(featureCollection, outputMessage.getBody());
        } catch (IOException ex) {
            log.warn("IOException occurred while writing data. Exception message: '{}', layer: '{}'", ex.getMessage(), featureCollection.getSchema().getName());
            if (log.isDebugEnabled()) {
                log.error("Exception trace: ", ex);
                throw ex;
            }
        }
    }
}