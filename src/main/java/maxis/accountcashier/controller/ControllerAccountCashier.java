package maxis.accountcashier.controller;

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

import maxis.accountcashier.model.FilterAccountCashier;
import maxis.accountcashier.model.ModelAccountCashier;
import maxis.accountcashier.service.ServiceAccountCashier;
import maxis.common.model.OnboardInputModel;

@RestController
@RequestMapping("/maxisellpdaccountcashier")
public class ControllerAccountCashier {
	@Autowired
	private ServiceAccountCashier serviceAccountCashier;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelAccountCashier> add(@RequestBody ModelAccountCashier modelAccountCashier) {
		try {
			ModelAccountCashier res = serviceAccountCashier.add(modelAccountCashier);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelAccountCashier>> get(@RequestBody FilterAccountCashier filterAccountCashier) {
		try {
			List<ModelAccountCashier> res = serviceAccountCashier.getList(filterAccountCashier);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelAccountCashier> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelAccountCashier res = serviceAccountCashier.create(onboardInputModel);
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
	public HttpEntity<ModelAccountCashier> update(@RequestBody ModelAccountCashier modelAccountCashier) {
		try {
			ModelAccountCashier res = serviceAccountCashier.update(modelAccountCashier);
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
	public HttpEntity<List<ModelAccountCashier>> getListbyPropertyCode(@RequestBody FilterAccountCashier filterAccountCashier) {
		try {
			List<ModelAccountCashier> res = serviceAccountCashier.getListByPropertyCodeAndValue(filterAccountCashier);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


