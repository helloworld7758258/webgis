/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package com.hnee.webgis.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.hnee.webgis.client.i18n.ApplicationMessages;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout
 * and functionality of this application.
 * 
 * @author geomajas-gwt-archetype
 */
public class Application implements EntryPoint {

	private Legend legend;

	private ApplicationMessages messages = GWT
			.create(ApplicationMessages.class);

	public Application() {
	}

	@Override
	public void onModuleLoad() {
		WidgetLayout.legendVectorRowHeight = 10;
		WidgetLayout.legendRasterRowHeight = 10;

		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setBackgroundColor("#A0A0A0");

		HLayout layout = new HLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);
		layout.setMargin(10);

		// ---------------------------------------------------------------------
		// Create the left-side (map and tabs):
		// ---------------------------------------------------------------------
		final MapWidget map = new MapWidget("mapMain", "app");
		final Toolbar toolbar = new Toolbar(map);

		toolbar.setButtonSize(WidgetLayout.toolbarSmallButtonSize);
		toolbar.setBackgroundColor("#647386");
		toolbar.setBackgroundImage("");
		toolbar.setBorder("0px");

		map.getMapModel().runWhenInitialized(new Runnable() {

			@Override
			public void run() {
				// Add a custom action button
				CallPlanningEditorToolbarAction action = new CallPlanningEditorToolbarAction(
						map);
				toolbar.addActionButton(action);

				Label title = new Label("GWT GeoMajas GIS Editor");
				title.setStyleName("appTitle");
				title.setWidth(260);
				toolbar.addFill();
				toolbar.addMember(title);
			}
		});

		VLayout mapLayout = new VLayout();
		mapLayout.addMember(toolbar);
		mapLayout.addMember(map);
		mapLayout.setHeight("65%");

		VLayout leftLayout = new VLayout();
		leftLayout.setBorder("10px solid #777777");
		leftLayout.setStyleName("round_corner");
		leftLayout.addMember(mapLayout);

		layout.addMember(leftLayout);

		// ---------------------------------------------------------------------
		// Create the right-side (overview map, layer-tree, legend):
		// ---------------------------------------------------------------------
		final SectionStack sectionStack = new SectionStack();
		sectionStack.setBorder("10px solid #777777");
		sectionStack.setStyleName("round_corner");
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setCanReorderSections(true);
		sectionStack.setCanResizeSections(false);
		sectionStack.setSize("250px", "100%");

		// LayerTree layout:
		SectionStackSection section1 = new SectionStackSection(
				messages.layerTreeTitle());
		section1.setExpanded(true);
		LayerTree layerTree = new LayerTree(map);
		section1.addItem(layerTree);
		sectionStack.addSection(section1);

		// Legend layout:
		SectionStackSection section2 = new SectionStackSection(
				messages.legendTitle());
		section2.setExpanded(true);
		legend = new Legend(map.getMapModel());
		legend.setBackgroundColor("#FFFFFF");
		section2.addItem(legend);
		sectionStack.addSection(section2);

		// Putting the right side layouts together:
		layout.addMember(sectionStack);

		// ---------------------------------------------------------------------
		// Finally draw everything:
		// ---------------------------------------------------------------------
		mainLayout.addMember(layout);
		mainLayout.draw();

		// Install a loading screen.
		// This only works if the application initially shows a map with at
		// least 1 vector layer:
		// LoadingScreen loadScreen = new LoadingScreen(map,
		// "Geomajas GWT application");
		// loadScreen.draw();

		// Then initialize:
		initialize();
	}

	private void initialize() {
		legend.setHeight(200);
	}
	
	private void zoomToLayer(final MapWidget map) {
		VectorLayer layer = map.getMapModel().getVectorLayer("clientLayerPlanningAreas");
		VectorLayerStore featureStore = layer.getFeatureStore();
		featureStore.getFeatures(GeomajasConstant.FEATURE_INCLUDE_GEOMETRY,
				new LazyLoadCallback() {

					@Override
					public void execute(List<Feature> response) {
						boolean success = false;
						List<Feature> features = new ArrayList<Feature>();
						for (Feature feature : response) {
							features.add(feature);
							success = true;
						}

						// only pan when their where really some items selected
						if (success) {
							map.getMapModel().zoomToFeatures(features);
						}
					}
				});
		
	}
}
