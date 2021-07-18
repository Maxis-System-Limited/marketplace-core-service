package maxis.serviceproperty.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import maxis.serviceproperty.model.ModelServiceProperty;
import maxis.serviceproperty.service.ServiceServiceProperty;

@RestController
@RequestMapping("/api")
public class ControllerServiceProperty {
	@Autowired
	private ServiceServiceProperty serviceServiceProperty;

	@RequestMapping(value = "/serviceproperty", method = RequestMethod.POST)
	public HttpEntity<ModelServiceProperty> add(@RequestBody ModelServiceProperty modelServiceProperty) {
		try {
			ModelServiceProperty res = serviceServiceProperty.add(modelServiceProperty);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/serviceproperty", method = RequestMethod.GET)
	public HttpEntity<List<ModelServiceProperty>> get() {
		try {
			List<ModelServiceProperty> res = serviceServiceProperty.getList();
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
