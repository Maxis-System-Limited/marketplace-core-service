package maxis.deliveryrider.controller;

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

import maxis.common.UtilityProperty;
import maxis.common.model.OnboardInputModel;
import maxis.deliveryrider.model.FilterDeliveryRider;
import maxis.deliveryrider.model.ModelDeliveryRider;
import maxis.deliveryrider.service.ServiceDeliveryRider;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.service.ServiceWarehouse;

@RestController
@RequestMapping("/maxisellpdeliveryrider")
public class ControllerDeliveryRider {
	@Autowired
	private ServiceDeliveryRider serviceDeliveryRider;
	@Autowired
	private ServiceWarehouse serviceWarehouse;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryRider> add(@RequestBody ModelDeliveryRider modelDeliveryRider) {
		try {
			ModelDeliveryRider res = serviceDeliveryRider.add(modelDeliveryRider);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelDeliveryRider>> get(@RequestBody FilterDeliveryRider filterDeliveryRider) {
		try {
			List<ModelDeliveryRider> res = serviceDeliveryRider.getList(filterDeliveryRider);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryRider> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelDeliveryRider res = serviceDeliveryRider.create(onboardInputModel);
			if(res == null) {
				return new ResponseEntity<>(res, HttpStatus.CONFLICT);
			}
			System.out.println("ControllerDeliveryRider : create : 1 created");
			String lpCode = UtilityProperty.getValueProperties(res.getSteps().get(0).getContext().get(0).getProperties(), "PARENT_ORGANIZATION_USER_ID");
			System.out.println("ControllerDeliveryRider : create : 2 : lpCode: " + lpCode);
			String tId = UtilityProperty.getValueProperties(res.getSteps().get(0).getContext().get(0).getProperties(), "THANA");
			System.out.println("ControllerDeliveryRider : create : 3 : tId: " + tId);
			ModelWarehouse mw = serviceWarehouse.getModelWarehouseByThanaDistrictFromPropeortyList(lpCode, tId);
			System.out.println("ControllerDeliveryRider : create : 4 : mw" + mw.getCode());
			res.setTaggedWarehouse(mw);
			res = serviceDeliveryRider.add(res);
			System.out.println("ControllerDeliveryRider : create : 5 : mw" + res.getTaggedWarehouse().getCode());
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryRider> update(@RequestBody ModelDeliveryRider modelDeliveryRider) {
		try {
			ModelDeliveryRider res = serviceDeliveryRider.update(modelDeliveryRider);
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
	public HttpEntity<List<ModelDeliveryRider>> getListbyPropertyCode(@RequestBody FilterDeliveryRider filterDeliveryRider) {
		try {
			List<ModelDeliveryRider> res = serviceDeliveryRider.getListByPropertyCodeAndValue(filterDeliveryRider);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


