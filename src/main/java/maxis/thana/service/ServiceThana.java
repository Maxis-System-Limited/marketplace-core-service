package maxis.thana.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.thana.model.DistrictListInputModel;
import maxis.thana.model.Thana;
import maxis.thana.model.ThanaInputModel;
import maxis.thana.model.ThanaListViewModel;
import maxis.thana.model.ThanaViewModel;
import maxis.thana.repository.RepositoryThana;
@Service
public class ServiceThana {
	@Autowired
	private RepositoryThana thanaRepository;

	public ThanaViewModel add(ThanaInputModel thanaInputModel) {
		Thana thana = new Thana();
		ThanaViewModel thanaViewModel = new ThanaViewModel();
		try {
			thana.setCode(thanaInputModel.getCode());
			thana.setCreatedBy(thanaInputModel.getCreatedBy());
			thana.setCreatedTime(new Date().toString());
			thana.setDisplayName(thanaInputModel.getDisplayName());
			thana.setDistrictId(thanaInputModel.getDistrictId());
			thana.setId(UUID.randomUUID().toString());
			thana.setModifiedBy(thanaInputModel.getModifiedBy());
			thana.setModifiedTime(new Date().toString());
			thana.setName(thanaInputModel.getName());
			
			// Save To DB
			thanaRepository.insert(thana);
			// Populate Response Model
			thanaViewModel.setId(thana.getId());
			thanaViewModel.setStatus("SUCCESS");
			thanaViewModel.setMessage("Thana Added with id: " + thana.getId());
			return thanaViewModel;
		} catch (Exception e) {

			thanaViewModel.setStatus("FAILED");
			thanaViewModel.setMessage("Message: " + e.getMessage() + "  Cause:" + e.getCause());
			return thanaViewModel;
		}
	}
	public ThanaListViewModel getThanasByDistrictId(String districtId) {
		ThanaListViewModel thanaListViewModel = new ThanaListViewModel();
		try {
			thanaListViewModel.setThanas(thanaRepository.findAllBydistrictId(districtId));
			thanaListViewModel.setStatus("SUCCESS");
			thanaListViewModel.setId("Event Id");
			thanaListViewModel.setMessage(thanaListViewModel.getThanas().size()+ " Thana Retrived");
			return thanaListViewModel;
		}catch(Exception e) {
			thanaListViewModel.setStatus("FAILED");
			thanaListViewModel.setMessage("Message: "+e.getMessage()+ "Cause:"+ e.getCause());
			return thanaListViewModel;
		}
		
	}
	
	
	public ThanaListViewModel getThanaListByMultipleDistrict(DistrictListInputModel districtIds) {
		ThanaListViewModel thanaListViewModel = new ThanaListViewModel();
		List<Thana> thanas = new ArrayList<Thana>(); 
		try {
		for(String districtId : districtIds.getDistrictIds()) {
			thanaListViewModel = getThanasByDistrictId(districtId);
			for(Thana t : thanaListViewModel.getThanas()) {
				thanas.add(t);
			}
		}
		thanaListViewModel.setThanas(thanas);
		
		return thanaListViewModel;
		}catch(Exception e) {
			thanaListViewModel.setStatus("FAILED");
			thanaListViewModel.setMessage(e.getStackTrace().toString());
			return thanaListViewModel;
		}
	}
	public ThanaListViewModel getThanas() {
		ThanaListViewModel thanaListViewModel = new ThanaListViewModel();
		
		try {
			List<Thana> thanaList = thanaRepository.findAll();
			thanaListViewModel.setThanas(thanaList);
		
			return thanaListViewModel;
		}catch(Exception e) {
			thanaListViewModel.setStatus("FAILED");
			thanaListViewModel.setMessage(e.getStackTrace().toString());
			return thanaListViewModel;
		}
	}
}
