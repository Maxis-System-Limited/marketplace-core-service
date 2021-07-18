package maxis.accountaccountant.controller;

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

import maxis.accountaccountant.model.FilterAccountAccountant;
import maxis.accountaccountant.model.ModelAccountAccountant;
import maxis.accountaccountant.service.ServiceAccountAccountant;
import maxis.common.model.OnboardInputModel;

@RestController
@RequestMapping("/maxisellpdaccountaccountant")
public class ControllerAccountAccountant {
	@Autowired
	private ServiceAccountAccountant serviceAccountAccountant;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelAccountAccountant> add(@RequestBody ModelAccountAccountant modelAccountAccountant) {
		try {
			ModelAccountAccountant res = serviceAccountAccountant.add(modelAccountAccountant);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelAccountAccountant>> get(@RequestBody FilterAccountAccountant filterAccountAccountant) {
		try {
			List<ModelAccountAccountant> res = serviceAccountAccountant.getList(filterAccountAccountant);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelAccountAccountant> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelAccountAccountant res = serviceAccountAccountant.create(onboardInputModel);
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
	public HttpEntity<ModelAccountAccountant> update(@RequestBody ModelAccountAccountant modelAccountAccountant) {
		try {
			ModelAccountAccountant res = serviceAccountAccountant.update(modelAccountAccountant);
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
	public HttpEntity<List<ModelAccountAccountant>> getListbyPropertyCode(@RequestBody FilterAccountAccountant filterAccountAccountant) {
		try {
			List<ModelAccountAccountant> res = serviceAccountAccountant.getListByPropertyCodeAndValue(filterAccountAccountant);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


