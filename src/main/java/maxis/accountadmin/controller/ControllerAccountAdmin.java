package maxis.accountadmin.controller;

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

import maxis.accountadmin.model.FilterAccountAdmin;
import maxis.accountadmin.model.ModelAccountAdmin;
import maxis.accountadmin.service.ServiceAccountAdmin;
import maxis.common.model.OnboardInputModel;

@RestController
@RequestMapping("/maxisellpdaccountadmin")
public class ControllerAccountAdmin {
	@Autowired
	private ServiceAccountAdmin serviceAccountAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelAccountAdmin> add(@RequestBody ModelAccountAdmin modelAccountAdmin) {
		try {
			ModelAccountAdmin res = serviceAccountAdmin.add(modelAccountAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelAccountAdmin>> get(@RequestBody FilterAccountAdmin filterAccountAdmin) {
		try {
			List<ModelAccountAdmin> res = serviceAccountAdmin.getList(filterAccountAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelAccountAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelAccountAdmin res = serviceAccountAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelAccountAdmin> update(@RequestBody ModelAccountAdmin modelAccountAdmin) {
		try {
			ModelAccountAdmin res = serviceAccountAdmin.update(modelAccountAdmin);
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
	public HttpEntity<List<ModelAccountAdmin>> getListbyPropertyCode(@RequestBody FilterAccountAdmin filterAccountAdmin) {
		try {
			List<ModelAccountAdmin> res = serviceAccountAdmin.getListByPropertyCodeAndValue(filterAccountAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


