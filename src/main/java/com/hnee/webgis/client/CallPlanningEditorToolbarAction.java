package com.hnee.webgis.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CallPlanningEditorToolbarAction extends ToolbarAction {

	private MapWidget mapWidget;
	private HTMLPane htmlPane;

	public CallPlanningEditorToolbarAction(MapWidget mapWidget,
			HTMLPane htmlPane) {
		super(WidgetLayout.iconTable, "Planungseditor",
				"Aktualisiere Planungsdialog mit ausgewählten Features");
		this.mapWidget = mapWidget;
		this.htmlPane = htmlPane;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(ClickEvent event) {
		String url = "http://localhost:8889/planungseditor";
		List<Feature> features = new ArrayList<Feature>();
		String geoobjects = "";
		for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
			features.addAll(layer.getSelectedFeatureValues());
		}
		// only zoom when there where really some items selected

		if (features.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for (Feature feature : features) {
				ids.add(feature.getId());
			}
			geoobjects = join(ids, ",");
			url = url + "?geoobjects=" + geoobjects;
			htmlPane.setContentsURL(url);
		}
	}

	public static String join(Iterable<?> elements, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (Object e : elements) {
			if (sb.length() > 0)
				sb.append(delimiter);
			sb.append(e);
		}
		return sb.toString();
	}

}
