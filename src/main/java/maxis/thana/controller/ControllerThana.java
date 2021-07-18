package maxis.thana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import maxis.thana.model.DistrictListInputModel;
import maxis.thana.model.ThanaInputModel;
import maxis.thana.model.ThanaListViewModel;
import maxis.thana.model.ThanaViewModel;
import maxis.thana.service.ServiceThana;


@RestController
@RequestMapping("/api")
public class ControllerThana {
	@Autowired
	private ServiceThana thanaService;

	@RequestMapping(value = "/thana", method = RequestMethod.POST)
	public ThanaViewModel add(@RequestBody ThanaInputModel thanaInputModel) {
		return thanaService.add(thanaInputModel);
	}
	
	@RequestMapping(value = "/thana", method = RequestMethod.GET)
	public ThanaListViewModel getThanaListByDistrictId(@RequestParam String districtId) {
		return thanaService.getThanasByDistrictId(districtId);
	}
	
	@RequestMapping(value = "/thana-all", method = RequestMethod.GET)
	public ThanaListViewModel getThanaList() {
		return thanaService.getThanas();
	}
	
	@RequestMapping(value = "/thanas-by-districtId", method = RequestMethod.POST)
	public ThanaListViewModel getThanaListByMultipleDistrict(@RequestBody DistrictListInputModel districtIds) {
		return thanaService.getThanaListByMultipleDistrict(districtIds);
	}
}
