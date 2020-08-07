package com.example.geojson.service;

import com.example.geojson.model.GenPlan;
import com.example.geojson.repository.GenPlanRepository;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LivingServiceImpl extends AbstractService<GenPlan, Long> implements GenPlanService {

    private final GenPlanRepository genPlanRepository;

    public LivingServiceImpl(GenPlanRepository genPlanRepository) {
        super(genPlanRepository);
        this.genPlanRepository = genPlanRepository;
    }

    @Override
    public Map<String, Class<?>> getFields() {
        Map<String, Class<?>> fields = new HashMap<>();
        fields.put("area", Float.class);
        fields.put("created", LocalDateTime.class);
        fields.put("changed", LocalDateTime.class);
        fields.put("geometry", MultiPolygon.class);
        return fields;
    }

    @Override
    public SimpleFeature modelToSimpleFeature(GenPlan genPlan) {
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(getSchema());

        builder.set("geometry", genPlan.getGeometry());
        builder.set("created", genPlan.getCreated());
        builder.set("changed", genPlan.getChanged());
        builder.set("area", genPlan.getArea());

        return builder.buildFeature(String.valueOf(genPlan.getId()));
    }

    @Override
    public String getName() {
        return "genplan_2035";
    }

    @Override
    public SimpleFeatureCollection findAll() {
        List<GenPlan> genPlanList = genPlanRepository.findAll();

        return collectionToSimpleFeatureCollection(genPlanList);
    }
}