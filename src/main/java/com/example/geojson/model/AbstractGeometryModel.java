package com.example.geojson.model;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class AbstractGeometryModel<T extends Geometry> {

    private static final int VALID_SRID = 4326;

    /**
     * geometry column
     */
    @Column(name = "wkb_geometry", columnDefinition = "geometry srid 4326")
    private T geometry;

    /**
     * Before persisting, we should change SRID, to avoid exception:
     * <pre>
     *     ERROR: Geometry SRID (0) does not match column SRID (4326)
     * </pre>
     * {@code columnDefinition} does not working properly, idk why
     */
    @PrePersist
    public void prePersist() {
        geometry.setSRID(VALID_SRID);
    }
}