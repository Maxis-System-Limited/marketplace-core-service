package maxis.servicetype.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import maxis.servicetype.model.ModelServiceType;
import maxis.servicetype.service.ServiceServiceType;

@RestController
@RequestMapping("/api")
public class ControllerServiceType {
	@Autowired
	private ServiceServiceType serviceServiceType;

	@RequestMapping(value = "/service-type", method = RequestMethod.POST)
	public HttpEntity<ModelServiceType> add(@RequestBody ModelServiceType modelServiceType) {
		try {
			ModelServiceType res = serviceServiceType.add(modelServiceType);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/service-type", method = RequestMethod.GET)
	public HttpEntity<List<ModelServiceType>> get() {
		try {
			List<ModelServiceType> res = serviceServiceType.getList();
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
