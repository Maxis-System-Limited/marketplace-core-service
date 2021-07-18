package maxis.jobmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import maxis.jobmanagement.model.StatusDetails;

public interface StatusDetailsRepository  extends MongoRepository<StatusDetails, String>{
	
	@Query("{'classTypeId' : ?0 , 'code' : ?1}")
	public StatusDetails getStatusDetailsByClassTypeIdAndCode(Long classTypeId, String code);
	
}
