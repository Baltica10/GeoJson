package com.example.geojson.converter;

import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.nio.charset.*;

/**
 * Абстрактный класс, куда вынесен весь общий код для конвертеров {@code geojson}
 */
public abstract class AbstractConverter<T> extends AbstractHttpMessageConverter<T> {

    /**
     * Точность координат, сколько знаков после запятой
     */
    private static final int DECIMAL_PLACES = 13;

    /**
     * Утилита, которая используется при конвертации геометрии
     */
    final GeometryJSON geometryJSON = new GeometryJSON(DECIMAL_PLACES);

    /**
     * Утилита, которая используется при конвертации {@code feature}
     */
    final FeatureJSON featureJSON;

    /**
     * Кодировка
     */
    static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Конструктор
     */
    public AbstractConverter() {
        super(CHARSET, new MediaType("application", "vnd.geo+json", CHARSET));
        featureJSON = new FeatureJSON(geometryJSON);
        featureJSON.setEncodeNullValues(true);
    }
}
