package maxis.jobmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import maxis.common.UtilityProperty;
import maxis.configuration.repository.RepositoryConfiguration;
import maxis.configuration.service.ServiceConfiguration;
import maxis.enterprise.model.ModelEnterprise;
import maxis.enterprise.repository.RepositoryEnterprise;
import maxis.jobmanagement.configuration.JobStatusConfiguration;
import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.model.Job;
import maxis.jobmanagement.model.RouteWarehouse;
import maxis.jobmanagement.viewmodel.JobViewModel;
import maxis.route.model.Edge;
import maxis.route.model.Route;
import maxis.route.service.ServiceRoute;
import maxis.thana.model.Thana;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.repository.RepositoryWarehouse;

@Service
public class CustomAssignOneBySystem extends CommonPerformableActions {
	@Autowired
	private RepositoryEnterprise repositoryEnterprise;

	@Autowired
	private RepositoryConfiguration repositoryConfiguration;

	@Autowired
	private ServiceConfiguration serviceConfiguration;

	@Autowired
	private RepositoryWarehouse repositoryWarehouse;
	@Autowired
	private ServiceRoute serviceRoute;

	public CustomAssignOneBySystem(JobDomainService jobDomainService) {
		super(jobDomainService);

	}

	@Override
	public JobViewModel performActions(JobStateModificationInputModel jobStateModificationInputModel)
			throws JsonMappingException, JsonProcessingException {
		updateDeliveryDestinationStatus(jobStateModificationInputModel.getJob());
		updateJobRoute(jobStateModificationInputModel.getJob());
		if (jobStateModificationInputModel.getRoleCode().equals("SYSTEM")
				&& jobStateModificationInputModel.getJob().getLogisticPartnerId() != null) {
			return jobDomainService.modifyStateWithoutPersistence(jobStateModificationInputModel);
		} else {
			return jobDomainService.modifyState(jobStateModificationInputModel);
		}

	}

	private void updateDeliveryDestinationStatus(Job job) {
		boolean isPickUpThanaExist = false;
		boolean isDropThanaExist = false;
		List<ModelEnterprise> enterprise = new ArrayList<>();
		List<Thana> warehouseThanaList = new ArrayList<Thana>();
		enterprise = repositoryEnterprise.findByCode(job.getTanentId());

		warehouseThanaList = serviceConfiguration.get_W_ThanaList(enterprise.get(0).getTaggedWarehouse().getCode());

		for (Thana thana : warehouseThanaList) {

			if (thana.getCode().equals(job.getPickUpThana().getCode())) {
				isPickUpThanaExist = true;
			}
			if (thana.getCode().equals(job.getDropOffThana().getCode())) {
				isDropThanaExist = true;
			}
		}

		if (isPickUpThanaExist && isDropThanaExist) {
			job.setStatus(JobStatusConfiguration.CUSTOMER_DELIVERY_FROM_ENTERPRISE_VIA_RIDER);
		} else if (isPickUpThanaExist) {
			job.setStatus(JobStatusConfiguration.WAREHOUSE_COLLECTION_FROM_ENTERPRISE_VIA_RIDER);
		} else {
			job.setStatus(JobStatusConfiguration.JOB_OUT_OF_WAREHOUSE_SERVICE_AREA);
		}
	}

	private void updateJobRoute(Job job) {
		List<ModelEnterprise> enterprise = new ArrayList<>();
		List<ModelWarehouse> modelWarehouses = new ArrayList<>();
		List<RouteWarehouse> plannedRoute = new ArrayList<>();
		String WarehouseCode;
		Long sourceWarehouseId;
		Long destinationWarehouseId;
		if (job.getPickUpThana() != null && job.getDropOffThana() != null) {
			enterprise = repositoryEnterprise.findByCode(job.getEnterpriseCode());
			sourceWarehouseId = Long.valueOf(UtilityProperty.getValueProperties(
					enterprise.get(0).getTaggedWarehouse().getSteps().get(0).getContext().get(0).getProperties(),
					"WAREHOUSE_ORDER_ID"));
			WarehouseCode = serviceConfiguration.get_W_From_ThanaList(
					enterprise.get(0).getTaggedWarehouse().getTanentId(), job.getDropOffThana().getCode());
			if (WarehouseCode != null) {
				modelWarehouses = repositoryWarehouse.findByCode(WarehouseCode);
			}
			destinationWarehouseId = Long.valueOf(UtilityProperty.getValueProperties(
					modelWarehouses.get(0).getSteps().get(0).getContext().get(0).getProperties(),
					"WAREHOUSE_ORDER_ID"));
			job.setSourceWarehouseOrderId(sourceWarehouseId);
			job.setDestinationWarehouseOrderId(destinationWarehouseId);
			job.setSourceWarehouse(enterprise.get(0).getTaggedWarehouse());
			job.setDestinationWarehouse(modelWarehouses.get(0));
			//For Same Warehouse
			if (sourceWarehouseId.equals(destinationWarehouseId)) {
				RouteWarehouse routeWarehouse = new RouteWarehouse();
				routeWarehouse.setExplored(false);
				routeWarehouse.setWarehouseOrderId(sourceWarehouseId);
				routeWarehouse.setOrderNo(Long.valueOf(1));
				routeWarehouse.setModelWarehouse(modelWarehouses.get(0));
				plannedRoute.add(routeWarehouse);
				job.setPlannedRoute(plannedRoute);
			} else {
			//For Different Warehouse
				List<Route> routes = new ArrayList<>();
				routes = serviceRoute.findDeliveryRoute(job.getLogisticPartnerCode(),sourceWarehouseId.intValue(),destinationWarehouseId.intValue());
				for(Route route : routes) {
					System.out.println("ROUTE ORDER: "+route.getOrderNo()+" - ROUTE NAME: "+route.getModelRoute().getName());
					for(Edge edge : route.getEdges()) {
						System.out.println("From: "+edge.getSourceNode()+" To: "+edge.getDestinationNode());
					}
				}
				
			}
		}
	}

}
