package com.hnee.webgis.client.split;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.map.feature.Feature;


public interface FeatureFunction {

	void execute(Geometry[] geometries, Feature feature);

}
