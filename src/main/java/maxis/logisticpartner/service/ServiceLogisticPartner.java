package maxis.logisticpartner.service;

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
import maxis.logisticpartner.model.FilterLogisticPartner;
import maxis.logisticpartner.model.ModelLogisticPartner;
import maxis.logisticpartner.repository.RepositoryLogisticPartner;

@Component
public class ServiceLogisticPartner {

	@Autowired
	private RepositoryLogisticPartner repositoryLogisticPartner;
	@Autowired
	private ServiceDesign serviceDesign;

	public ServiceLogisticPartner() {
		super();
	}

	public ModelLogisticPartner add(ModelLogisticPartner modelLogisticPartner) {

		modelLogisticPartner.setId(UUID.randomUUID().toString());
		// modelLogisticPartner.setCreatedDate(new Date().toString());
		// modelLogisticPartner.setModifiedDate(new Date().toString());

		try {
			repositoryLogisticPartner.save(modelLogisticPartner);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelLogisticPartner;
	}

	public List<ModelLogisticPartner> getList(FilterLogisticPartner filterLogisticPartner) {
		List<ModelLogisticPartner> modelLogisticPartner = new ArrayList<ModelLogisticPartner>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterLogisticPartner.getCode())) {
			modelLogisticPartner = repositoryLogisticPartner.findByCode(filterLogisticPartner.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterLogisticPartner.getTanentId())) {
			modelLogisticPartner = repositoryLogisticPartner.findByTanentId(filterLogisticPartner.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterLogisticPartner.getCreatedById())) {
			modelLogisticPartner = repositoryLogisticPartner.findByCreatedById(filterLogisticPartner.getCreatedById());
		} else {
			modelLogisticPartner = repositoryLogisticPartner.findAll();
		}
		return modelLogisticPartner;
	}

	public ModelLogisticPartner create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelLogisticPartner modelLogisticPartner = new ModelLogisticPartner();
		String corelationId = UUID.randomUUID().toString();
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelLogisticPartner = repositoryLogisticPartner.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelLogisticPartner == null) {
				ModelLogisticPartner newModelLogisticPartner = new ModelLogisticPartner();
				newModelLogisticPartner = (ModelLogisticPartner) UtilityProperty.initializeBaseModel(newModelLogisticPartner,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLP");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelLogisticPartner.setId(corelationId);
				newModelLogisticPartner.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelLogisticPartner.setCreatedDate(new Date().toString());
				newModelLogisticPartner.setModifiedDate(new Date().toString());
				
				newModelLogisticPartner.setRole(modelDesign.getRole());
				newModelLogisticPartner.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelLogisticPartner.setSteps(steps);

				try {
			
					repositoryLogisticPartner.save(newModelLogisticPartner);
					return newModelLogisticPartner;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelLogisticPartner update(ModelLogisticPartner modelLogisticPartner) {

		ModelLogisticPartner existingModelLogisticPartner = new ModelLogisticPartner();
		String password = null;

		if (modelLogisticPartner.getAttemptId() != null) {
			existingModelLogisticPartner = repositoryLogisticPartner
					.findFirstByAttemptId(modelLogisticPartner.getAttemptId());
			if (existingModelLogisticPartner != null) {
				try {

					for (Step step : existingModelLogisticPartner.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelLogisticPartner.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryLogisticPartner.save(modelLogisticPartner);
					return modelLogisticPartner;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelLogisticPartner> getListByPropertyCodeAndValue(FilterLogisticPartner filterLogisticPartner) {
		List<ModelLogisticPartner> modelLogisticPartnerList = new ArrayList<ModelLogisticPartner>();
		List<ModelLogisticPartner> modifedModelLogisticPartnerList = new ArrayList<ModelLogisticPartner>();
		boolean isRecordFound = false;
		if (filterLogisticPartner.getTanentId() != null) {
			modelLogisticPartnerList = repositoryLogisticPartner
					.getByTanentIdAndRoleId(filterLogisticPartner.getTanentId(), filterLogisticPartner.getRoleId());
		}

		if (modelLogisticPartnerList.size() > 0) {
			for (ModelLogisticPartner modelLogisticPartner : modelLogisticPartnerList) {
				for (Step step : modelLogisticPartner.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterLogisticPartner.getPropertyCode())) {
								modifedModelLogisticPartnerList.add(modelLogisticPartner);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelLogisticPartnerList;
	}

}
