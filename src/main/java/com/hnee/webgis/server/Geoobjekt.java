package com.hnee.webgis.server;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "GEOOBJEKT")
@SecondaryTable(name = "GEOMETRY_TYPE", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "GISPADID") })
public class Geoobjekt {
	
	// Fields

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "GISPADID")
	private Long id;

	@Column(name = "OBJEKTNAME")
	private String name;

	@Column(name = "GTYP", nullable = false)
	private Integer gtyp;

	@Column(table = "GEOMETRY_TYPE", name = "GEOMETRYTYPE")
	private String geometryType;

	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name = "GEOM")
	private Geometry geometry;
	
	@OneToOne(mappedBy="geoobjekt", cascade=CascadeType.ALL)
	private Linfos linfos;
	
	// Constructors

	/** default constructor */
	public Geoobjekt() {
	}

	// Property accessors
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGtyp() {
		return gtyp;
	}

	public void setGtyp(Integer gtyp) {
		this.gtyp = gtyp;
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

	public Linfos getLinfos() {
		return linfos;
	}

	public void setLinfos(Linfos linfos) {
		this.linfos = linfos;
	}

}
