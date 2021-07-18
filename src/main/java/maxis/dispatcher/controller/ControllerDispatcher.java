package maxis.dispatcher.controller;

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
import maxis.dispatcher.model.FilterDispatcher;
import maxis.dispatcher.model.ModelDispatcher;
import maxis.dispatcher.service.ServiceDispatcher;

@RestController
@RequestMapping("/maxisellpdadispatcher")
public class ControllerDispatcher {
	@Autowired
	private ServiceDispatcher serviceDispatcher;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelDispatcher> add(@RequestBody ModelDispatcher modelDispatcher) {
		try {
			ModelDispatcher res = serviceDispatcher.add(modelDispatcher);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelDispatcher>> get(@RequestBody FilterDispatcher filterDispatcher) {
		try {
			List<ModelDispatcher> res = serviceDispatcher.getList(filterDispatcher);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelDispatcher> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelDispatcher res = serviceDispatcher.create(onboardInputModel);
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
	public HttpEntity<ModelDispatcher> update(@RequestBody ModelDispatcher modelDispatcher) {
		try {
			ModelDispatcher res = serviceDispatcher.update(modelDispatcher);
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
	public HttpEntity<List<ModelDispatcher>> getListbyPropertyCode(@RequestBody FilterDispatcher filterDispatcher) {
		try {
			List<ModelDispatcher> res = serviceDispatcher.getListByPropertyCodeAndValue(filterDispatcher);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


