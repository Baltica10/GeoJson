package com.example.geojson.service;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Абстрактный сервис, для всех новых сервисов, которая работает с {@link SimpleFeatureCollection} и
 * {@link SimpleFeature}
 *
 * @param <T> model type
 * @param <I> key type
 */
public abstract class AbstractService<T, I> {

    public final JpaRepository<T, I> repository;

    public AbstractService(JpaRepository<T, I> repository) {
        this.repository = repository;
    }

    /**
     * get basic builder
     *
     * @param name name of feature
     * @return simple feature builder
     */
    public SimpleFeatureTypeBuilder getBuilder(String name) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(name);
        builder.setDefaultGeometry("geometry");

        return builder;
    }

    /**
     * Получение списка полей, которые будут включены в properties внутри GeoJSON
     *
     * @return мапа, где ключ это название поля, а значение тип данных
     */
    public abstract Map<String, Class<?>> getFields();

    public abstract SimpleFeature modelToSimpleFeature(T t);

    /**
     * fetch schema for {@link SimpleFeature} and {@link SimpleFeatureCollection}. It will built only once, on first
     * request, then will use this instance
     *
     * @return suitable schema
     */
    public SimpleFeatureType getSchema() {
        final SimpleFeatureTypeBuilder builder = getBuilder(getName());
        final Map<String, Class<?>> fields = getFields();
        fields.forEach(builder::add);
        return builder.buildFeatureType();
    }

    public SimpleFeatureCollection collectionToSimpleFeatureCollection(Collection<T> collection) {
        final List<SimpleFeature> collect = collection.stream()
                .map(this::modelToSimpleFeature)
                .collect(Collectors.toList());

        return DataUtilities.collection(collect);
    }

    /**
     * Получение названия feature
     *
     * @return название feature
     */
    public abstract String getName();

    public SimpleFeatureCollection findAll() {
        final List<T> all = repository.findAll();
        return collectionToSimpleFeatureCollection(all);
    }

}