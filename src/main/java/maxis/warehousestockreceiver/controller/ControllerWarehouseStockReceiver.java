package maxis.warehousestockreceiver.controller;

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
import maxis.warehousestockreceiver.model.FilterWarehouseStockReceiver;
import maxis.warehousestockreceiver.model.ModelWarehouseStockReceiver;
import maxis.warehousestockreceiver.service.ServiceWarehouseStockReceiver;

@RestController
@RequestMapping("/maxisellpwsreceiver")
public class ControllerWarehouseStockReceiver {
	@Autowired
	private ServiceWarehouseStockReceiver serviceWarehouseStockReceiver;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseStockReceiver> add(@RequestBody ModelWarehouseStockReceiver modelWarehouseStockReceiver) {
		try {
			ModelWarehouseStockReceiver res = serviceWarehouseStockReceiver.add(modelWarehouseStockReceiver);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelWarehouseStockReceiver>> get(@RequestBody FilterWarehouseStockReceiver filterWarehouseStockReceiver) {
		try {
			List<ModelWarehouseStockReceiver> res = serviceWarehouseStockReceiver.getList(filterWarehouseStockReceiver);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseStockReceiver> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelWarehouseStockReceiver res = serviceWarehouseStockReceiver.create(onboardInputModel);
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
	public HttpEntity<ModelWarehouseStockReceiver> update(@RequestBody ModelWarehouseStockReceiver modelWarehouseStockReceiver) {
		try {
			ModelWarehouseStockReceiver res = serviceWarehouseStockReceiver.update(modelWarehouseStockReceiver);
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
	public HttpEntity<List<ModelWarehouseStockReceiver>> getListbyPropertyCode(@RequestBody FilterWarehouseStockReceiver filterWarehouseStockReceiver) {
		try {
			List<ModelWarehouseStockReceiver> res = serviceWarehouseStockReceiver.getListByPropertyCodeAndValue(filterWarehouseStockReceiver);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


