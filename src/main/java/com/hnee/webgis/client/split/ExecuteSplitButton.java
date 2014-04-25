/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package com.hnee.webgis.client.split;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartHandler;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopHandler;

import com.hnee.webgis.server.command.dto.SavePlanungRequest;
import com.hnee.webgis.server.command.dto.SavePlanungResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Button that executes the split calculation and displays the result on the map.
 * 
 * @author Pieter De Graef
 */
public class ExecuteSplitButton extends ToolStripButton implements GeometrySplitStartHandler, GeometrySplitStopHandler {

	private final List<GfxGeometry> splitResult;

	private final MapWidget mapWidget;

	public ExecuteSplitButton(final MapWidget mapWidget, final FeatureSplitService service) {
		this.mapWidget = mapWidget;
		this.splitResult = new ArrayList<GfxGeometry>();

		setIcon("[ISOMORPHIC]/geomajas/osgeo/split.png");
		setIconSize(24);
		setHeight(32);
		setDisabled(true);
		setHoverWrap(false);
		setTooltip("Split the selected area now.");
		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				service.stop(new FeatureFunction() {

					public void execute(Geometry[] geometries, Feature feature) {
						// Now display the result on the map:
						mapWidget.getMapModel().clearSelectedFeatures();

//						for (int i = 0; i < geometries.length; i++) {
//							GfxGeometry gfx = new GfxGeometry("split-result-" + i, GeometryConverter
//									.toGwt(geometries[i]), new ShapeStyle("#CC0000", .9f, "#660000", 1.0f, 3));
//							mapWidget.registerWorldPaintable(gfx);
//							splitResult.add(gfx);
//						}
						// JAN: here you can save the geometry by executing a server command !!!
						SavePlanungRequest request = new SavePlanungRequest();
						request.setId(Long.decode(feature.getId()));
						Geometry geometry = new Geometry(Geometry.MULTI_POLYGON, 0, 5);
						geometry.setGeometries(geometries);
						request.setGeometry(geometry);
						GwtCommand command = new GwtCommand(SavePlanungRequest.COMMAND);
						command.setCommandRequest(request);
						GwtCommandDispatcher.getInstance().execute(command,
								new AbstractCommandCallback<SavePlanungResponse>() {

									@Override
									public void execute(SavePlanungResponse response) {
										// refresh the layers	
										mapWidget.getMapModel().refresh();
									}
								});
					}
				});
			}
		});
		service.addGeometrySplitStartHandler(this);
		service.addGeometrySplitStopHandler(this);
	}

	// ------------------------------------------------------------------------
	// GeometryEditWorkflowHandler implementation:
	// ------------------------------------------------------------------------

	public void onGeometrySplitStart(GeometrySplitStartEvent event) {
		setDisabled(false);

		for (GfxGeometry gfx : splitResult) {
			mapWidget.unregisterWorldPaintable(gfx);
		}
		splitResult.clear();
	}

	public void onGeometrySplitStop(GeometrySplitStopEvent event) {
		setDisabled(true);
	}
}