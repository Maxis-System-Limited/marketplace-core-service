package maxis.deliveryadmin.controller;

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
import maxis.deliveryadmin.model.FilterDeliveryAdmin;
import maxis.deliveryadmin.model.ModelDeliveryAdmin;
import maxis.deliveryadmin.service.ServiceDeliveryAdmin;

@RestController
@RequestMapping("/maxisellpddeliveryadmin")
public class ControllerDeliveryAdmin {
	@Autowired
	private ServiceDeliveryAdmin serviceDeliveryAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryAdmin> add(@RequestBody ModelDeliveryAdmin modelDeliveryAdmin) {
		try {
			ModelDeliveryAdmin res = serviceDeliveryAdmin.add(modelDeliveryAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelDeliveryAdmin>> get(@RequestBody FilterDeliveryAdmin filterDeliveryAdmin) {
		try {
			List<ModelDeliveryAdmin> res = serviceDeliveryAdmin.getList(filterDeliveryAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelDeliveryAdmin res = serviceDeliveryAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelDeliveryAdmin> update(@RequestBody ModelDeliveryAdmin modelDeliveryAdmin) {
		try {
			ModelDeliveryAdmin res = serviceDeliveryAdmin.update(modelDeliveryAdmin);
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
	public HttpEntity<List<ModelDeliveryAdmin>> getListbyPropertyCode(@RequestBody FilterDeliveryAdmin filterDeliveryAdmin) {
		try {
			List<ModelDeliveryAdmin> res = serviceDeliveryAdmin.getListByPropertyCodeAndValue(filterDeliveryAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


