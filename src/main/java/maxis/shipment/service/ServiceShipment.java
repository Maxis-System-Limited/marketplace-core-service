package maxis.shipment.service;

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
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;
import maxis.shipment.model.FilterShipment;
import maxis.shipment.model.ModelShipment;
import maxis.shipment.repository.RepositoryShipment;

@Component
public class ServiceShipment {

	@Autowired
	private RepositoryShipment repositoryShipment;
	@Autowired
	private ServiceDesign serviceDesign;

	public ServiceShipment() {
		super();
	}

	public ModelShipment add(ModelShipment modelShipment) {

		modelShipment.setId(UUID.randomUUID().toString());
		// modelShipment.setCreatedDate(new Date().toString());
		// modelShipment.setModifiedDate(new Date().toString());

		try {
			repositoryShipment.save(modelShipment);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelShipment;
	}

	public List<ModelShipment> getList(FilterShipment filterShipment) {
		List<ModelShipment> modelShipment = new ArrayList<ModelShipment>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterShipment.getCode())) {
			modelShipment = repositoryShipment.findByCode(filterShipment.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterShipment.getTanentId())) {
			modelShipment = repositoryShipment.findByTanentId(filterShipment.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterShipment.getCreatedById())) {
			modelShipment = repositoryShipment.findByCreatedById(filterShipment.getCreatedById());
		} else {
			modelShipment = repositoryShipment.findAll();
		}
		return modelShipment;
	}

	public ModelShipment create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelShipment modelShipment = new ModelShipment();
		String corelationId = UUID.randomUUID().toString();
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelShipment = repositoryShipment.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelShipment == null) {
				ModelShipment newModelShipment = new ModelShipment();
				newModelShipment = (ModelShipment) UtilityProperty.initializeBaseModel(newModelShipment,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPSHIPMENT");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelShipment.setId(corelationId);
				newModelShipment.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelShipment.setCreatedDate(new Date().toString());
				newModelShipment.setModifiedDate(new Date().toString());
				
				newModelShipment.setRole(modelDesign.getRole());
				newModelShipment.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelShipment.setSteps(steps);

				try {
			
					repositoryShipment.save(newModelShipment);
					return newModelShipment;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelShipment update(ModelShipment modelShipment) {

		ModelShipment existingModelShipment = new ModelShipment();
		String password = null;

		if (modelShipment.getAttemptId() != null) {
			existingModelShipment = repositoryShipment
					.findFirstByAttemptId(modelShipment.getAttemptId());
			if (existingModelShipment != null) {
				try {

					for (Step step : existingModelShipment.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelShipment.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryShipment.save(modelShipment);
					return modelShipment;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelShipment> getListByPropertyCodeAndValue(FilterShipment filterShipment) {
		List<ModelShipment> modelShipmentList = new ArrayList<ModelShipment>();
		List<ModelShipment> modifedModelShipmentList = new ArrayList<ModelShipment>();
		boolean isRecordFound = false;
		if (filterShipment.getTanentId() != null) {
			modelShipmentList = repositoryShipment
					.getByTanentIdAndRoleId(filterShipment.getTanentId(), filterShipment.getRoleId());
		}

		if (modelShipmentList.size() > 0) {
			for (ModelShipment modelShipment : modelShipmentList) {
				for (Step step : modelShipment.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterShipment.getPropertyCode())) {
								modifedModelShipmentList.add(modelShipment);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelShipmentList;
	}

}
