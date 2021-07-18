package maxis.maxissuperadmin.controller;

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
import maxis.maxissuperadmin.model.FilterMaxisSuperAdmin;
import maxis.maxissuperadmin.model.ModelMaxisSuperAdmin;
import maxis.maxissuperadmin.service.ServiceMaxisSuperAdmin;

@RestController
@RequestMapping("/maxissuperadmin")
public class ControllerMaxisSuperAdmin {
	@Autowired
	private ServiceMaxisSuperAdmin serviceMaxisSuperAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelMaxisSuperAdmin> add(@RequestBody ModelMaxisSuperAdmin modelMaxisSuperAdmin) {
		try {
			ModelMaxisSuperAdmin res = serviceMaxisSuperAdmin.add(modelMaxisSuperAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelMaxisSuperAdmin>> get(@RequestBody FilterMaxisSuperAdmin filterMaxisSuperAdmin) {
		try {
			List<ModelMaxisSuperAdmin> res = serviceMaxisSuperAdmin.getList(filterMaxisSuperAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelMaxisSuperAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelMaxisSuperAdmin res = serviceMaxisSuperAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelMaxisSuperAdmin> update(@RequestBody ModelMaxisSuperAdmin modelMaxisSuperAdmin) {
		try {
			ModelMaxisSuperAdmin res = serviceMaxisSuperAdmin.update(modelMaxisSuperAdmin);
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
	public HttpEntity<List<ModelMaxisSuperAdmin>> getListbyPropertyCode(@RequestBody FilterMaxisSuperAdmin filterMaxisSuperAdmin) {
		try {
			List<ModelMaxisSuperAdmin> res = serviceMaxisSuperAdmin.getListByPropertyCodeAndValue(filterMaxisSuperAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


