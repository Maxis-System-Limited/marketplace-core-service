package maxis.jobmanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import maxis.jobmanagement.inputmodel.JobInputModel;
import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.model.FilterJob;
import maxis.jobmanagement.model.Job;
import maxis.jobmanagement.olmworkflow.CategoryStateViewModel;
import maxis.jobmanagement.olmworkflow.CategoryWiseStateActionService;
import maxis.jobmanagement.olmworkflow.CategoryWiseStateActions;
import maxis.jobmanagement.olmworkflow.OLMNextStateResponseModel;
import maxis.jobmanagement.olmworkflow.OLMRequestModel;
import maxis.jobmanagement.olmworkflow.OLMWorkflowService;
import maxis.jobmanagement.repository.JobRepository;
import maxis.jobmanagement.viewmodel.CategoryJobViewModel;
import maxis.jobmanagement.viewmodel.JobViewModel;

@Service
public class JobDomainService {
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private OLMWorkflowService oLMWorkflowService;

	@Autowired
	private CategoryWiseStateActionService categoryWiseStateActionService;

	public List<CategoryJobViewModel> getJobs(FilterJob filterJob) {

		CategoryWiseStateActions categoryWiseStateActions = new CategoryWiseStateActions();
		List<CategoryStateViewModel> categoryStateViewModel2 = new ArrayList<CategoryStateViewModel>();
		List<Job> jobs = new ArrayList<Job>();
		List<Job> totalJobs = new ArrayList<Job>();
		// Long stateId = Long.valueOf(0);
		List<CategoryJobViewModel> categoryJobViewModels = new ArrayList<CategoryJobViewModel>();

		categoryWiseStateActions = categoryWiseStateActionService.getStateActionByCategoryId(filterJob);

		for (Long stateId : categoryWiseStateActions.getData().getCategoryStates()) {
			if ((filterJob.getTanentId().toUpperCase().equals("MAXIS"))
					|| (filterJob.getTanentId().toUpperCase().equals("LP"))) {
				jobs = jobRepository.findBycurrentstateId(stateId);
			} else {
				jobs = jobRepository.getByTanentIdAndCurrentStateid(filterJob.getTanentId(), stateId);
			}
			totalJobs.addAll(jobs);
			jobs.clear();

		}

		Map<Long, List<CategoryStateViewModel>> actionsByStateId = categoryWiseStateActions.getData()
				.getCategoryStateViewModel().stream()
				.collect(Collectors.groupingBy(CategoryStateViewModel::getStateId));
		for (Job job : totalJobs) {
			if (filterJob.getStatusList().contains(job.getStatus())) {
				categoryStateViewModel2 = actionsByStateId.get(job.getCurrentstateId());
				CategoryJobViewModel categoryJobViewModel = new CategoryJobViewModel();
				categoryJobViewModel.setJob(job);
				categoryJobViewModel.setActions(categoryStateViewModel2);
				categoryJobViewModels.add(categoryJobViewModel);
			}

		}
//		for (Map.Entry<Long, List<CategoryStateViewModel>> entry : actionsByStateId.entrySet()) {
//			// System.out.println("State ID " + entry.getKey() + " Actions are " +
//			// entry.getValue());
//
//		}

		return categoryJobViewModels;
	}

	public JobViewModel modifyState(JobStateModificationInputModel jobStateModificationInputModel)
			throws JsonMappingException, JsonProcessingException {

		JobViewModel jobViewModel = new JobViewModel();
		OLMNextStateResponseModel oLMNextStateResponseModel = new OLMNextStateResponseModel();

		try {
			OLMRequestModel oLMRequestModel = new OLMRequestModel();
			oLMRequestModel.setActionEventId(jobStateModificationInputModel.getActionEventId());
			oLMRequestModel.setClassTypeId(jobStateModificationInputModel.getJob().getClassTypeId());
			oLMRequestModel.setStateId(jobStateModificationInputModel.getJob().getCurrentstateId());
			oLMRequestModel.setRoleId(getRoleIdByRolecode(jobStateModificationInputModel.getRoleCode()));
			oLMNextStateResponseModel = oLMWorkflowService.getNextAllowableState(oLMRequestModel);

			jobStateModificationInputModel.getJob()
					.setPreviousstate(jobStateModificationInputModel.getJob().getCurrentstate());
			jobStateModificationInputModel.getJob()
					.setPreviousstateId(jobStateModificationInputModel.getJob().getCurrentstateId());
			jobStateModificationInputModel.getJob()
					.setCurrentstate(oLMNextStateResponseModel.getData().getAttributes().getCode());
			jobStateModificationInputModel.getJob()
					.setCurrentstateId(oLMNextStateResponseModel.getData().getAttributes().getId());
			jobStateModificationInputModel.getJob().setModifiedAt(new Date().toString());

			jobRepository.save(jobStateModificationInputModel.getJob());

			jobViewModel.setStatus("SUCCESS");
			jobViewModel.setPreviousWarehouseId(jobStateModificationInputModel.getJob().getPreviousstateId());
			jobViewModel.setPreviousWarehouseCode(jobStateModificationInputModel.getJob().getPreviousstate());
			jobViewModel.setCurrentWarehouseCode(jobStateModificationInputModel.getJob().getCurrentstate());
			jobViewModel.setCurrentWarehouseId(jobStateModificationInputModel.getJob().getCurrentstateId());
			jobViewModel.setMessage("STATUS UPDATE FROM " + jobStateModificationInputModel.getJob().getPreviousstate()
					+ " TO " + jobStateModificationInputModel.getJob().getCurrentstate());
			jobViewModel.setId(jobStateModificationInputModel.getJob().getId());

			return jobViewModel;

		} catch (Exception e) {
			jobViewModel.setStatus("FAILED");
			jobViewModel.setMessage("STATUS UPDATE FAILED. Message: " + e.getMessage() + ". CAUSE:" + e.getCause());
			return jobViewModel;
		}
	}

	public JobViewModel modifyStateWithoutPersistence(JobStateModificationInputModel jobStateModificationInputModel)
			throws JsonMappingException, JsonProcessingException {

		JobViewModel jobViewModel = new JobViewModel();
		OLMNextStateResponseModel oLMNextStateResponseModel = new OLMNextStateResponseModel();

		try {
			OLMRequestModel oLMRequestModel = new OLMRequestModel();
			oLMRequestModel.setActionEventId(jobStateModificationInputModel.getActionEventId());
			oLMRequestModel.setClassTypeId(jobStateModificationInputModel.getJob().getClassTypeId());
			oLMRequestModel.setStateId(jobStateModificationInputModel.getJob().getCurrentstateId());
			oLMRequestModel.setRoleId(getRoleIdByRolecode(jobStateModificationInputModel.getRoleCode()));
			oLMNextStateResponseModel = oLMWorkflowService.getNextAllowableState(oLMRequestModel);

			jobStateModificationInputModel.getJob()
					.setPreviousstate(jobStateModificationInputModel.getJob().getCurrentstate());
			jobStateModificationInputModel.getJob()
					.setPreviousstateId(jobStateModificationInputModel.getJob().getCurrentstateId());
			jobStateModificationInputModel.getJob()
					.setCurrentstate(oLMNextStateResponseModel.getData().getAttributes().getCode());
			jobStateModificationInputModel.getJob()
					.setCurrentstateId(oLMNextStateResponseModel.getData().getAttributes().getId());
			jobStateModificationInputModel.getJob().setModifiedAt(new Date().toString());

			jobViewModel.setStatus("SUCCESS");
			jobViewModel.setMessage("STATUS UPDATE FROM " + jobStateModificationInputModel.getJob().getPreviousstate()
					+ " TO " + jobStateModificationInputModel.getJob().getCurrentstate());
			jobViewModel.setId(jobStateModificationInputModel.getJob().getId());

			return jobViewModel;

		} catch (Exception e) {
			jobViewModel.setStatus("FAILED");
			jobViewModel.setMessage("STATUS UPDATE FAILED. Message: " + e.getMessage() + ". CAUSE:" + e.getCause());
			return jobViewModel;
		}
	}

	public List<Job> getJobsListByTanentId(String tanentId) {
		return jobRepository.findByTanentId(tanentId);
	}

	public List<Job> getJobsListByCreatedBy(String createdBy) {
		return jobRepository.findByCreatedBy(createdBy);
	}

	public List<Job> getJobsListByAssignedRiderId(String assignedRiderId) {
		return jobRepository.findByAssignedRiderId(assignedRiderId);
	}

	public String getRoleIdByRolecode(String roleCode) {
		Map<String, String> roleIdCode = new HashMap<String, String>();
		roleIdCode.put("SYSTEM", "1");
		roleIdCode.put("MAXISELADMIN", "2");
		roleIdCode.put("MAXISELSUPPORT", "3");
		roleIdCode.put("MAXISELBILLING", "4");
		roleIdCode.put("MAXISELLPADMIN", "5");
		roleIdCode.put("MAXISELLPENTERPRISEADMIN", "6");
		roleIdCode.put("MAXISELLPENTERPRISEBM", "7");
		roleIdCode.put("MAXISELLPENTERPRISESK", "8");
		roleIdCode.put("MAXISELLPERSONNEL", "9");
		roleIdCode.put("MAXISELLPDADMIN", "10");
		roleIdCode.put("MAXISELLPDADISPATCHER", "11");
		roleIdCode.put("MAXISELLPDACCOUNTADMIN", "12");
		roleIdCode.put("MAXISELLPDACCOUNTCASHIER", "13");
		roleIdCode.put("MAXISELLPDACCOUNTACCUNTANT", "14");
		roleIdCode.put("MAXISELLPDDELIVERYADMIN", "15");
		roleIdCode.put("MAXISELLPDDELIVERYPNDC", "16");
		roleIdCode.put("MAXISELLPDDELIVERYRIDER", "17");
		roleIdCode.put("MAXISELLPSHIPMENTADMIN", "18");
		roleIdCode.put("MAXISELLPSHIPMENTC", "19");
		roleIdCode.put("MAXISELLPSHIPMENTT", "20");
		roleIdCode.put("MAXISELLPWADMIN", "21");
		roleIdCode.put("MAXISELLPWSRECEIVER", "22");
		roleIdCode.put("MAXISELLPWSISSUER", "23");
		roleIdCode.put("MAXISELLPWSKEEPER", "24");
		return roleIdCode.get(roleCode);
	}

}
