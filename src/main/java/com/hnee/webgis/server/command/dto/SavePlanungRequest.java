package com.hnee.webgis.server.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

public class SavePlanungRequest implements CommandRequest {

	public static final  String COMMAND = "SavePlanungCommand";

	private Geometry geometry;

	private Long id;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
