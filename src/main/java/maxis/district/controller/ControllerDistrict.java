package maxis.district.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import maxis.district.model.DistrictInputModel;
import maxis.district.model.DistrictListViewModel;
import maxis.district.model.DistrictViewModel;
import maxis.district.service.ServiceDistrict;


@RestController
@RequestMapping("/api")
public class ControllerDistrict {
	@Autowired
	private ServiceDistrict districtService;

	@RequestMapping(value = "/district", method = RequestMethod.POST)
	public DistrictViewModel add(@RequestBody DistrictInputModel districtInputModel) {
		return districtService.add(districtInputModel);
	}
	
	@RequestMapping(value = "/district", method = RequestMethod.GET)
	public DistrictListViewModel getAll() {
		return districtService.getAll();
	}
	
}
