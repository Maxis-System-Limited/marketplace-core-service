package maxis.jobmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.jobmanagement.model.StatusDetails;
import maxis.jobmanagement.repository.StatusDetailsRepository;

@Service
public class StatusDetailsDomainService {
	@Autowired
	private StatusDetailsRepository statusDetailsRepository;
	
	public StatusDetails getStatusDetailsByClassTypeIdAndCode(Long classTypeId, String code) {
		
		return statusDetailsRepository.getStatusDetailsByClassTypeIdAndCode(classTypeId, code);

	}
	
	
}
