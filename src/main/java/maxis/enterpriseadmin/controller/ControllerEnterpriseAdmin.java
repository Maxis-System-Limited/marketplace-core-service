package maxis.enterpriseadmin.controller;

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
import maxis.enterpriseadmin.model.FilterEnterpriseAdmin;
import maxis.enterpriseadmin.model.ModelEnterpriseAdmin;
import maxis.enterpriseadmin.service.ServiceEnterpriseAdmin;

@RestController
@RequestMapping("/maxisellpenterpriseadmin")
public class ControllerEnterpriseAdmin {
	@Autowired
	private ServiceEnterpriseAdmin serviceEnterpriseAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelEnterpriseAdmin> add(@RequestBody ModelEnterpriseAdmin modelEnterpriseAdmin) {
		try {
			ModelEnterpriseAdmin res = serviceEnterpriseAdmin.add(modelEnterpriseAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelEnterpriseAdmin>> get(@RequestBody FilterEnterpriseAdmin filterEnterpriseAdmin) {
		try {
			List<ModelEnterpriseAdmin> res = serviceEnterpriseAdmin.getList(filterEnterpriseAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelEnterpriseAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelEnterpriseAdmin res = serviceEnterpriseAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelEnterpriseAdmin> update(@RequestBody ModelEnterpriseAdmin modelEnterpriseAdmin) {
		try {
			ModelEnterpriseAdmin res = serviceEnterpriseAdmin.update(modelEnterpriseAdmin);
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
	public HttpEntity<List<ModelEnterpriseAdmin>> getListbyPropertyCode(@RequestBody FilterEnterpriseAdmin filterEnterpriseAdmin) {
		try {
			List<ModelEnterpriseAdmin> res = serviceEnterpriseAdmin.getListByPropertyCodeAndValue(filterEnterpriseAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


