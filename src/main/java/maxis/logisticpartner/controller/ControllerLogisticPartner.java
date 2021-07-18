package maxis.logisticpartner.controller;

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
import maxis.logisticpartner.model.FilterLogisticPartner;
import maxis.logisticpartner.model.ModelLogisticPartner;
import maxis.logisticpartner.service.ServiceLogisticPartner;

@RestController
@RequestMapping("/maxisellp")
public class ControllerLogisticPartner {
	@Autowired
	private ServiceLogisticPartner serviceLogisticPartner;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelLogisticPartner> add(@RequestBody ModelLogisticPartner modelLogisticPartner) {
		try {
			ModelLogisticPartner res = serviceLogisticPartner.add(modelLogisticPartner);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelLogisticPartner>> get(@RequestBody FilterLogisticPartner filterLogisticPartner) {
		try {
			List<ModelLogisticPartner> res = serviceLogisticPartner.getList(filterLogisticPartner);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelLogisticPartner> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelLogisticPartner res = serviceLogisticPartner.create(onboardInputModel);
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
	public HttpEntity<ModelLogisticPartner> update(@RequestBody ModelLogisticPartner modelLogisticPartner) {
		try {
			ModelLogisticPartner res = serviceLogisticPartner.update(modelLogisticPartner);
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
	public HttpEntity<List<ModelLogisticPartner>> getListbyPropertyCode(@RequestBody FilterLogisticPartner filterLogisticPartner) {
		try {
			List<ModelLogisticPartner> res = serviceLogisticPartner.getListByPropertyCodeAndValue(filterLogisticPartner);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


