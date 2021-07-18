package maxis.district.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.district.model.District;
import maxis.district.model.DistrictInputModel;
import maxis.district.model.DistrictListViewModel;
import maxis.district.model.DistrictViewModel;
import maxis.district.repository.RepositoryDistrict;
@Service
public class ServiceDistrict {
	@Autowired
	private RepositoryDistrict districtRepository;

	public DistrictViewModel add(DistrictInputModel districtInputModel) {
		DistrictViewModel districtViewModel = new DistrictViewModel();
		District district = new District();
		try {
			district.setId(UUID.randomUUID().toString());
			district.setCreatedTime(new Date().toString());
			district.setModifiedTime(new Date().toString());
			district.setCode(districtInputModel.getCode());
			district.setCreatedBy(districtInputModel.getCreatedBy());
			district.setDisplayName(districtInputModel.getDisplayName());
			district.setModifiedBy(districtInputModel.getModifiedBy());
			district.setName(districtInputModel.getName());
			
			districtRepository.insert(district);

			districtViewModel.setId(district.getId());
			districtViewModel.setStatus("SUCCESS");
			districtViewModel.setMessage("District Added With ID: " + district.getId());
			return districtViewModel;
		} catch (Exception e) {
			districtViewModel.setId(district.getId());
			districtViewModel.setStatus("FAILED");
			districtViewModel.setMessage(e.getMessage() + e.getCause());
			return districtViewModel;
		}
	}

	public DistrictListViewModel getAll() {
		DistrictListViewModel districtListViewModel = new DistrictListViewModel();
		try {
			districtListViewModel.setDistricts(districtRepository.findAll());
			districtListViewModel.setStatus("SUCCESS");
			districtListViewModel.setMessage(districtRepository.findAll().size() + " District Retrived.");
			districtListViewModel.setId("Event Id");
			return districtListViewModel;
		} catch (Exception e) {
			districtListViewModel.setStatus("FAILED");
			districtListViewModel.setMessage(e.getMessage() + " " + e.getCause());
			return districtListViewModel;
		}

	}
}
