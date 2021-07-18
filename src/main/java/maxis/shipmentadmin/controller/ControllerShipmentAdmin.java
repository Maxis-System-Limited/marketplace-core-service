package maxis.shipmentadmin.controller;

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
import maxis.shipmentadmin.model.FilterShipmentAdmin;
import maxis.shipmentadmin.model.ModelShipmentAdmin;
import maxis.shipmentadmin.service.ServiceShipmentAdmin;

@RestController
@RequestMapping("/maxisellpsadmin")
public class ControllerShipmentAdmin {
	@Autowired
	private ServiceShipmentAdmin serviceShipmentAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelShipmentAdmin> add(@RequestBody ModelShipmentAdmin modelShipmentAdmin) {
		try {
			ModelShipmentAdmin res = serviceShipmentAdmin.add(modelShipmentAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelShipmentAdmin>> get(@RequestBody FilterShipmentAdmin filterShipmentAdmin) {
		try {
			List<ModelShipmentAdmin> res = serviceShipmentAdmin.getList(filterShipmentAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelShipmentAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelShipmentAdmin res = serviceShipmentAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelShipmentAdmin> update(@RequestBody ModelShipmentAdmin modelShipmentAdmin) {
		try {
			ModelShipmentAdmin res = serviceShipmentAdmin.update(modelShipmentAdmin);
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
	public HttpEntity<List<ModelShipmentAdmin>> getListbyPropertyCode(@RequestBody FilterShipmentAdmin filterShipmentAdmin) {
		try {
			List<ModelShipmentAdmin> res = serviceShipmentAdmin.getListByPropertyCodeAndValue(filterShipmentAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


