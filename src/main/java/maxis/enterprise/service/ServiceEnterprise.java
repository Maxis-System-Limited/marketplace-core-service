package maxis.enterprise.service;

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
import maxis.enterprise.model.FilterEnterprise;
import maxis.enterprise.model.ModelEnterprise;
import maxis.enterprise.repository.RepositoryEnterprise;

@Component
public class ServiceEnterprise {

	@Autowired
	private RepositoryEnterprise repositoryEnterprise;
	@Autowired
	private ServiceDesign serviceDesign;

	public ServiceEnterprise() {
		super();
	}

	public ModelEnterprise add(ModelEnterprise modelEnterprise) {
		if (modelEnterprise.getId() == null)
			modelEnterprise.setId(UUID.randomUUID().toString());
		// modelEnterprise.setCreatedDate(new Date().toString());
		// modelEnterprise.setModifiedDate(new Date().toString());

		try {
			modelEnterprise = repositoryEnterprise.save(modelEnterprise);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelEnterprise;
	}

	public List<ModelEnterprise> getList(FilterEnterprise filterEnterprise) {
		List<ModelEnterprise> modelEnterprise = new ArrayList<ModelEnterprise>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterEnterprise.getCode())) {
			modelEnterprise = repositoryEnterprise.findByCode(filterEnterprise.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterprise.getTanentId())) {
			modelEnterprise = repositoryEnterprise.findByTanentId(filterEnterprise.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterprise.getCreatedById())) {
			modelEnterprise = repositoryEnterprise.findByCreatedById(filterEnterprise.getCreatedById());
		} else {
			modelEnterprise = repositoryEnterprise.findAll();
		}
		return modelEnterprise;
	}

	public ModelEnterprise create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelEnterprise modelEnterprise = new ModelEnterprise();
		String corelationId = UUID.randomUUID().toString();
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelEnterprise = repositoryEnterprise.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelEnterprise == null) {
				ModelEnterprise newModelEnterprise = new ModelEnterprise();
				newModelEnterprise = (ModelEnterprise) UtilityProperty.initializeBaseModel(newModelEnterprise,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPENTERPRISE");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelEnterprise.setId(corelationId);
				newModelEnterprise.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelEnterprise.setCreatedDate(new Date().toString());
				newModelEnterprise.setModifiedDate(new Date().toString());
				
				newModelEnterprise.setRole(modelDesign.getRole());
				newModelEnterprise.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelEnterprise.setSteps(steps);

				try {
			
					repositoryEnterprise.save(newModelEnterprise);
					return newModelEnterprise;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelEnterprise update(ModelEnterprise modelEnterprise) {

		ModelEnterprise existingModelEnterprise = new ModelEnterprise();
		String password = null;

		if (modelEnterprise.getAttemptId() != null) {
			existingModelEnterprise = repositoryEnterprise
					.findFirstByAttemptId(modelEnterprise.getAttemptId());
			if (existingModelEnterprise != null) {
				try {

					for (Step step : existingModelEnterprise.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelEnterprise.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryEnterprise.save(modelEnterprise);
					return modelEnterprise;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelEnterprise> getListByPropertyCodeAndValue(FilterEnterprise filterEnterprise) {
		List<ModelEnterprise> modelEnterpriseList = new ArrayList<ModelEnterprise>();
		List<ModelEnterprise> modifedModelEnterpriseList = new ArrayList<ModelEnterprise>();
		boolean isRecordFound = false;
		if (filterEnterprise.getTanentId() != null) {
			modelEnterpriseList = repositoryEnterprise
					.getByTanentIdAndRoleId(filterEnterprise.getTanentId(), filterEnterprise.getRoleId());
		}

		if (modelEnterpriseList.size() > 0) {
			for (ModelEnterprise modelEnterprise : modelEnterpriseList) {
				for (Step step : modelEnterprise.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterEnterprise.getPropertyCode())) {
								modifedModelEnterpriseList.add(modelEnterprise);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelEnterpriseList;
	}

}
