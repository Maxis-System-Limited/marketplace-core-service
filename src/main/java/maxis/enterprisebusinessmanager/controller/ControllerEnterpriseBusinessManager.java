package maxis.enterprisebusinessmanager.controller;

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
import maxis.enterprisebusinessmanager.model.FilterEnterpriseBusinessManager;
import maxis.enterprisebusinessmanager.model.ModelEnterpriseBusinessManager;
import maxis.enterprisebusinessmanager.service.ServiceEnterpriseBusinessManager;

@RestController
@RequestMapping("/maxisellpenterprisebm")
public class ControllerEnterpriseBusinessManager {
	@Autowired
	private ServiceEnterpriseBusinessManager serviceEnterpriseBusinessManager;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelEnterpriseBusinessManager> add(@RequestBody ModelEnterpriseBusinessManager modelEnterpriseBusinessManager) {
		try {
			ModelEnterpriseBusinessManager res = serviceEnterpriseBusinessManager.add(modelEnterpriseBusinessManager);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelEnterpriseBusinessManager>> get(@RequestBody FilterEnterpriseBusinessManager filterEnterpriseBusinessManager) {
		try {
			List<ModelEnterpriseBusinessManager> res = serviceEnterpriseBusinessManager.getList(filterEnterpriseBusinessManager);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelEnterpriseBusinessManager> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelEnterpriseBusinessManager res = serviceEnterpriseBusinessManager.create(onboardInputModel);
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
	public HttpEntity<ModelEnterpriseBusinessManager> update(@RequestBody ModelEnterpriseBusinessManager modelEnterpriseBusinessManager) {
		try {
			ModelEnterpriseBusinessManager res = serviceEnterpriseBusinessManager.update(modelEnterpriseBusinessManager);
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
	public HttpEntity<List<ModelEnterpriseBusinessManager>> getListbyPropertyCode(@RequestBody FilterEnterpriseBusinessManager filterEnterpriseBusinessManager) {
		try {
			List<ModelEnterpriseBusinessManager> res = serviceEnterpriseBusinessManager.getListByPropertyCodeAndValue(filterEnterpriseBusinessManager);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


