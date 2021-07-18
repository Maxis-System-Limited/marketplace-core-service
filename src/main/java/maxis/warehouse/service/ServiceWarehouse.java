package maxis.warehouse.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import maxis.common.UtilityFunction;
import maxis.common.UtilityProperty;
import maxis.common.model.Context;
import maxis.common.model.OnboardInputModel;
import maxis.common.model.Property;
import maxis.common.model.Step;
import maxis.configuration.service.ServiceConfiguration;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;
import maxis.thana.model.Thana;
import maxis.warehouse.model.FilterWarehouse;
import maxis.warehouse.model.ModelWarehouse;
import maxis.warehouse.repository.RepositoryWarehouse;

@Component
public class ServiceWarehouse {
	@Autowired
	private RepositoryWarehouse repositoryWarehouse;
	@Autowired
	private ServiceDesign serviceDesign;
	@Autowired
	private ServiceConfiguration serviceConfiguration;

	public ServiceWarehouse() {
		super();
	}

	public ModelWarehouse add(ModelWarehouse modelWarehouse) {

		modelWarehouse.setId(UUID.randomUUID().toString());
		// modelWarehouse.setCreatedDate(new Date().toString());
		// modelWarehouse.setModifiedDate(new Date().toString());

		try {
			repositoryWarehouse.save(modelWarehouse);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelWarehouse;
	}

	public List<ModelWarehouse> getList(FilterWarehouse filterWarehouse) {
		List<ModelWarehouse> modelWarehouse = new ArrayList<ModelWarehouse>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouse.getCode())) {
			modelWarehouse = repositoryWarehouse.findByCode(filterWarehouse.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouse.getTanentId())) {
			modelWarehouse = repositoryWarehouse.findByTanentId(filterWarehouse.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouse.getCreatedById())) {
			modelWarehouse = repositoryWarehouse.findByCreatedById(filterWarehouse.getCreatedById());
		} else {
			modelWarehouse = repositoryWarehouse.findAll();
		}
		return modelWarehouse;
	}

	public ModelWarehouse create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelWarehouse modelWarehouse = new ModelWarehouse();
		
		long warehouseOrderNumber = repositoryWarehouse.findAll().size()+1;
		String corelationId = UUID.randomUUID().toString();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if (onboardInputModel.getAttemptId() != null) {
			modelWarehouse = repositoryWarehouse.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelWarehouse == null) {
				ModelWarehouse newModelWarehouse = new ModelWarehouse();
				newModelWarehouse = (ModelWarehouse) UtilityProperty.initializeBaseModel(newModelWarehouse,
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPWAREHOUSE");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				if(!(warehouseOrderNumber <= 0)) {
					steps = UtilityProperty.setValue(steps, "WAREHOUSE_ORDER_ID",Long.toString(warehouseOrderNumber));
				}else {
					steps = UtilityProperty.setValue(steps, "WAREHOUSE_ORDER_ID",Long.toString(1));

				}
				newModelWarehouse.setWarehouseOrderId(new Long(warehouseOrderNumber));
				newModelWarehouse.setWarehouseOrderIdString(""+ warehouseOrderNumber);
				newModelWarehouse.setId(corelationId);
				newModelWarehouse.setAttemptId(onboardInputModel.getAttemptId());

				newModelWarehouse.setCreatedDate(new Date().toString());
				newModelWarehouse.setModifiedDate(new Date().toString());

				newModelWarehouse.setRole(modelDesign.getRole());
				newModelWarehouse.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());

				newModelWarehouse.setSteps(steps);

				try {

					repositoryWarehouse.save(newModelWarehouse);
					return newModelWarehouse;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelWarehouse update(ModelWarehouse modelWarehouse) {

		ModelWarehouse existingModelWarehouse = new ModelWarehouse();
		String password = null;

		if (modelWarehouse.getAttemptId() != null) {
			existingModelWarehouse = repositoryWarehouse.findFirstByAttemptId(modelWarehouse.getAttemptId());
			if (existingModelWarehouse != null) {
				try {

					for (Step step : existingModelWarehouse.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelWarehouse.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryWarehouse.save(modelWarehouse);
					return modelWarehouse;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelWarehouse> getListByPropertyCodeAndValue(FilterWarehouse filterWarehouse) {
		List<ModelWarehouse> modelWarehouseList = new ArrayList<ModelWarehouse>();
		List<ModelWarehouse> modifedModelWarehouseList = new ArrayList<ModelWarehouse>();
		boolean isRecordFound = false;
		if (filterWarehouse.getTanentId() != null) {
			modelWarehouseList = repositoryWarehouse.getByTanentIdAndRoleId(filterWarehouse.getTanentId(),
					filterWarehouse.getRoleId());
		}

		if (modelWarehouseList.size() > 0) {
			for (ModelWarehouse modelWarehouse : modelWarehouseList) {
				for (Step step : modelWarehouse.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterWarehouse.getPropertyCode()) && 
									property.getPropertyValue().equals(filterWarehouse.getPropertyValue())) {
								modifedModelWarehouseList.add(modelWarehouse);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelWarehouseList;
	}

	public ModelWarehouse getModelWarehouseByThanaDistrictFromPropeortyList(String logisticPartnerId, String thanaId)
	{
		ModelWarehouse returnModelWarehouse = new ModelWarehouse();
		List<ModelWarehouse> modelWarehouseList = repositoryWarehouse.findByTanentId(logisticPartnerId);
		
		for(ModelWarehouse currentModelWarehouse : modelWarehouseList)
		{
			ArrayList<Thana> thanaList = serviceConfiguration.get_W_ThanaList(currentModelWarehouse.getCode());
			
			for(Thana currentThana : thanaList)
			{
				if (currentThana.getId().equals(thanaId))
					return currentModelWarehouse;
			}
		}
		
		return returnModelWarehouse;
	}
}
