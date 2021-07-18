package maxis.warehouseadmin.controller;

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
import maxis.warehouseadmin.model.FilterWarehouseAdmin;
import maxis.warehouseadmin.model.ModelWarehouseAdmin;
import maxis.warehouseadmin.service.ServiceWarehouseAdmin;

@RestController
@RequestMapping("/maxisellpwadmin")
public class ControllerWarehouseAdmin {
	@Autowired
	private ServiceWarehouseAdmin serviceWarehouseAdmin;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseAdmin> add(@RequestBody ModelWarehouseAdmin modelWarehouseAdmin) {
		try {
			ModelWarehouseAdmin res = serviceWarehouseAdmin.add(modelWarehouseAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelWarehouseAdmin>> get(@RequestBody FilterWarehouseAdmin filterWarehouseAdmin) {
		try {
			List<ModelWarehouseAdmin> res = serviceWarehouseAdmin.getList(filterWarehouseAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseAdmin> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelWarehouseAdmin res = serviceWarehouseAdmin.create(onboardInputModel);
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
	public HttpEntity<ModelWarehouseAdmin> update(@RequestBody ModelWarehouseAdmin modelWarehouseAdmin) {
		try {
			ModelWarehouseAdmin res = serviceWarehouseAdmin.update(modelWarehouseAdmin);
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
	public HttpEntity<List<ModelWarehouseAdmin>> getListbyPropertyCode(@RequestBody FilterWarehouseAdmin filterWarehouseAdmin) {
		try {
			List<ModelWarehouseAdmin> res = serviceWarehouseAdmin.getListByPropertyCodeAndValue(filterWarehouseAdmin);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


