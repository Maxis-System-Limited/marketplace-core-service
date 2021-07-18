package maxis.dispatcheradmin.controller;

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
import maxis.dispatcheradmin.model.FilterDispatcherAdmin;
import maxis.dispatcheradmin.model.ModelDispatcherAdmin;
import maxis.dispatcheradmin.service.ServiceDispatcherAdmin;

@RestController
@RequestMapping("/maxisellpdadmin")
public class ControllerDispatcherAdmin {
	@Autowired
	private ServiceDispatcherAdmin serviceDispatcherAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelDispatcherAdmin> add(@RequestBody ModelDispatcherAdmin modelDispatcherAdmin) {
		try {
			ModelDispatcherAdmin res = serviceDispatcherAdmin.add(modelDispatcherAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelDispatcherAdmin>> get(@RequestBody FilterDispatcherAdmin filterDispatcherAdmin) {
		try {
			List<ModelDispatcherAdmin> res = serviceDispatcherAdmin.getList(filterDispatcherAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelDispatcherAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelDispatcherAdmin res = serviceDispatcherAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelDispatcherAdmin> update(@RequestBody ModelDispatcherAdmin modelDispatcherAdmin) {
		try {
			ModelDispatcherAdmin res = serviceDispatcherAdmin.update(modelDispatcherAdmin);
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
	public HttpEntity<List<ModelDispatcherAdmin>> getListbyPropertyCode(@RequestBody FilterDispatcherAdmin filterDispatcherAdmin) {
		try {
			List<ModelDispatcherAdmin> res = serviceDispatcherAdmin.getListByPropertyCodeAndValue(filterDispatcherAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


