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

package com.hnee.webgis.client.widget;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.hnee.webgis.client.event.GeometryEditSuspendResumeHandler;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;

/**
 * The editing toolbar used within this sample application.
 * 
 * @author Pieter De Graef
 */
public class MenuBar extends ToolStrip {

	private GeometryEditor editor;

	private MapWidget map;

	private EventBus eventBus;

	private SnappingOptionWindow wnd;

	public MenuBar(GeometryEditor editor) {
		this.editor = editor;
		eventBus = new SimpleEventBus();
		map = editor.getMapWidget();
		setWidth100();
		setHeight(36);

		// Add creation options:
		addMenuButton(getNewGeometryButton());
		addSeparator();

		// Add buttons to help the editing process:
		addButton(new CancelEditingBtn(editor.getEditService()));

		UndoBtn undoBtn = new UndoBtn(editor.getEditService());
		addGeometryEditSuspensionHandler(undoBtn);
		addButton(undoBtn);

		RedoBtn redoBtn = new RedoBtn(editor.getEditService());
		addGeometryEditSuspensionHandler(redoBtn);
		addButton(redoBtn);

		addSeparator();

		ToolStripButton snappingBtn = new ToolStripButton("Snapping options");
		snappingBtn.setHeight(32);
		snappingBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			public void onClick(ClickEvent event) {
				if (wnd == null) {
					wnd = new SnappingOptionWindow(MenuBar.this.editor);
				}
				wnd.show();
			}
		});
		addButton(snappingBtn);
	}

	public HandlerRegistration addGeometryEditSuspensionHandler(GeometryEditSuspendResumeHandler handler) {
		return eventBus.addHandler(GeometryEditSuspendResumeHandler.TYPE, handler);
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	protected ToolStripMenuButton getNewGeometryButton() {
		Menu menu = new Menu();
		menu.setShowShadow(true);
		menu.setShadowDepth(3);

		MenuItem pointItem = new MenuItem("Draw Point", "[ISOMORPHIC]/geomajas/osgeo/point-create.png");
		pointItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry point = new Geometry(Geometry.POINT, 0, 0);
				GeometryIndex index = editor.getEditService().getIndexService()
						.create(GeometryIndexType.TYPE_VERTEX, 0);

				editor.getEditService().start(point);
				editor.getEditService().setInsertIndex(index);
				editor.getEditService().setEditingState(GeometryEditState.INSERTING);
			}
		});
		MenuItem lineItem = new MenuItem("Draw Line", "[ISOMORPHIC]/geomajas/osgeo/line-create.png");
		lineItem.addClickHandler(new ClickHandler() {

			public void onClick(MenuItemClickEvent event) {
				Geometry line = new Geometry(Geometry.LINE_STRING, 0, 0);
				GeometryIndex index = editor.getEditService().getIndexService()
						.create(GeometryIndexType.TYPE_VERTEX, 0);

				editor.getEditService().start(line);
				editor.getEditService().setInsertIndex(index);
				editor.getEditService().setEditingState(GeometryEditState.INSERTING);
			}
		});

		menu.setItems(pointItem, lineItem);

		ToolStripMenuButton menuButton = new ToolStripMenuButton("Free drawing", menu);
		menuButton.setWidth(100);
		menuButton.setHeight(32);
		return menuButton;
	}

}