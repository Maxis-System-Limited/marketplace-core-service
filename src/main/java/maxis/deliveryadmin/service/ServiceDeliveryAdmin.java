package maxis.deliveryadmin.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import maxis.common.UtilityFunction;
import maxis.common.UtilityProperty;
import maxis.common.model.Context;
import maxis.common.model.OnboardInputModel;
import maxis.common.model.Property;
import maxis.common.model.Step;
import maxis.deliveryadmin.model.FilterDeliveryAdmin;
import maxis.deliveryadmin.model.ModelDeliveryAdmin;
import maxis.deliveryadmin.repository.RepositoryDeliveryAdmin;
import maxis.design.model.AuthenticationModel;
import maxis.design.model.AuthenticationRole;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;

@Component
public class ServiceDeliveryAdmin {

	@Autowired
	private RepositoryDeliveryAdmin repositoryDeliveryAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceDeliveryAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelDeliveryAdmin add(ModelDeliveryAdmin modelDeliveryAdmin) {

		modelDeliveryAdmin.setId(UUID.randomUUID().toString());
		// modelDeliveryAdmin.setCreatedDate(new Date().toString());
		// modelDeliveryAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryDeliveryAdmin.save(modelDeliveryAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelDeliveryAdmin;
	}

	public List<ModelDeliveryAdmin> getList(FilterDeliveryAdmin filterDeliveryAdmin) {
		List<ModelDeliveryAdmin> modelDeliveryAdmin = new ArrayList<ModelDeliveryAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryAdmin.getCode())) {
			modelDeliveryAdmin = repositoryDeliveryAdmin.findByCode(filterDeliveryAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryAdmin.getTanentId())) {
			modelDeliveryAdmin = repositoryDeliveryAdmin.findByTanentId(filterDeliveryAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDeliveryAdmin.getCreatedById())) {
			modelDeliveryAdmin = repositoryDeliveryAdmin.findByCreatedById(filterDeliveryAdmin.getCreatedById());
		} else {
			modelDeliveryAdmin = repositoryDeliveryAdmin.findAll();
		}
		return modelDeliveryAdmin;
	}

	public ModelDeliveryAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelDeliveryAdmin modelDeliveryAdmin = new ModelDeliveryAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelDeliveryAdmin = repositoryDeliveryAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelDeliveryAdmin == null) {
				ModelDeliveryAdmin newModelDeliveryAdmin = new ModelDeliveryAdmin();
				newModelDeliveryAdmin = (ModelDeliveryAdmin) UtilityProperty.initializeBaseModel(newModelDeliveryAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDDELIVERYADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelDeliveryAdmin.setId(corelationId);
				newModelDeliveryAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelDeliveryAdmin.setCreatedDate(new Date().toString());
				newModelDeliveryAdmin.setModifiedDate(new Date().toString());
				
				newModelDeliveryAdmin.setRole(modelDesign.getRole());
				newModelDeliveryAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelDeliveryAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelDeliveryAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelDeliveryAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelDeliveryAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelDeliveryAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelDeliveryAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelDeliveryAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelDeliveryAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelDeliveryAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelDeliveryAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelDeliveryAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelDeliveryAdmin.getModifiedById());

				for (Step step : newModelDeliveryAdmin.getSteps()) {
					for (Context context : step.getContext()) {
						for (Property property : context.getProperties()) {

							switch (property.getPropertyCode()) {
							case "PASSWORD":
								password = property.getPropertyValue();
								break;
							case "USERID":
								userId = property.getPropertyValue();
								break;
							}

						}
					}
				}
				
				final String baseUrl = "http://maxis-elog-security.nagadpay.com/api/user/" + userId +"/exists";
				URI uri = new URI(baseUrl);
				 
				ResponseEntity<Boolean> isExistInAuthServier = restTemplate.getForEntity(uri, Boolean.class);
				
				authenticationModel.setPassword(password);
				authenticationModel.setRoles(null);
				authenticationModel.setRoles(authenticationRoles);
				authenticationModel.setStatus("");
				authenticationModel.setTanentId(newModelDeliveryAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryDeliveryAdmin.save(newModelDeliveryAdmin);
					return newModelDeliveryAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelDeliveryAdmin update(ModelDeliveryAdmin modelDeliveryAdmin) {

		ModelDeliveryAdmin existingModelDeliveryAdmin = new ModelDeliveryAdmin();
		String password = null;

		if (modelDeliveryAdmin.getAttemptId() != null) {
			existingModelDeliveryAdmin = repositoryDeliveryAdmin
					.findFirstByAttemptId(modelDeliveryAdmin.getAttemptId());
			if (existingModelDeliveryAdmin != null) {
				try {

					for (Step step : existingModelDeliveryAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelDeliveryAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryDeliveryAdmin.save(modelDeliveryAdmin);
					return modelDeliveryAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelDeliveryAdmin> getListByPropertyCodeAndValue(FilterDeliveryAdmin filterDeliveryAdmin) {
		List<ModelDeliveryAdmin> modelDeliveryAdminList = new ArrayList<ModelDeliveryAdmin>();
		List<ModelDeliveryAdmin> modifedModelDeliveryAdminList = new ArrayList<ModelDeliveryAdmin>();
		boolean isRecordFound = false;
		if (filterDeliveryAdmin.getTanentId() != null) {
			modelDeliveryAdminList = repositoryDeliveryAdmin
					.getByTanentIdAndRoleId(filterDeliveryAdmin.getTanentId(), filterDeliveryAdmin.getRoleId());
		}

		if (modelDeliveryAdminList.size() > 0) {
			for (ModelDeliveryAdmin modelDeliveryAdmin : modelDeliveryAdminList) {
				for (Step step : modelDeliveryAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterDeliveryAdmin.getPropertyCode())) {
								modifedModelDeliveryAdminList.add(modelDeliveryAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelDeliveryAdminList;
	}

}
