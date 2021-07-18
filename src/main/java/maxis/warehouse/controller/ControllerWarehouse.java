package maxis.warehouse.controller;

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
import maxis.warehouse.model.FilterWarehouse;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.service.ServiceWarehouse;

@RestController
@RequestMapping("/maxisellpwarehouse")
public class ControllerWarehouse {
	@Autowired
	private ServiceWarehouse serviceWarehouse;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouse> add(@RequestBody ModelWarehouse modelWarehouse) {
		try {
			ModelWarehouse res = serviceWarehouse.add(modelWarehouse);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelWarehouse>> get(@RequestBody FilterWarehouse filterWarehouse) {
		try {
			List<ModelWarehouse> res = serviceWarehouse.getList(filterWarehouse);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouse> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelWarehouse res = serviceWarehouse.create(onboardInputModel);
			if (res != null) {
				return new ResponseEntity<>(res, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(res, HttpStatus.CONFLICT);
			}

		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public HttpEntity<ModelWarehouse> update(@RequestBody ModelWarehouse modelWarehouse) {
		try {
			ModelWarehouse res = serviceWarehouse.update(modelWarehouse);
			if (res != null) {
				return new ResponseEntity<>(res, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(res, HttpStatus.NOT_MODIFIED);
			}

		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getbyproperty")
	public HttpEntity<List<ModelWarehouse>> getListbyPropertyCode(@RequestBody FilterWarehouse filterWarehouse) {
		try {
			List<ModelWarehouse> res = serviceWarehouse.getListByPropertyCodeAndValue(filterWarehouse);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getwbythana")
	public HttpEntity<ModelWarehouse> getWarehouseByThana(@RequestBody FilterWarehouse filterWarehouse) {
		try {
			ModelWarehouse res = serviceWarehouse.getModelWarehouseByThanaDistrictFromPropeortyList(filterWarehouse.getCode(), filterWarehouse.getPropertyValue());
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		}
		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
