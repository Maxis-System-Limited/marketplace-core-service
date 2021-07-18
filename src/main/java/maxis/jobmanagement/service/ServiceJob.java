package maxis.jobmanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.common.UtilityFunction;
import maxis.common.UtilityProperty;
import maxis.configuration.service.ServiceConfiguration;
import maxis.enterprise.model.ModelEnterprise;
import maxis.enterprise.repository.RepositoryEnterprise;
import maxis.jobmanagement.configuration.JobStatusConfiguration;
import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.model.FilterJob;
import maxis.jobmanagement.model.Job;
import maxis.jobmanagement.olmworkflow.CategoryWiseStateActionService;
import maxis.jobmanagement.olmworkflow.OLMNextStateResponseModel;
import maxis.jobmanagement.olmworkflow.OLMRequestModel;
import maxis.jobmanagement.olmworkflow.OLMWorkflowService;
import maxis.jobmanagement.repository.JobRepository;
import maxis.jobmanagement.viewmodel.CategoryJobViewModel;
import maxis.jobmanagement.viewmodel.JobViewModel;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.repository.RepositoryWarehouse;

@Service
public class ServiceJob {

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private OLMWorkflowService oLMWorkflowService;

	@Autowired
	private CategoryWiseStateActionService categoryWiseStateActionService;
	@Autowired
	private JobDomainService jobDomainService;
	
	@Autowired
	private RepositoryEnterprise repositoryEnterprise;

	@Autowired
	private CustomAssignOneBySystem customAssignOneBySystem;

	@Autowired
	private RepositoryWarehouse repositoryWarehouse;
	
	@Autowired
	private ServiceConfiguration serviceConfiguration;
	
	
	public JobViewModel add(Job job) {
		JobViewModel jobViewModel = new JobViewModel();
		Random r = new Random();
		int n = r.nextInt();
		String roleId = jobDomainService.getRoleIdByRolecode(job.getRoleCode());
		String randomCode = Integer.toHexString(n);
		
		try {
			if(job.getId() == null) {
				// Workflow
				OLMNextStateResponseModel oLMNextStateResponseModel = new OLMNextStateResponseModel();
				/// Bring Action Event Id from OLM using Class Type ID,Role ID, State Id which
				/// is Start
				OLMRequestModel oLMRequestModel = new OLMRequestModel();
				oLMRequestModel.setActionEventId(101);
				oLMRequestModel.setClassTypeId(job.getClassTypeId());
				oLMRequestModel.setStateId(1);
				oLMRequestModel.setRoleId(roleId);
				oLMNextStateResponseModel = oLMWorkflowService.getNextAllowableState(oLMRequestModel);

				// Job Property Update
				job.setId(UUID.randomUUID().toString());
				job.setCreatedAt(new Date().toString());
				job.setCode(randomCode);

				// Check CUSTOMER_DELIVERY_FROM_ENTERPRISE_VIA_RIDER or
				// WAREHOUSE_COLLECTION_FROM_ENTERPRISE_VIA_RIDER

				job.setDisplayCurrentState(oLMNextStateResponseModel.getData().getAttributes().getName());
				job.setCurrentstate(oLMNextStateResponseModel.getData().getAttributes().getCode());
				job.setCurrentstateId(oLMNextStateResponseModel.getData().getAttributes().getId());
				job.setRoleIdOfCreatedById(roleId);
				job.setPreviousstate("START");
				job.setPreviousstateId(Long.valueOf(1));

				// Check Logistic Partner
				if (!job.getSourcing().equals("MAXIS")) {
					JobStateModificationInputModel jobStateModificationInputModel = new JobStateModificationInputModel();
					jobStateModificationInputModel.setActionEventId(Long.valueOf(102));
					jobStateModificationInputModel.setRoleId("1");
					jobStateModificationInputModel.setJob(job);
					jobStateModificationInputModel.setRoleCode("SYSTEM");
					jobStateModificationInputModel.setTanentId(job.getTanentId());
					jobViewModel = customAssignOneBySystem.performActions(jobStateModificationInputModel);
				}else {
					job.setStatus(JobStatusConfiguration.WAITING_FOR_LOGISTIC_PARTNER_ASSIGNMENT);
				}
			}
			jobRepository.save(job);

			jobViewModel.setStatus("SUCCESS");
			jobViewModel.setMessage("STATUS UPDATE FROM " + job.getPreviousstate() + " TO " + job.getCurrentstate());
			jobViewModel.setId(job.getId());

		} catch (Exception e) {
			jobViewModel.setStatus("FAILED");
			jobViewModel.setMessage("MESSAGE: " + e.getMessage() + ". CAUSE: " + e.getCause());

		}

		return jobViewModel;

	}

	public JobViewModel addDemoJob(Job job) {
		JobViewModel jobViewModel = new JobViewModel();
		Random r = new Random();
		int n = r.nextInt();
		boolean x = true;
		String randomCode = Integer.toHexString(n);

		try {

			// Job Property Update
			job.setId(UUID.randomUUID().toString());
			job.setCreatedAt(new Date().toString());
			job.setCode(randomCode);

			jobRepository.save(job);

			jobViewModel.setStatus("SUCCESS");
			jobViewModel.setId(job.getId());

		} catch (Exception e) {
			jobViewModel.setStatus("FAILED");
			jobViewModel.setMessage("MESSAGE: " + e.getMessage() + ". CAUSE: " + e.getCause());

		}

		return jobViewModel;

	}

	

	public Job Edit(Job job) {
		job.setModifiedAt(new Date().toString());
		try {
			jobRepository.save(job);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return job;
	}

	public List<Job> getList(FilterJob filterJob) {
		List<Job> Job = new ArrayList<Job>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterJob.getCode())) {
			Job = jobRepository.findByCode(filterJob.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterJob.getTanentId())) {
			Job = jobRepository.findByTanentId(filterJob.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterJob.getCreatedById())) {
			Job = jobRepository.findByCreatedById(filterJob.getCreatedById());
		} else {
			Job = jobRepository.findAll();
		}
		return Job;
	}

	public List<CategoryJobViewModel> getJobByCategoryIdAndStatusList(FilterJob filterJob) {

		return jobDomainService.getJobs(filterJob);
	}
	
	
	
	
	

}
