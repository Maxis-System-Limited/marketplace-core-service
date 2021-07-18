package maxis.transporter.controller;

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

import maxis.common.model.OnboardInputModel;
import maxis.transporter.model.FilterTransporter;
import maxis.transporter.model.ModelTransporter;
import maxis.transporter.service.ServiceTransporter;

@RestController
@RequestMapping("/maxisellpdeliverytransporter")
public class ControllerTransporter {
	@Autowired
	private ServiceTransporter serviceTransporter;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelTransporter> add(@RequestBody ModelTransporter modelTransporter) {
		try {
			ModelTransporter res = serviceTransporter.add(modelTransporter);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelTransporter>> get(@RequestBody FilterTransporter filterTransporter) {
		try {
			List<ModelTransporter> res = serviceTransporter.getList(filterTransporter);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelTransporter> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelTransporter res = serviceTransporter.create(onboardInputModel);
			if(res != null) {
				return new ResponseEntity<>(res, HttpStatus.CREATED);
			}else {
				return new ResponseEntity<>(res, HttpStatus.CONFLICT);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public HttpEntity<ModelTransporter> update(@RequestBody ModelTransporter modelTransporter) {
		try {
			ModelTransporter res = serviceTransporter.update(modelTransporter);
			if(res != null) {
				return new ResponseEntity<>(res, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(res, HttpStatus.NOT_MODIFIED);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/getbyproperty")
	public HttpEntity<List<ModelTransporter>> getListbyPropertyCode(@RequestBody FilterTransporter filterTransporter) {
		try {
			List<ModelTransporter> res = serviceTransporter.getListByPropertyCodeAndValue(filterTransporter);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


