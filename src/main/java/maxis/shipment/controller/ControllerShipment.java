package maxis.shipment.controller;

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
import maxis.shipment.model.FilterShipment;
import maxis.shipment.model.ModelShipment;
import maxis.shipment.service.ServiceShipment;

@RestController
@RequestMapping("/maxisellpshipment")
public class ControllerShipment {
	@Autowired
	private ServiceShipment serviceShipment;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelShipment> add(@RequestBody ModelShipment modelShipment) {
		try {
			ModelShipment res = serviceShipment.add(modelShipment);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelShipment>> get(@RequestBody FilterShipment filterShipment) {
		try {
			List<ModelShipment> res = serviceShipment.getList(filterShipment);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelShipment> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelShipment res = serviceShipment.create(onboardInputModel);
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
	public HttpEntity<ModelShipment> update(@RequestBody ModelShipment modelShipment) {
		try {
			ModelShipment res = serviceShipment.update(modelShipment);
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
	public HttpEntity<List<ModelShipment>> getListbyPropertyCode(@RequestBody FilterShipment filterShipment) {
		try {
			List<ModelShipment> res = serviceShipment.getListByPropertyCodeAndValue(filterShipment);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


