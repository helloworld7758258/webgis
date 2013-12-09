package com.hnee.webgis.server;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Geoobjects planning lines object for hibernate layer model.
 *
 * @author oliver
 */
@Entity
@Table(name = "GEOOBJECT_PL")
public class Geoobject_pl {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "GID")
	private Long id;
	
	@Column(name="NAME")
	private String name;
	
	@Type(type = "org.hibernatespatial.GeometryUserType")
	@Column(name="GEOM")
	private Geometry geometry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
