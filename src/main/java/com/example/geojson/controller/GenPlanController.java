package com.example.geojson.controller;

import com.example.geojson.service.GenPlanService;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/gp", produces = "application/vnd.geo+json;charset=UTF-8")
public class GenPlanController {

    private final GenPlanService genPlanService;

    public GenPlanController(GenPlanService genPlanService) {
        this.genPlanService = genPlanService;
    }

    @GetMapping
    public ResponseEntity<SimpleFeatureCollection> findAll() {
        return ResponseEntity.ok(genPlanService.findAll());
    }
}