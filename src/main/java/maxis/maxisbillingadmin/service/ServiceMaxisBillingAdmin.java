package maxis.maxisbillingadmin.service;

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
import maxis.design.model.AuthenticationModel;
import maxis.design.model.AuthenticationRole;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;
import maxis.maxisbillingadmin.model.FilterMaxisBillingAdmin;
import maxis.maxisbillingadmin.model.ModelMaxisBillingAdmin;
import maxis.maxisbillingadmin.repository.RepositoryMaxisBillingAdmin;

@Component
public class ServiceMaxisBillingAdmin {

	@Autowired
	private RepositoryMaxisBillingAdmin repositoryMaxisBillingAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceMaxisBillingAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelMaxisBillingAdmin add(ModelMaxisBillingAdmin modelMaxisBillingAdmin) {

		modelMaxisBillingAdmin.setId(UUID.randomUUID().toString());
		// modelMaxisBillingAdmin.setCreatedDate(new Date().toString());
		// modelMaxisBillingAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryMaxisBillingAdmin.save(modelMaxisBillingAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelMaxisBillingAdmin;
	}

	public List<ModelMaxisBillingAdmin> getList(FilterMaxisBillingAdmin filterMaxisBillingAdmin) {
		List<ModelMaxisBillingAdmin> modelMaxisBillingAdmin = new ArrayList<ModelMaxisBillingAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisBillingAdmin.getCode())) {
			modelMaxisBillingAdmin = repositoryMaxisBillingAdmin.findByCode(filterMaxisBillingAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisBillingAdmin.getTanentId())) {
			modelMaxisBillingAdmin = repositoryMaxisBillingAdmin.findByTanentId(filterMaxisBillingAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisBillingAdmin.getCreatedById())) {
			modelMaxisBillingAdmin = repositoryMaxisBillingAdmin.findByCreatedById(filterMaxisBillingAdmin.getCreatedById());
		} else {
			modelMaxisBillingAdmin = repositoryMaxisBillingAdmin.findAll();
		}
		return modelMaxisBillingAdmin;
	}

	public ModelMaxisBillingAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelMaxisBillingAdmin modelMaxisBillingAdmin = new ModelMaxisBillingAdmin();
		
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelMaxisBillingAdmin = repositoryMaxisBillingAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelMaxisBillingAdmin == null) {
				ModelMaxisBillingAdmin newModelMaxisBillingAdmin = new ModelMaxisBillingAdmin();
				newModelMaxisBillingAdmin = (ModelMaxisBillingAdmin) UtilityProperty.initializeBaseModel(newModelMaxisBillingAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELBILLING");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelMaxisBillingAdmin.setId(corelationId);
				newModelMaxisBillingAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelMaxisBillingAdmin.setCreatedDate(new Date().toString());
				newModelMaxisBillingAdmin.setModifiedDate(new Date().toString());
				
				newModelMaxisBillingAdmin.setRole(modelDesign.getRole());
				newModelMaxisBillingAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelMaxisBillingAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelMaxisBillingAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelMaxisBillingAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelMaxisBillingAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelMaxisBillingAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelMaxisBillingAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelMaxisBillingAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelMaxisBillingAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelMaxisBillingAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelMaxisBillingAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelMaxisBillingAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelMaxisBillingAdmin.getModifiedById());

				for (Step step : newModelMaxisBillingAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelMaxisBillingAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryMaxisBillingAdmin.save(newModelMaxisBillingAdmin);
					return newModelMaxisBillingAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelMaxisBillingAdmin update(ModelMaxisBillingAdmin modelMaxisBillingAdmin) {

		ModelMaxisBillingAdmin existingModelMaxisBillingAdmin = new ModelMaxisBillingAdmin();
		String password = null;

		if (modelMaxisBillingAdmin.getAttemptId() != null) {
			existingModelMaxisBillingAdmin = repositoryMaxisBillingAdmin
					.findFirstByAttemptId(modelMaxisBillingAdmin.getAttemptId());
			if (existingModelMaxisBillingAdmin != null) {
				try {

					for (Step step : existingModelMaxisBillingAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelMaxisBillingAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryMaxisBillingAdmin.save(modelMaxisBillingAdmin);
					return modelMaxisBillingAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelMaxisBillingAdmin> getListByPropertyCodeAndValue(FilterMaxisBillingAdmin filterMaxisBillingAdmin) {
		List<ModelMaxisBillingAdmin> modelMaxisBillingAdminList = new ArrayList<ModelMaxisBillingAdmin>();
		List<ModelMaxisBillingAdmin> modifedModelMaxisBillingAdminList = new ArrayList<ModelMaxisBillingAdmin>();
		boolean isRecordFound = false;
		if (filterMaxisBillingAdmin.getTanentId() != null) {
			modelMaxisBillingAdminList = repositoryMaxisBillingAdmin
					.getByTanentIdAndRoleId(filterMaxisBillingAdmin.getTanentId(), filterMaxisBillingAdmin.getRoleId());
		}

		if (modelMaxisBillingAdminList.size() > 0) {
			for (ModelMaxisBillingAdmin modelMaxisBillingAdmin : modelMaxisBillingAdminList) {
				for (Step step : modelMaxisBillingAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterMaxisBillingAdmin.getPropertyCode())) {
								modifedModelMaxisBillingAdminList.add(modelMaxisBillingAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelMaxisBillingAdminList;
	}

}
