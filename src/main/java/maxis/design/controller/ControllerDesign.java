package maxis.design.controller;

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

import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;

@RestController
@RequestMapping("/design")
public class ControllerDesign {
	@Autowired
	private ServiceDesign serviceDesign;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelDesign> add(@RequestBody ModelDesign modelDesign) {
		try {
			ModelDesign res = serviceDesign.add(modelDesign);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/get")
	public HttpEntity<List<ModelDesign>> get(@RequestBody FilterDesign filterDesign) {
		try {
			List<ModelDesign> res = serviceDesign.getList(filterDesign);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}


