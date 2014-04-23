package com.hnee.webgis.client.split;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.plugin.editing.client.GeometryArrayFunction;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;

public class FeatureSplitService extends GeometrySplitService {

	private Feature feature;

	public FeatureSplitService(GeometryEditService service) {
		super(service);
	}

	public void start(Feature feature) {
		this.feature = feature;
		super.start(GeometryConverter.toDto(feature.getGeometry()));
	}

	public void stop(final FeatureFunction callback) {
		super.stop(new GeometryArrayFunction() {

			@Override
			public void execute(Geometry[] geometries) {
				callback.execute(geometries, feature);
			}
		});
	}

}
