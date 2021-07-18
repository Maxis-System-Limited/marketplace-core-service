package maxis.shipmentcontroller.controller;

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
import maxis.shipmentcontroller.model.FilterShipmentController;
import maxis.shipmentcontroller.model.ModelShipmentController;
import maxis.shipmentcontroller.service.ServiceShipmentController;

@RestController
@RequestMapping("/maxisellpscontroller")
public class ControllerShipmentController {
	@Autowired
	private ServiceShipmentController serviceShipmentController;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelShipmentController> add(@RequestBody ModelShipmentController modelShipmentController) {
		try {
			ModelShipmentController res = serviceShipmentController.add(modelShipmentController);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelShipmentController>> get(@RequestBody FilterShipmentController filterShipmentController) {
		try {
			List<ModelShipmentController> res = serviceShipmentController.getList(filterShipmentController);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public HttpEntity<ModelShipmentController> create(@RequestBody OnboardInputModel onboardInputModel) {
		try {
			ModelShipmentController res = serviceShipmentController.create(onboardInputModel);
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
	public HttpEntity<ModelShipmentController> update(@RequestBody ModelShipmentController modelShipmentController) {
		try {
			ModelShipmentController res = serviceShipmentController.update(modelShipmentController);
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
	public HttpEntity<List<ModelShipmentController>> getListbyPropertyCode(@RequestBody FilterShipmentController filterShipmentController) {
		try {
			List<ModelShipmentController> res = serviceShipmentController.getListByPropertyCodeAndValue(filterShipmentController);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
}


