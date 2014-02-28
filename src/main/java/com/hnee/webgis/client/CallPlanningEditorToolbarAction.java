package com.hnee.webgis.client;

import org.geomajas.gwt.client.action.ToolbarAction;
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

	public CallPlanningEditorToolbarAction(MapWidget mapWidget) {
		super(WidgetLayout.iconOpen, "Planungseditor",
				"Öffnet Planungseditor in modalem Dialog");
		this.mapWidget = mapWidget;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(ClickEvent event) {
		final Window window = createWindow();
		window.show();
	}

	private Window createWindow() {
		VLayout layout = new VLayout();  
        layout.setWidth100();  
        layout.setHeight100();
		final Window winModal = new Window();  
        winModal.setWidth(750);  
        winModal.setHeight(900);  
        winModal.setTitle("Modal Window");  
        winModal.setShowMinimizeButton(false);  
        winModal.setIsModal(true);  
        winModal.setShowModalMask(true);  
        winModal.centerInPage();  
        winModal.addCloseClickHandler(new CloseClickHandler() {  
            public void onCloseClick(CloseClickEvent event) {  
                winModal.destroy();  
            }  
        });
		final HTMLPane htmlPane = new HTMLPane();
		htmlPane.setShowEdges(true);
		htmlPane.setContentsURL("http://international.hnee.de:8080/hnee/planungseditor");
		htmlPane.setContentsType(ContentsType.PAGE);
		layout.addMember(htmlPane);  
        layout.draw();
		winModal.addItem(layout);
		// Return the dialog box
		return winModal;
	}
}
