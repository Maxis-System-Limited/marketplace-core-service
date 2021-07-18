package maxis.logisticpartneradmin.controller;

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
import maxis.logisticpartneradmin.model.FilterLogisticPartnerAdmin;
import maxis.logisticpartneradmin.model.ModelLogisticPartnerAdmin;
import maxis.logisticpartneradmin.service.ServiceLogisticPartnerAdmin;

@RestController
@RequestMapping("/maxisellpadmin")
public class ControllerLogisticPartnerAdmin {
	@Autowired
	private ServiceLogisticPartnerAdmin serviceLogisticPartnerAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelLogisticPartnerAdmin> add(@RequestBody ModelLogisticPartnerAdmin modelLogisticPartnerAdmin) {
		try {
			ModelLogisticPartnerAdmin res = serviceLogisticPartnerAdmin.add(modelLogisticPartnerAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelLogisticPartnerAdmin>> get(@RequestBody FilterLogisticPartnerAdmin filterLogisticPartnerAdmin) {
		try {
			List<ModelLogisticPartnerAdmin> res = serviceLogisticPartnerAdmin.getList(filterLogisticPartnerAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelLogisticPartnerAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelLogisticPartnerAdmin res = serviceLogisticPartnerAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelLogisticPartnerAdmin> update(@RequestBody ModelLogisticPartnerAdmin modelLogisticPartnerAdmin) {
		try {
			ModelLogisticPartnerAdmin res = serviceLogisticPartnerAdmin.update(modelLogisticPartnerAdmin);
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
	public HttpEntity<List<ModelLogisticPartnerAdmin>> getListbyPropertyCode(@RequestBody FilterLogisticPartnerAdmin filterLogisticPartnerAdmin) {
		try {
			List<ModelLogisticPartnerAdmin> res = serviceLogisticPartnerAdmin.getListByPropertyCodeAndValue(filterLogisticPartnerAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


