package maxis.maxissupportadmin.service;

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
import maxis.maxissupportadmin.model.FilterMaxisSupportAdmin;
import maxis.maxissupportadmin.model.ModelMaxisSupportAdmin;
import maxis.maxissupportadmin.repository.RepositoryMaxisSupportAdmin;

@Component
public class ServiceMaxisSupportAdmin {

	@Autowired
	private RepositoryMaxisSupportAdmin repositoryMaxisSupportAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceMaxisSupportAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelMaxisSupportAdmin add(ModelMaxisSupportAdmin modelMaxisSupportAdmin) {

		modelMaxisSupportAdmin.setId(UUID.randomUUID().toString());
		// modelMaxisSupportAdmin.setCreatedDate(new Date().toString());
		// modelMaxisSupportAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryMaxisSupportAdmin.save(modelMaxisSupportAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelMaxisSupportAdmin;
	}

	public List<ModelMaxisSupportAdmin> getList(FilterMaxisSupportAdmin filterMaxisSupportAdmin) {
		List<ModelMaxisSupportAdmin> modelMaxisSupportAdmin = new ArrayList<ModelMaxisSupportAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisSupportAdmin.getCode())) {
			modelMaxisSupportAdmin = repositoryMaxisSupportAdmin.findByCode(filterMaxisSupportAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisSupportAdmin.getTanentId())) {
			modelMaxisSupportAdmin = repositoryMaxisSupportAdmin.findByTanentId(filterMaxisSupportAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterMaxisSupportAdmin.getCreatedById())) {
			modelMaxisSupportAdmin = repositoryMaxisSupportAdmin.findByCreatedById(filterMaxisSupportAdmin.getCreatedById());
		} else {
			modelMaxisSupportAdmin = repositoryMaxisSupportAdmin.findAll();
		}
		return modelMaxisSupportAdmin;
	}

	public ModelMaxisSupportAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelMaxisSupportAdmin modelMaxisSupportAdmin = new ModelMaxisSupportAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelMaxisSupportAdmin = repositoryMaxisSupportAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelMaxisSupportAdmin == null) {
				ModelMaxisSupportAdmin newModelMaxisSupportAdmin = new ModelMaxisSupportAdmin();
				newModelMaxisSupportAdmin = (ModelMaxisSupportAdmin) UtilityProperty.initializeBaseModel(newModelMaxisSupportAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELSUPPORT");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelMaxisSupportAdmin.setId(corelationId);
				newModelMaxisSupportAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelMaxisSupportAdmin.setCreatedDate(new Date().toString());
				newModelMaxisSupportAdmin.setModifiedDate(new Date().toString());
				
				newModelMaxisSupportAdmin.setRole(modelDesign.getRole());
				newModelMaxisSupportAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelMaxisSupportAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelMaxisSupportAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelMaxisSupportAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelMaxisSupportAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelMaxisSupportAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelMaxisSupportAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelMaxisSupportAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelMaxisSupportAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelMaxisSupportAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelMaxisSupportAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelMaxisSupportAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelMaxisSupportAdmin.getModifiedById());

				for (Step step : newModelMaxisSupportAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelMaxisSupportAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryMaxisSupportAdmin.save(newModelMaxisSupportAdmin);
					return newModelMaxisSupportAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelMaxisSupportAdmin update(ModelMaxisSupportAdmin modelMaxisSupportAdmin) {

		ModelMaxisSupportAdmin existingModelMaxisSupportAdmin = new ModelMaxisSupportAdmin();
		String password = null;

		if (modelMaxisSupportAdmin.getAttemptId() != null) {
			existingModelMaxisSupportAdmin = repositoryMaxisSupportAdmin
					.findFirstByAttemptId(modelMaxisSupportAdmin.getAttemptId());
			if (existingModelMaxisSupportAdmin != null) {
				try {

					for (Step step : existingModelMaxisSupportAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelMaxisSupportAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryMaxisSupportAdmin.save(modelMaxisSupportAdmin);
					return modelMaxisSupportAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelMaxisSupportAdmin> getListByPropertyCodeAndValue(FilterMaxisSupportAdmin filterMaxisSupportAdmin) {
		List<ModelMaxisSupportAdmin> modelMaxisSupportAdminList = new ArrayList<ModelMaxisSupportAdmin>();
		List<ModelMaxisSupportAdmin> modifedModelMaxisSupportAdminList = new ArrayList<ModelMaxisSupportAdmin>();
		boolean isRecordFound = false;
		if (filterMaxisSupportAdmin.getTanentId() != null) {
			modelMaxisSupportAdminList = repositoryMaxisSupportAdmin
					.getByTanentIdAndRoleId(filterMaxisSupportAdmin.getTanentId(), filterMaxisSupportAdmin.getRoleId());
		}

		if (modelMaxisSupportAdminList.size() > 0) {
			for (ModelMaxisSupportAdmin modelMaxisSupportAdmin : modelMaxisSupportAdminList) {
				for (Step step : modelMaxisSupportAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterMaxisSupportAdmin.getPropertyCode())) {
								modifedModelMaxisSupportAdminList.add(modelMaxisSupportAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelMaxisSupportAdminList;
	}

}
