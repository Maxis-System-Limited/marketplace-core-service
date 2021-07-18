package maxis.enterpriseadmin.service;

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
import maxis.enterpriseadmin.model.FilterEnterpriseAdmin;
import maxis.enterpriseadmin.model.ModelEnterpriseAdmin;
import maxis.enterpriseadmin.repository.RepositoryEnterpriseAdmin;

@Component
public class ServiceEnterpriseAdmin {

	@Autowired
	private RepositoryEnterpriseAdmin repositoryEnterpriseAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceEnterpriseAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelEnterpriseAdmin add(ModelEnterpriseAdmin modelEnterpriseAdmin) {

		modelEnterpriseAdmin.setId(UUID.randomUUID().toString());
		// modelEnterpriseAdmin.setCreatedDate(new Date().toString());
		// modelEnterpriseAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryEnterpriseAdmin.save(modelEnterpriseAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelEnterpriseAdmin;
	}

	public List<ModelEnterpriseAdmin> getList(FilterEnterpriseAdmin filterEnterpriseAdmin) {
		List<ModelEnterpriseAdmin> modelEnterpriseAdmin = new ArrayList<ModelEnterpriseAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseAdmin.getCode())) {
			modelEnterpriseAdmin = repositoryEnterpriseAdmin.findByCode(filterEnterpriseAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseAdmin.getTanentId())) {
			modelEnterpriseAdmin = repositoryEnterpriseAdmin.findByTanentId(filterEnterpriseAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterEnterpriseAdmin.getCreatedById())) {
			modelEnterpriseAdmin = repositoryEnterpriseAdmin.findByCreatedById(filterEnterpriseAdmin.getCreatedById());
		} else {
			modelEnterpriseAdmin = repositoryEnterpriseAdmin.findAll();
		}
		return modelEnterpriseAdmin;
	}

	public ModelEnterpriseAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelEnterpriseAdmin modelEnterpriseAdmin = new ModelEnterpriseAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if (onboardInputModel.getAttemptId() != null) {
			modelEnterpriseAdmin = repositoryEnterpriseAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelEnterpriseAdmin == null) {
				ModelEnterpriseAdmin newModelEnterpriseAdmin = new ModelEnterpriseAdmin();
				newModelEnterpriseAdmin = (ModelEnterpriseAdmin) UtilityProperty.initializeBaseModel(
						newModelEnterpriseAdmin, onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPENTERPRISEADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelEnterpriseAdmin.setId(corelationId);
				newModelEnterpriseAdmin.setAttemptId(onboardInputModel.getAttemptId());

				newModelEnterpriseAdmin.setCreatedDate(new Date().toString());
				newModelEnterpriseAdmin.setModifiedDate(new Date().toString());

				newModelEnterpriseAdmin.setRole(modelDesign.getRole());
				newModelEnterpriseAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());

				newModelEnterpriseAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelEnterpriseAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelEnterpriseAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelEnterpriseAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelEnterpriseAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelEnterpriseAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelEnterpriseAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelEnterpriseAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelEnterpriseAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelEnterpriseAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelEnterpriseAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelEnterpriseAdmin.getModifiedById());

				for (Step step : newModelEnterpriseAdmin.getSteps()) {
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

				final String baseUrl = "http://maxis-elog-security.nagadpay.com/api/user/" + userId + "/exists";
				URI uri = new URI(baseUrl);

				ResponseEntity<Boolean> isExistInAuthServier = restTemplate.getForEntity(uri, Boolean.class);

				authenticationModel.setPassword(password);
				authenticationModel.setRoles(null);
				authenticationModel.setRoles(authenticationRoles);
				authenticationModel.setStatus("");
				authenticationModel.setTanentId(newModelEnterpriseAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if (!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST,
								entity, String.class);
					}
					repositoryEnterpriseAdmin.save(newModelEnterpriseAdmin);
					return newModelEnterpriseAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelEnterpriseAdmin update(ModelEnterpriseAdmin modelEnterpriseAdmin) {

		ModelEnterpriseAdmin existingModelEnterpriseAdmin = new ModelEnterpriseAdmin();
		String password = null;

		if (modelEnterpriseAdmin.getAttemptId() != null) {
			existingModelEnterpriseAdmin = repositoryEnterpriseAdmin
					.findFirstByAttemptId(modelEnterpriseAdmin.getAttemptId());
			if (existingModelEnterpriseAdmin != null) {
				try {

					for (Step step : existingModelEnterpriseAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelEnterpriseAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryEnterpriseAdmin.save(modelEnterpriseAdmin);
					return modelEnterpriseAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelEnterpriseAdmin> getListByPropertyCodeAndValue(FilterEnterpriseAdmin filterEnterpriseAdmin) {
		List<ModelEnterpriseAdmin> modelEnterpriseAdminList = new ArrayList<ModelEnterpriseAdmin>();
		List<ModelEnterpriseAdmin> modifedModelEnterpriseAdminList = new ArrayList<ModelEnterpriseAdmin>();
		boolean isRecordFound = false;
		if (filterEnterpriseAdmin.getTanentId() != null) {
			modelEnterpriseAdminList = repositoryEnterpriseAdmin
					.getByTanentIdAndRoleId(filterEnterpriseAdmin.getTanentId(), filterEnterpriseAdmin.getRoleId());
		}

		if (modelEnterpriseAdminList.size() > 0) {
			for (ModelEnterpriseAdmin modelEnterpriseAdmin : modelEnterpriseAdminList) {
				for (Step step : modelEnterpriseAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterEnterpriseAdmin.getPropertyCode())) {
								modifedModelEnterpriseAdminList.add(modelEnterpriseAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelEnterpriseAdminList;
	}

}
