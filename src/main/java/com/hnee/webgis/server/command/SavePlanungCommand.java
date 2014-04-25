package com.hnee.webgis.server.command;

import org.geomajas.command.Command;
import org.geomajas.service.DtoConverterService;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hnee.webgis.server.MPlanung;
import com.hnee.webgis.server.command.dto.SavePlanungRequest;
import com.hnee.webgis.server.command.dto.SavePlanungResponse;

@Transactional
@Component(SavePlanungRequest.COMMAND)
public class SavePlanungCommand implements Command<SavePlanungRequest, SavePlanungResponse> {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DtoConverterService converterService;
	
	private Logger log = LoggerFactory.getLogger(SavePlanungCommand.class);

	@Override
	public SavePlanungResponse getEmptyCommandResponse() {
		return new SavePlanungResponse();
	}

	@Override
	public void execute(SavePlanungRequest request, SavePlanungResponse response) throws Exception {
		MPlanung planung = (MPlanung) sessionFactory.getCurrentSession().load(MPlanung.class, request.getId());
		planung.setGeometry(converterService.toInternal(request.getGeometry()));
		log.info("Updated geometry for planung "+planung.getName());
	}

}
