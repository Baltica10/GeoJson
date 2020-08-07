package com.example.geojson.repository;

import com.example.geojson.model.GenPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GenPlanRepository extends JpaRepository<GenPlan, Long> {
}