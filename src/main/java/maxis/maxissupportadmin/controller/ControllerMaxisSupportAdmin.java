package maxis.maxissupportadmin.controller;

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
import maxis.maxissupportadmin.model.FilterMaxisSupportAdmin;
import maxis.maxissupportadmin.model.ModelMaxisSupportAdmin;
import maxis.maxissupportadmin.service.ServiceMaxisSupportAdmin;

@RestController
@RequestMapping("/maxiselsupport")
public class ControllerMaxisSupportAdmin {
	@Autowired
	private ServiceMaxisSupportAdmin serviceMaxisSupportAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelMaxisSupportAdmin> add(@RequestBody ModelMaxisSupportAdmin modelMaxisSupportAdmin) {
		try {
			ModelMaxisSupportAdmin res = serviceMaxisSupportAdmin.add(modelMaxisSupportAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelMaxisSupportAdmin>> get(@RequestBody FilterMaxisSupportAdmin filterMaxisSupportAdmin) {
		try {
			List<ModelMaxisSupportAdmin> res = serviceMaxisSupportAdmin.getList(filterMaxisSupportAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelMaxisSupportAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelMaxisSupportAdmin res = serviceMaxisSupportAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelMaxisSupportAdmin> update(@RequestBody ModelMaxisSupportAdmin modelMaxisSupportAdmin) {
		try {
			ModelMaxisSupportAdmin res = serviceMaxisSupportAdmin.update(modelMaxisSupportAdmin);
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
	public HttpEntity<List<ModelMaxisSupportAdmin>> getListbyPropertyCode(@RequestBody FilterMaxisSupportAdmin filterMaxisSupportAdmin) {
		try {
			List<ModelMaxisSupportAdmin> res = serviceMaxisSupportAdmin.getListByPropertyCodeAndValue(filterMaxisSupportAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


