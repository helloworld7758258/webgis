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

import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStartHandler;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopEvent;
import org.geomajas.plugin.editing.client.split.event.GeometrySplitStopHandler;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Toolbar button for adding inner rings to a polygon.
 * 
 * @author Pieter De Graef
 */
public class StartSplitProcessButton extends ToolStripButton implements GeometrySplitStartHandler,
		GeometrySplitStopHandler {

	private final MapWidget mapWidget;

	private final FeatureSplitService service;

	private GraphicsController previousController;

	private Label label;
	
	private HandlerRegistration registration;

	public StartSplitProcessButton(final FeatureSplitService service, final MapWidget mapWidget) {
		this.service = service;
		this.mapWidget = mapWidget;

		setHoverWidth(300);
		setTitle("Start splitting process");
		setTooltip("Select an area on the map. Then draw a splitting line.");
		setIconSize(24);
		setHeight(32);

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				previousController = mapWidget.getController();
				// JAN: i moved the feature selection here as it was called twice
				registration = mapWidget.getMapModel().addFeatureSelectionHandler(new FeatureSelectionHandler() {

					public void onFeatureSelected(FeatureSelectedEvent event) {
		                Feature feature = event.getFeature();
		                mapWidget.setController(previousController);
		                previousController = null;
		                service.start(feature);

		                // Now let the user know:
		                if (label != null) {
		                    label.destroy();
		                    label = null;
		                }

		                label = new Label("<p><b>You have selected " + feature.getLabel() + "!</b></p><p>Now continue by drawing a "
		                        + "splitting line on the map. This is done by clicking points on the map, ending with a double " +
		                        "click.</p><p>Finally choose an option in the toolbar (finish, cancel, undo, ...).</p>");
		                label.setParentElement(mapWidget);
		                label.setShowEdges(true);
		                label.setBackgroundColor("#ffffd0");
		                label.setPadding(5);
		                label.setWidth(350);
		                label.setTop(25);
		                label.setLeft(-220); // start off screen
		                label.setValign(VerticalAlignment.CENTER);
		                label.setAlign(Alignment.CENTER);
		                label.setAnimateTime(1000); // milliseconds
		                label.addClickHandler(new ClickHandler() {

		                    public void onClick(ClickEvent event) {
		                        label.destroy();
		                    }
		                });

		                label.animateMove(75, 25);
		                // deregister when done !
		                registration.removeHandler();
		            }

		            public void onFeatureDeselected(FeatureDeselectedEvent event) {
		            }


		        });
				mapWidget.setController(new SelectForSplitController(mapWidget));
			}
		});

		service.addGeometrySplitStartHandler(this);
		service.addGeometrySplitStopHandler(this);


    }

	// ------------------------------------------------------------------------
	// FeatureSelectionHandler:
	// ------------------------------------------------------------------------

	public void onGeometrySplitStart(GeometrySplitStartEvent event) {
		setDisabled(true);
	}

	public void onGeometrySplitStop(GeometrySplitStopEvent event) {
		if (label != null) {
			label.destroy();
			label = null;
		}
		setDisabled(false);
	}
}