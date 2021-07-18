package maxis.warehousestockissuer.controller;

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
import maxis.warehousestockissuer.model.FilterWarehouseStockIssuer;
import maxis.warehousestockissuer.model.ModelWarehouseStockIssuer;
import maxis.warehousestockissuer.service.ServiceWarehouseStockIssuer;

@RestController
@RequestMapping("/maxisellpwsissuer")
public class ControllerWarehouseStockIssuer {
	@Autowired
	private ServiceWarehouseStockIssuer serviceWarehouseStockIssuer;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseStockIssuer> add(@RequestBody ModelWarehouseStockIssuer modelWarehouseStockIssuer) {
		try {
			ModelWarehouseStockIssuer res = serviceWarehouseStockIssuer.add(modelWarehouseStockIssuer);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelWarehouseStockIssuer>> get(@RequestBody FilterWarehouseStockIssuer filterWarehouseStockIssuer) {
		try {
			List<ModelWarehouseStockIssuer> res = serviceWarehouseStockIssuer.getList(filterWarehouseStockIssuer);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseStockIssuer> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelWarehouseStockIssuer res = serviceWarehouseStockIssuer.create(onboardInputModel);
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
	public HttpEntity<ModelWarehouseStockIssuer> update(@RequestBody ModelWarehouseStockIssuer modelWarehouseStockIssuer) {
		try {
			ModelWarehouseStockIssuer res = serviceWarehouseStockIssuer.update(modelWarehouseStockIssuer);
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
	public HttpEntity<List<ModelWarehouseStockIssuer>> getListbyPropertyCode(@RequestBody FilterWarehouseStockIssuer filterWarehouseStockIssuer) {
		try {
			List<ModelWarehouseStockIssuer> res = serviceWarehouseStockIssuer.getListByPropertyCodeAndValue(filterWarehouseStockIssuer);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


