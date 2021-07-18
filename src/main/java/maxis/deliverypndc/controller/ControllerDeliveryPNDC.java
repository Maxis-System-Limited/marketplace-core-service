package maxis.deliverypndc.controller;

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
import maxis.deliverypndc.model.FilterDeliveryPNDC;
import maxis.deliverypndc.model.ModelDeliveryPNDC;
import maxis.deliverypndc.service.ServiceDeliveryPNDC;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.service.ServiceWarehouse;

@RestController
@RequestMapping("/maxisellpdeliverypndc")
public class ControllerDeliveryPNDC {
	@Autowired
	private ServiceDeliveryPNDC serviceDeliveryPNDC;
	@Autowired
	private ServiceWarehouse serviceWarehouse;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryPNDC> add(@RequestBody ModelDeliveryPNDC modelDeliveryPNDC) {
		try {
			ModelDeliveryPNDC res = serviceDeliveryPNDC.add(modelDeliveryPNDC);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelDeliveryPNDC>> get(@RequestBody FilterDeliveryPNDC filterDeliveryPNDC) {
		try {
			List<ModelDeliveryPNDC> res = serviceDeliveryPNDC.getList(filterDeliveryPNDC);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryPNDC> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelDeliveryPNDC res = serviceDeliveryPNDC.create(onboardInputModel);
			if(res == null) {
				return new ResponseEntity<>(res, HttpStatus.CONFLICT);
			}
			System.out.println("ControllerDelivery : create : 1 created");
			String lpCode = UtilityProperty.getValueProperties(res.getSteps().get(0).getContext().get(0).getProperties(), "PARENT_ORGANIZATION_USER_ID");
			System.out.println("ControllerDelivery : create : 2 : lpCode: " + lpCode);
			String tId = UtilityProperty.getValueProperties(res.getSteps().get(0).getContext().get(0).getProperties(), "THANA");
			System.out.println("ControllerDelivery : create : 3 : tId: " + tId);
			ModelWarehouse mw = serviceWarehouse.getModelWarehouseByThanaDistrictFromPropeortyList(lpCode, tId);
			System.out.println("ControllerDelivery : create : 4 : mw" + mw.getCode());
			res.setTaggedWarehouse(mw);
			res = serviceDeliveryPNDC.add(res);
			System.out.println("ControllerDelivery : create : 5 : mw" + res.getTaggedWarehouse().getCode());
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public HttpEntity<ModelDeliveryPNDC> update(@RequestBody ModelDeliveryPNDC modelDeliveryPNDC) {
		try {
			ModelDeliveryPNDC res = serviceDeliveryPNDC.update(modelDeliveryPNDC);
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
	public HttpEntity<List<ModelDeliveryPNDC>> getListbyPropertyCode(@RequestBody FilterDeliveryPNDC filterDeliveryPNDC) {
		try {
			List<ModelDeliveryPNDC> res = serviceDeliveryPNDC.getListByPropertyCodeAndValue(filterDeliveryPNDC);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


