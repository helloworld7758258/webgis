package com.hnee.webgis.server;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "M_PLANUNG")
@SecondaryTables({
        @SecondaryTable(name = "MEASURES_COUNT", pkJoinColumns = {
                @PrimaryKeyJoinColumn(name = "FKEY", referencedColumnName = "PKEY")}),
        @SecondaryTable(name = "GEOMETRY_TYPE", pkJoinColumns = {
                @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "PKEY") })
})
public class MPlanung {

    // Fields

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "PKEY", unique = true, nullable = false)
    private Long id;

    @Column(name = "PK_PIDENT")
    private String name;

    @Column(name="FK_ZIEL_P")
    private String ziel_p;

    @Column(name="GEBNRA")
    private String gebnra;

    @Type(type = "org.hibernatespatial.GeometryUserType")
    @Column(name = "GEOM")
    private Geometry geometry;

    @Column(table = "MEASURES_COUNT", name = "CNT")
    private Long measurecount;

    @Column(table = "GEOMETRY_TYPE", name = "GEOMETRYTYPE")
    private String geometryType;

    // Constructors

    /**
     * default constructor
     */
    public MPlanung() {
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeasurecount() {
        return measurecount;
    }

    public void setMeasurecount(Long measurecount) {
        this.measurecount = measurecount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZiel_p() {
        return ziel_p;
    }

    public void setZiel_p(String ziel_p) {
        this.ziel_p = ziel_p;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public String getGebnra() {
        return gebnra;
    }

    public void setGebnra(String gebnra) {
        this.gebnra = gebnra;
    }
}