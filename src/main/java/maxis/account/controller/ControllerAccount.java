package maxis.account.controller;

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

import maxis.account.model.FilterAccount;
import maxis.account.model.ModelAccount;
import maxis.account.service.ServiceAccount;
import maxis.common.model.OnboardInputModel;

@RestController
@RequestMapping("/account")
public class ControllerAccount {
	@Autowired
	private ServiceAccount serviceAccount;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelAccount> add(@RequestBody ModelAccount modelAccount) {
		try {
			ModelAccount res = serviceAccount.add(modelAccount);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelAccount>> get(@RequestBody FilterAccount filterAccount) {
		try {
			List<ModelAccount> res = serviceAccount.getList(filterAccount);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelAccount> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelAccount res = serviceAccount.create(onboardInputModel);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
}


