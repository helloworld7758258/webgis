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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.hnee.webgis.client.gui.SearchPanel;
import com.hnee.webgis.client.i18n.ApplicationMessages;
import com.hnee.webgis.client.merge.CancelMergeProcessButton;
import com.hnee.webgis.client.merge.ExecuteMergeButton;
import com.hnee.webgis.client.merge.StartMergeProcessButton;
import com.hnee.webgis.client.split.CancelSplitProcessButton;
import com.hnee.webgis.client.split.ExecuteSplitButton;
import com.hnee.webgis.client.split.StartSplitProcessButton;
import com.hnee.webgis.client.widget.MenuBar;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.Legend;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.widget.layer.client.widget.CombinedLayertree;

/**
 * Entry point and main class for GWT application. This class defines the layout
 * and functionality of this application.
 *
 * @author Oliver Bienert
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

        final MapWidget map = new MapWidget("mapMain", "app");
        final Toolbar toolbar = new Toolbar(map);

        GeometryEditorImpl editor = new GeometryEditorImpl(map);
        GeometrySplitService splitService = new GeometrySplitService(editor.getEditService());
        GeometryMergeService mergingService = new GeometryMergeService();
        mergingService.setPrecision(1);

        HLayout mainLayout = new HLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight100();
        mainLayout.setMembersMargin(5);
        mainLayout.setMargin(5);

        // ---------------------------------------------------------------------
        // Create the left-side (overview map, layer-tree, legend):
        // ---------------------------------------------------------------------
        final SectionStack sectionStack = new SectionStack();
        sectionStack.setBorder("2px solid #455469");
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setCanReorderSections(true);
        sectionStack.setCanResizeSections(true);
        sectionStack.setWidth(230);
        sectionStack.setHeight("100%");

        // LayerTree layout:
        SectionStackSection section1 = new SectionStackSection(
                messages.layerTreeTitle());
        section1.setExpanded(true);
        section1.setCanCollapse(true);
        CombinedLayertree layerTree = new CombinedLayertree(map);
        section1.addItem(layerTree);
        sectionStack.addSection(section1);

        // ---------------------------------------------------------------------
        // Create the right-side (data editor):
        // ---------------------------------------------------------------------
        final HTMLPane htmlPane = new HTMLPane();
        htmlPane.setShowEdges(true);
        htmlPane.setContentsURL("http://localhost:8080/hnee/planungseditor?geoobjects");
        htmlPane.setContentsType(ContentsType.PAGE);

        VLayout centerLayout = new VLayout();
        centerLayout.setBorder("2px solid #455469");
        centerLayout.setWidth("*");
        centerLayout.setShowResizeBar(true);

        // ---------------------------------------------------------------------
        // Create a split toolstrip
        // ---------------------------------------------------------------------
        ToolStrip toolStripSplit = new ToolStrip();
        toolStripSplit.setMembersMargin(2);
        toolStripSplit.setWidth100();
        toolStripSplit.addButton(new StartSplitProcessButton(splitService, map));
        toolStripSplit.addSeparator();
        toolStripSplit.addButton(new ExecuteSplitButton(map, splitService));
        toolStripSplit.addButton(new CancelSplitProcessButton(map, splitService));

        // ---------------------------------------------------------------------
        // Create a merge toolstrip
        // ---------------------------------------------------------------------
        ToolStrip toolStripMerge = new ToolStrip();
        toolStripMerge.setMembersMargin(2);
        toolStripMerge.setWidth100();
        toolStripMerge.addButton(new StartMergeProcessButton(map, mergingService));
        toolStripMerge.addSeparator();
        toolStripMerge.addButton(new ExecuteMergeButton(map, mergingService));
        toolStripMerge.addButton(new CancelMergeProcessButton(map, mergingService));

        // ---------------------------------------------------------------------
        // Create an editing toolstrip (adding points and lines)
        // ---------------------------------------------------------------------
        MenuBar editingToolStrip = new MenuBar(editor);

        // Set toolbar options
        toolbar.setButtonSize(WidgetLayout.toolbarSmallButtonSize);
        toolbar.setBackgroundColor("#647386");
        toolbar.setBackgroundImage("");
        toolbar.setBorder("0px");

        // ---------------------------------------------------------------------
        // Create the map layout (map and tabs):
        // ---------------------------------------------------------------------
        VLayout mapLayout = new VLayout();

        // Add a custom action button
        final CallPlanningEditorToolbarAction action = new CallPlanningEditorToolbarAction(
                map, htmlPane);

        map.getMapModel().runWhenInitialized(new Runnable() {

            @Override
            public void run() {
                toolbar.addActionButton(action);
                toolbar.addFill();

            }
        });

        mapLayout.addMember(map);
        mapLayout.setWidth100();

        centerLayout.addMember(toolbar);
        centerLayout.addMember(mapLayout);
        centerLayout.addMember(editingToolStrip);
        centerLayout.addMember(toolStripSplit);
        centerLayout.addMember(toolStripMerge);

        mainLayout.addMember(sectionStack);
        mainLayout.addMember(centerLayout);
        mainLayout.addMember(htmlPane);

		// Add a search panel to the top-right of the map:
		SearchPanel searchPanel = new SearchPanel(map.getMapModel(), mapLayout);
		mapLayout.addChild(searchPanel);

        // ---------------------------------------------------------------------
        // Finally draw everything:
        // ---------------------------------------------------------------------
        mainLayout.draw();

        // Install a loading screen.
        // This only works if the application initially shows a map with at
        // least 1 vector layer:
        // LoadingScreen loadScreen = new LoadingScreen(map,
        // "Geomajas GWT application");
        // loadScreen.draw();

        // Then initialize:
        // initialize();
    }

//	private void initialize() {
//
//	}

}
