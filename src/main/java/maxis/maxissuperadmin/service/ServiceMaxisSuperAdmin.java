package maxis.maxissuperadmin.service;

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
import maxis.maxissuperadmin.model.FilterMaxisSuperAdmin;
import maxis.maxissuperadmin.model.ModelMaxisSuperAdmin;
import maxis.maxissuperadmin.repository.RepositoryMaxisSuperAdmin;

@Component
public class ServiceMaxisSuperAdmin {

	@Autowired
	private RepositoryMaxisSuperAdmin repositoryMaxisSuperAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceMaxisSuperAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelMaxisSuperAdmin add(ModelMaxisSuperAdmin modelMaxisSuperAdmin) {

		modelMaxisSuperAdmin.setId(UUID.randomUUID().toString());
		// modelMaxisSuperAdmin.setCreatedDate(new Date().toString());
		// modelMaxisSuperAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryMaxisSuperAdmin.save(modelMaxisSuperAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelMaxisSuperAdmin;
	}

	public List<ModelMaxisSuperAdmin> getList(FilterMaxisSuperAdmin filterMaxisSuperAdmin) {
		List<ModelMaxisSuperAdmin> modelMaxisSuperAdmin = new ArrayList<ModelMaxisSuperAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisSuperAdmin.getCode())) {
			modelMaxisSuperAdmin = repositoryMaxisSuperAdmin.findByCode(filterMaxisSuperAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisSuperAdmin.getTanentId())) {
			modelMaxisSuperAdmin = repositoryMaxisSuperAdmin.findByTanentId(filterMaxisSuperAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisSuperAdmin.getCreatedById())) {
			modelMaxisSuperAdmin = repositoryMaxisSuperAdmin.findByCreatedById(filterMaxisSuperAdmin.getCreatedById());
		} else {
			modelMaxisSuperAdmin = repositoryMaxisSuperAdmin.findAll();
		}
		return modelMaxisSuperAdmin;
	}

	public ModelMaxisSuperAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelMaxisSuperAdmin modelMaxisSuperAdmin = new ModelMaxisSuperAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelMaxisSuperAdmin = repositoryMaxisSuperAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelMaxisSuperAdmin == null) {
				ModelMaxisSuperAdmin newModelMaxisSuperAdmin = new ModelMaxisSuperAdmin();
				newModelMaxisSuperAdmin = (ModelMaxisSuperAdmin) UtilityProperty.initializeBaseModel(newModelMaxisSuperAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelMaxisSuperAdmin.setId(corelationId);
				newModelMaxisSuperAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelMaxisSuperAdmin.setCreatedDate(new Date().toString());
				newModelMaxisSuperAdmin.setModifiedDate(new Date().toString());
				
				newModelMaxisSuperAdmin.setRole(modelDesign.getRole());
				newModelMaxisSuperAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelMaxisSuperAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelMaxisSuperAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelMaxisSuperAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelMaxisSuperAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelMaxisSuperAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelMaxisSuperAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelMaxisSuperAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelMaxisSuperAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelMaxisSuperAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelMaxisSuperAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelMaxisSuperAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelMaxisSuperAdmin.getModifiedById());

				for (Step step : newModelMaxisSuperAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelMaxisSuperAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryMaxisSuperAdmin.save(newModelMaxisSuperAdmin);
					return newModelMaxisSuperAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelMaxisSuperAdmin update(ModelMaxisSuperAdmin modelMaxisSuperAdmin) {

		ModelMaxisSuperAdmin existingModelMaxisSuperAdmin = new ModelMaxisSuperAdmin();
		String password = null;

		if (modelMaxisSuperAdmin.getAttemptId() != null) {
			existingModelMaxisSuperAdmin = repositoryMaxisSuperAdmin
					.findFirstByAttemptId(modelMaxisSuperAdmin.getAttemptId());
			if (existingModelMaxisSuperAdmin != null) {
				try {

					for (Step step : existingModelMaxisSuperAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelMaxisSuperAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryMaxisSuperAdmin.save(modelMaxisSuperAdmin);
					return modelMaxisSuperAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelMaxisSuperAdmin> getListByPropertyCodeAndValue(FilterMaxisSuperAdmin filterMaxisSuperAdmin) {
		List<ModelMaxisSuperAdmin> modelMaxisSuperAdminList = new ArrayList<ModelMaxisSuperAdmin>();
		List<ModelMaxisSuperAdmin> modifedModelMaxisSuperAdminList = new ArrayList<ModelMaxisSuperAdmin>();
		boolean isRecordFound = false;
		if (filterMaxisSuperAdmin.getTanentId() != null) {
			modelMaxisSuperAdminList = repositoryMaxisSuperAdmin
					.getByTanentIdAndRoleId(filterMaxisSuperAdmin.getTanentId(), filterMaxisSuperAdmin.getRoleId());
		}

		if (modelMaxisSuperAdminList.size() > 0) {
			for (ModelMaxisSuperAdmin modelMaxisSuperAdmin : modelMaxisSuperAdminList) {
				for (Step step : modelMaxisSuperAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterMaxisSuperAdmin.getPropertyCode())) {
								modifedModelMaxisSuperAdminList.add(modelMaxisSuperAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelMaxisSuperAdminList;
	}

}
