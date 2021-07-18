package maxis.configurationmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import maxis.configurationmanagement.model.FilterConfigurationManagement;
import maxis.configurationmanagement.model.ModelConfigurationManagement;
import maxis.configurationmanagement.service.ServiceConfigurationManagement;

@RestController
@RequestMapping("/configurationmanagement")
public class ControllerConfigurationManagement {
	@Autowired
	private ServiceConfigurationManagement serviceConfigurationManagement;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelConfigurationManagement> add(@RequestBody ModelConfigurationManagement modelConfigurationManagement) {
		try {
			ModelConfigurationManagement res = serviceConfigurationManagement.add(modelConfigurationManagement);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelConfigurationManagement>> get(@RequestBody FilterConfigurationManagement filterConfigurationManagement) {
		try {
			List<ModelConfigurationManagement> res = serviceConfigurationManagement.getList(filterConfigurationManagement);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}


