package maxis.jobmanagement.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import maxis.jobmanagement.configuration.JobStatusConfiguration;
import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.model.Bundle;
import maxis.jobmanagement.olmworkflow.OLMNextStateResponseModel;
import maxis.jobmanagement.olmworkflow.OLMRequestModel;
import maxis.jobmanagement.olmworkflow.OLMWorkflowService;
import maxis.jobmanagement.repository.BundleRepository;
import maxis.jobmanagement.viewmodel.BundleViewModel;
import maxis.jobmanagement.viewmodel.JobViewModel;

@Service
public class BundleService {
	
	@Autowired
	private JobDomainService jobDomainService;
	@Autowired
	private OLMWorkflowService oLMWorkflowService;
	@Autowired
	private BundleRepository bundleRepository;
	
	
	public BundleViewModel create(List<JobStateModificationInputModel> jobStateModificationInputModels) throws JsonMappingException, JsonProcessingException {
		BundleViewModel bundleViewModel = new BundleViewModel();
		Bundle bundle = new Bundle();
		String bundleId = UUID.randomUUID().toString();
		String roleId;
		String randomCode = Integer.toHexString(new Random().nextInt());
		try {
		for(JobStateModificationInputModel jobStateModificationInputModel : jobStateModificationInputModels) {
			JobViewModel jobViewModel = new JobViewModel();
			roleId = jobDomainService.getRoleIdByRolecode(jobStateModificationInputModel.getRoleCode());
			jobStateModificationInputModel.setActionEventId(JobStatusConfiguration.BUNDLE_CREATION_ACTION_EVENT_ID);
			jobStateModificationInputModel.setRoleId(roleId);
			jobStateModificationInputModel.getJob().setShipmentBundleId(bundleId);
			
			jobViewModel = jobDomainService.modifyState(jobStateModificationInputModel);
			if(bundle.getCurrentStateId() == null && bundle.getPreviousStateId() == null) {
				bundle.setId(bundleId);
				bundle.setCurrentStateId(jobViewModel.getCurrentWarehouseId());
				bundle.setCurrentStateCode(jobViewModel.getCurrentWarehouseCode());
				bundle.setPreviousStateId(jobViewModel.getPreviousWarehouseId());
				bundle.setPreviousStateCode(jobViewModel.getPreviousWarehouseCode());
				bundle.setCode(randomCode);
				
				bundleViewModel.setPreviousStateId(jobStateModificationInputModel.getJob().getPreviousstateId());
				bundleViewModel.setPreviousStateCode(jobStateModificationInputModel.getJob().getPreviousstate());
				bundleViewModel.setCurrentStateCode(jobStateModificationInputModel.getJob().getCurrentstate());
				bundleViewModel.setCurrentStateId(jobStateModificationInputModel.getJob().getCurrentstateId());
				bundleViewModel.setMessage("STATUS UPDATE FROM " + jobStateModificationInputModel.getJob().getPreviousstate()
						+ " TO " + jobStateModificationInputModel.getJob().getCurrentstate());
				bundleViewModel.setId(jobStateModificationInputModel.getJob().getId());
				
			}
		}
		bundle.setNumberOfJobs(Long.valueOf(jobStateModificationInputModels.size()));
		bundleRepository.save(bundle);
		
		bundleViewModel.setStatus("SUCCESS");
		return bundleViewModel;

	} catch (Exception e) {
		bundleViewModel.setStatus("FAILED");
		bundleViewModel.setMessage("STATUS UPDATE FAILED. Message: " + e.getMessage() + ". CAUSE:" + e.getCause());
		return bundleViewModel;
	}
		

	}
	
	private Bundle getBundleNextState(Long actionEventId,Long classTypeId, Long currentStateId, String roleId,Bundle bundle) throws JsonMappingException, JsonProcessingException {
		
		OLMNextStateResponseModel oLMNextStateResponseModel = new OLMNextStateResponseModel();
		/// Bring Action Event Id from OLM using Class Type ID,Role ID, State Id which
		/// is Start
		OLMRequestModel oLMRequestModel = new OLMRequestModel();
		oLMRequestModel.setActionEventId(actionEventId);
		oLMRequestModel.setClassTypeId(classTypeId);
		oLMRequestModel.setStateId(currentStateId);
		oLMRequestModel.setRoleId(roleId);
		oLMNextStateResponseModel = oLMWorkflowService.getNextAllowableState(oLMRequestModel);
		bundle.setCurrentStateId(oLMNextStateResponseModel.getData().getAttributes().getId());
		bundle.setCurrentStateCode(oLMNextStateResponseModel.getData().getAttributes().getCode());
		return bundle;
	}
	
}
