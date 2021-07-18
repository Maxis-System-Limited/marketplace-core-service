package maxis.jobmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.Getter;
import lombok.Setter;
import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.viewmodel.JobViewModel;

@Getter
@Setter
abstract class CommonPerformableActions implements IPerformable {
	@Autowired
	protected final JobDomainService jobDomainService;
	
	@Autowired
    public CommonPerformableActions(JobDomainService jobDomainService) {
        this.jobDomainService = jobDomainService;            
    }

	public JobViewModel performActions(JobStateModificationInputModel jobStateModificationInputModel) throws JsonMappingException, JsonProcessingException {
		return jobDomainService.modifyState(jobStateModificationInputModel);
	}

}
