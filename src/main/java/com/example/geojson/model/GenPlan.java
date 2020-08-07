package com.example.geojson.model;

import lombok.*;
import org.locationtech.jts.geom.MultiPolygon;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "genplan_2035_test_view")
public class GenPlan extends AbstractGeometryModel<MultiPolygon> {

    @Id
    @GeneratedValue
    @Column(name = "fid")
    private Long id;

    @Column(name = "area")
    private Float area;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "changed")
    private LocalDateTime changed;

}