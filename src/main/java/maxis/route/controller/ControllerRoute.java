package maxis.route.controller;

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

import maxis.route.model.FilterRoute;
import maxis.route.model.ModelRoute;
import maxis.route.service.ServiceRoute;

@RestController
@RequestMapping("/route")
public class ControllerRoute {
	@Autowired
	private ServiceRoute serviceRoute;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HttpEntity<ModelRoute> add(@RequestBody ModelRoute modelRoute) {
		try {
			ModelRoute res = serviceRoute.add(modelRoute);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/getall")
	public HttpEntity<List<ModelRoute>> get(@RequestBody FilterRoute filterRoute) {
		try {
			List<ModelRoute> res = serviceRoute.getList(filterRoute);
			return new ResponseEntity<>(res, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
