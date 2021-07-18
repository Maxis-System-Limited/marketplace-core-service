package maxis.enterprise.controller;

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
import maxis.enterprise.model.FilterEnterprise;
import maxis.enterprise.model.ModelEnterprise;
import maxis.enterprise.service.ServiceEnterprise;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.service.ServiceWarehouse;

@RestController
@RequestMapping("/maxisellpenterprise")
public class ControllerEnterprise {
	@Autowired
	private ServiceEnterprise serviceEnterprise;
	@Autowired
	private ServiceWarehouse serviceWarehouse;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelEnterprise> add(@RequestBody ModelEnterprise modelEnterprise) {
		try {
			ModelEnterprise res = serviceEnterprise.add(modelEnterprise);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelEnterprise>> get(@RequestBody FilterEnterprise filterEnterprise) {
		try {
			List<ModelEnterprise> res = serviceEnterprise.getList(filterEnterprise);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelEnterprise> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelEnterprise res = serviceEnterprise.create(onboardInputModel);
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
			res = serviceEnterprise.add(res);
			System.out.println("ControllerDelivery : create : 5 : mw" + res.getTaggedWarehouse().getCode());
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public HttpEntity<ModelEnterprise> update(@RequestBody ModelEnterprise modelEnterprise) {
		try {
			ModelEnterprise res = serviceEnterprise.update(modelEnterprise);
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
	public HttpEntity<List<ModelEnterprise>> getListbyPropertyCode(@RequestBody FilterEnterprise filterEnterprise) {
		try {
			List<ModelEnterprise> res = serviceEnterprise.getListByPropertyCodeAndValue(filterEnterprise);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


