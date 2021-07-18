package maxis.jobmanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.viewmodel.JobViewModel;

public interface IPerformable {
	public JobViewModel performActions(JobStateModificationInputModel jobStateModificationInputModel)throws JsonMappingException, JsonProcessingException ;

}
