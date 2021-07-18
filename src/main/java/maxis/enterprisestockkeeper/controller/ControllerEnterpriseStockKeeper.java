package maxis.enterprisestockkeeper.controller;

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
import maxis.enterprisestockkeeper.model.FilterEnterpriseStockKeeper;
import maxis.enterprisestockkeeper.model.ModelEnterpriseStockKeeper;
import maxis.enterprisestockkeeper.service.ServiceEnterpriseStockKeeper;

@RestController
@RequestMapping("/maxisellpenterprisesk")
public class ControllerEnterpriseStockKeeper {
	@Autowired
	private ServiceEnterpriseStockKeeper serviceEnterpriseStockKeeper;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelEnterpriseStockKeeper> add(@RequestBody ModelEnterpriseStockKeeper modelEnterpriseStockKeeper) {
		try {
			ModelEnterpriseStockKeeper res = serviceEnterpriseStockKeeper.add(modelEnterpriseStockKeeper);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelEnterpriseStockKeeper>> get(@RequestBody FilterEnterpriseStockKeeper filterEnterpriseStockKeeper) {
		try {
			List<ModelEnterpriseStockKeeper> res = serviceEnterpriseStockKeeper.getList(filterEnterpriseStockKeeper);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelEnterpriseStockKeeper> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelEnterpriseStockKeeper res = serviceEnterpriseStockKeeper.create(onboardInputModel);
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
	public HttpEntity<ModelEnterpriseStockKeeper> update(@RequestBody ModelEnterpriseStockKeeper modelEnterpriseStockKeeper) {
		try {
			ModelEnterpriseStockKeeper res = serviceEnterpriseStockKeeper.update(modelEnterpriseStockKeeper);
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
	public HttpEntity<List<ModelEnterpriseStockKeeper>> getListbyPropertyCode(@RequestBody FilterEnterpriseStockKeeper filterEnterpriseStockKeeper) {
		try {
			List<ModelEnterpriseStockKeeper> res = serviceEnterpriseStockKeeper.getListByPropertyCodeAndValue(filterEnterpriseStockKeeper);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


