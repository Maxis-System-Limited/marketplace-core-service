package maxis.jobmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.model.FilterJob;
import maxis.jobmanagement.model.Job;
import maxis.jobmanagement.service.ActionsFactory;
import maxis.jobmanagement.service.BundleService;
import maxis.jobmanagement.service.ServiceJob;
import maxis.jobmanagement.viewmodel.BundleViewModel;
import maxis.jobmanagement.viewmodel.CategoryJobViewModel;
import maxis.jobmanagement.viewmodel.JobViewModel;

@RestController
@RequestMapping("/api")
public class JobController {
	@Autowired
	private ServiceJob jobService;
	
	@Autowired
	private ActionsFactory actionsFactory;
	
	@Autowired
	private BundleService bundleService;
	
	

	@RequestMapping(value = "/job", method = RequestMethod.POST)
	public JobViewModel add(@RequestBody Job Job) {
		return jobService.add(Job);
	}
	
	@RequestMapping(value = "/jobdemo", method = RequestMethod.POST)
	public JobViewModel addDemo(@RequestBody Job Job) {
		return jobService.addDemoJob(Job);
	}
	
	
	@RequestMapping(value = "/jobs", method = RequestMethod.POST)
	public List<Job> add(@RequestBody FilterJob filterJob) {
		return jobService.getList(filterJob);
	}

	@RequestMapping(value = "/jobsbystatus", method = RequestMethod.POST)
	public List<CategoryJobViewModel> getjobsbystatus(@RequestBody FilterJob filterJob) {
		return jobService.getJobByCategoryIdAndStatusList(filterJob);
	}
	
	
//	@RequestMapping(value = "/routebyjobid", method = RequestMethod.POST)
//	public GeneratedRoute getRoute(@RequestBody String jobId) {
//		return generatedRouteService.get(jobId);
//	}
//	@RequestMapping(value = "/job", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobListByCategory(@RequestParam Long id) {
//		return jobService.getJobsByCategoryId(id);
//	}
//
//	@RequestMapping(value = "/category-assignedrider", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobListByCategory(@RequestParam Long id, String assignedRiderId) {
//		return jobService.getJobsByCategoryIdAndRoleId(id, assignedRiderId);
//	}
//
	@RequestMapping(value = "/action-event", method = RequestMethod.POST)
	public JobViewModel add(@RequestBody JobStateModificationInputModel jobStateModificationInputModel)
			throws JsonMappingException, JsonProcessingException {
		return actionsFactory.modifyJob(jobStateModificationInputModel);
	}
	
	
	@RequestMapping(value = "/bundle", method = RequestMethod.POST)
	public BundleViewModel add(@RequestBody List<JobStateModificationInputModel> jobStateModificationInputModels)
			throws JsonMappingException, JsonProcessingException {
		return bundleService.create(jobStateModificationInputModels);
	}
//
//	@RequestMapping(value = "/merchant-job", method = RequestMethod.GET)
//	public JobListViewModel getJobsListByCreatedById(@RequestParam String createdBy) {
//		return jobService.getJobsListByCreatedBy(createdBy);
//	}
//
//	@RequestMapping(value = "/tanent-job", method = RequestMethod.GET)
//	public JobListViewModel getJobListByTanentId(@RequestParam String tanentId) {
//		return jobService.getJobsListByTanentId(tanentId);
//	}
//
//	@RequestMapping(value = "/rider-job", method = RequestMethod.GET)
//	public JobListViewModel getJobListByAssignedRiderId(@RequestParam String assignedRiderId) {
//		return jobService.getJobsListByAssignedRiderId(assignedRiderId);
//	}
//
//	@RequestMapping(value = "/category-status-warehouse", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCategoryIdAndStatusAndReceivedWarehouseId(Long id, String status,
//			String receivedWarehouseId) {
//		return jobService.getJobsByCatetoryIdAndStatusAndReceivedWarehouseId(id, status, receivedWarehouseId);
//	}
//
//	@RequestMapping(value = "/category-destination-warehouse", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCategoryIdAndDeliveryChannelAndReceivedWarehouseId(Long id,
//			String deliveryChannel, String receivedWarehouseId) {
//		return jobService.getJobsByCatetoryIdAndDeliveryChannelAndReceivedWarehouseId(id, deliveryChannel,
//				receivedWarehouseId);
//	}
//
//	@RequestMapping(value = "/category-status-createdby", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCategoryIdAndStatusAndCreatedBy(Long id, String status, String createdBy) {
//		return jobService.getJobsByCatetoryIdAndStatusAndCreatedBy(id, status, createdBy);
//	}
//
//	@RequestMapping(value = "/category-createdby", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCategoryIdAndCreatedBy(Long id, String createdBy) {
//		return jobService.getJobsByCatetoryIdAndCreatedBy(id, createdBy);
//	}
//
//	@RequestMapping(value = "/category-status", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCategoryIdAndStatus(Long id, String status) {
//		return jobService.getJobsByCatetoryIdAndStatus(id, status);
//	}
//
//	@RequestMapping(value = "/category-destination", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCategoryIdAndDeliveryChannelAndAssignedRiderId(Long id, String status,
//			String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdAndDeliveryChannelAndAssignedRiderId(id, status, assignedRiderId);
//	}
//
//	@RequestMapping(value = "/warehouse-receivable", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryForWarehouseReceivable(Long id) {
//		return jobService.getJobsByCatetoryForWarehouseReceivable(id);
//	}
//
//	@RequestMapping(value = "/warehouse-stockin", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryForWarehouseStockIn(Long id) {
//		return jobService.getJobsByCatetoryForWarehouseStockIn(id);
//	}
//
//	@RequestMapping(value = "/warehouse-delivery", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryForWarehouseDelivery(Long id) {
//		return jobService.getJobsByCatetoryForWarehouseDelivery(id);
//	}
//
//	@RequestMapping(value = "/merchant-receivable", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryForMerchantReceivable(Long id, String createdBy) {
//		return jobService.getJobsByCatetoryForMerchantReceivable(id, createdBy);
//	}
//
//	@RequestMapping(value = "/merchant-stockin", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryForMerchantStockIn(Long id, String createdBy) {
//		return jobService.getJobsByCatetoryForMerchantStockIn(id, createdBy);
//	}
//
//	@RequestMapping(value = "/merchant-delivery", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryForMerchantDelivery(Long id, String createdBy) {
//		return jobService.getJobsByCatetoryForMerchantDelivery(id, createdBy);
//	}
//
//	@RequestMapping(value = "/dispatcher-merchant", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForDispatcherMerchant(Long id, String status) {
//		return jobService.getJobsByCatetoryIdForDispatcherMerchant(id, status);
//	}
//
//	@RequestMapping(value = "/dispatcher-warehouse", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForDispatcherWarehouse(Long id, String status) {
//		return jobService.getJobsByCatetoryIdForDispatcherWarehouse(id, status);
//	}
//
//	@RequestMapping(value = "/rider-receivable", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForRiderReceivable(Long id, String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdForRiderReceivable(id, assignedRiderId);
//	}
//
//	@RequestMapping(value = "/rider-stockinhand", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForRiderStockInHand(Long id, String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdForRiderStockInHand(id, assignedRiderId);
//	}
//
//	@RequestMapping(value = "/rider-warehouse-delivery", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForRiderWarehouseDelivery(Long id, String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdForRiderWarehouseDelivery(id, assignedRiderId);
//	}
//
//	@RequestMapping(value = "/rider-customer-delivery", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForRiderCustomerDelivery(Long id, String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdForRiderCustomerDelivery(id, assignedRiderId);
//	}
//
//	@RequestMapping(value = "/rider-merchant-delivery", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForRiderMerchantDelivery(Long id, String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdForRiderMerchantDelivery(id, assignedRiderId);
//	}
//	@RequestMapping(value = "/customer-receivable", method = RequestMethod.GET)
//	public CategoryJobListViewModel getJobsByCatetoryIdForCustomerReceivable(Long id,String assignedRiderId) {
//		return jobService.getJobsByCatetoryIdForCustomerReceivable(id,assignedRiderId);
//	}
}
