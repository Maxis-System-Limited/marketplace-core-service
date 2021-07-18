package maxis.warehousestockkeeper.controller;

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
import maxis.warehousestockkeeper.model.FilterWarehouseStockKeeper;
import maxis.warehousestockkeeper.model.ModelWarehouseStockKeeper;
import maxis.warehousestockkeeper.service.ServiceWarehouseStockKeeper;

@RestController
@RequestMapping("/maxisellpwskeeper")
public class ControllerWarehouseStockKeeper {
	@Autowired
	private ServiceWarehouseStockKeeper serviceWarehouseStockKeeper;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseStockKeeper> add(@RequestBody ModelWarehouseStockKeeper modelWarehouseStockKeeper) {
		try {
			ModelWarehouseStockKeeper res = serviceWarehouseStockKeeper.add(modelWarehouseStockKeeper);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelWarehouseStockKeeper>> get(@RequestBody FilterWarehouseStockKeeper filterWarehouseStockKeeper) {
		try {
			List<ModelWarehouseStockKeeper> res = serviceWarehouseStockKeeper.getList(filterWarehouseStockKeeper);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouseStockKeeper> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelWarehouseStockKeeper res = serviceWarehouseStockKeeper.create(onboardInputModel);
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
	public HttpEntity<ModelWarehouseStockKeeper> update(@RequestBody ModelWarehouseStockKeeper modelWarehouseStockKeeper) {
		try {
			ModelWarehouseStockKeeper res = serviceWarehouseStockKeeper.update(modelWarehouseStockKeeper);
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
	public HttpEntity<List<ModelWarehouseStockKeeper>> getListbyPropertyCode(@RequestBody FilterWarehouseStockKeeper filterWarehouseStockKeeper) {
		try {
			List<ModelWarehouseStockKeeper> res = serviceWarehouseStockKeeper.getListByPropertyCodeAndValue(filterWarehouseStockKeeper);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


