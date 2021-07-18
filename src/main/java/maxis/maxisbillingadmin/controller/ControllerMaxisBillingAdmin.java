package maxis.maxisbillingadmin.controller;

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
import maxis.maxisbillingadmin.model.FilterMaxisBillingAdmin;
import maxis.maxisbillingadmin.model.ModelMaxisBillingAdmin;
import maxis.maxisbillingadmin.service.ServiceMaxisBillingAdmin;

@RestController
@RequestMapping("/maxiselbilling")
public class ControllerMaxisBillingAdmin {
	@Autowired
	private ServiceMaxisBillingAdmin serviceMaxisBillingAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelMaxisBillingAdmin> add(@RequestBody ModelMaxisBillingAdmin modelMaxisBillingAdmin) {
		try {
			ModelMaxisBillingAdmin res = serviceMaxisBillingAdmin.add(modelMaxisBillingAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelMaxisBillingAdmin>> get(@RequestBody FilterMaxisBillingAdmin filterMaxisBillingAdmin) {
		try {
			List<ModelMaxisBillingAdmin> res = serviceMaxisBillingAdmin.getList(filterMaxisBillingAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelMaxisBillingAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelMaxisBillingAdmin res = serviceMaxisBillingAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelMaxisBillingAdmin> update(@RequestBody ModelMaxisBillingAdmin modelMaxisBillingAdmin) {
		try {
			ModelMaxisBillingAdmin res = serviceMaxisBillingAdmin.update(modelMaxisBillingAdmin);
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
	public HttpEntity<List<ModelMaxisBillingAdmin>> getListbyPropertyCode(@RequestBody FilterMaxisBillingAdmin filterMaxisBillingAdmin) {
		try {
			List<ModelMaxisBillingAdmin> res = serviceMaxisBillingAdmin.getListByPropertyCodeAndValue(filterMaxisBillingAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


