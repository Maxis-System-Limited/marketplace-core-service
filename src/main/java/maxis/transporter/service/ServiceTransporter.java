package maxis.transporter.service;

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
import maxis.transporter.model.FilterTransporter;
import maxis.transporter.model.ModelTransporter;
import maxis.transporter.repository.RepositoryTransporter;

@Component
public class ServiceTransporter {

	@Autowired
	private RepositoryTransporter repositoryTransporter;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceTransporter(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelTransporter add(ModelTransporter modelTransporter) {

		modelTransporter.setId(UUID.randomUUID().toString());
		// modelTransporter.setCreatedDate(new Date().toString());
		// modelTransporter.setModifiedDate(new Date().toString());

		try {
			repositoryTransporter.save(modelTransporter);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelTransporter;
	}

	public List<ModelTransporter> getList(FilterTransporter filterTransporter) {
		List<ModelTransporter> modelTransporter = new ArrayList<ModelTransporter>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterTransporter.getCode())) {
			modelTransporter = repositoryTransporter.findByCode(filterTransporter.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterTransporter.getTanentId())) {
			modelTransporter = repositoryTransporter.findByTanentId(filterTransporter.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterTransporter.getCreatedById())) {
			modelTransporter = repositoryTransporter.findByCreatedById(filterTransporter.getCreatedById());
		} else {
			modelTransporter = repositoryTransporter.findAll();
		}
		return modelTransporter;
	}

	public ModelTransporter create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelTransporter modelTransporter = new ModelTransporter();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelTransporter = repositoryTransporter.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelTransporter == null) {
				ModelTransporter newModelTransporter = new ModelTransporter();
				newModelTransporter = (ModelTransporter) UtilityProperty.initializeBaseModel(newModelTransporter,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPSHIPMENTT");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelTransporter.setId(corelationId);
				newModelTransporter.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelTransporter.setCreatedDate(new Date().toString());
				newModelTransporter.setModifiedDate(new Date().toString());
				
				newModelTransporter.setRole(modelDesign.getRole());
				newModelTransporter.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelTransporter.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelTransporter.getRole().getRoleId());
				authenticationRole.setCode(newModelTransporter.getRole().getCode());
				authenticationRole.setCreatedAt(newModelTransporter.getCreatedDate());
				authenticationRole.setCreatedBy(newModelTransporter.getCreatedById());
				authenticationRole.setDisplayName(newModelTransporter.getRole().getDisplayName());
				authenticationRole.setLevel(newModelTransporter.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelTransporter.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelTransporter.getRole().getParentId());
				authenticationRole.setStatus(newModelTransporter.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelTransporter.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelTransporter.getModifiedById());

				for (Step step : newModelTransporter.getSteps()) {
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
				authenticationModel.setTanentId(newModelTransporter.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryTransporter.save(newModelTransporter);
					return newModelTransporter;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelTransporter update(ModelTransporter modelTransporter) {

		ModelTransporter existingModelTransporter = new ModelTransporter();
		String password = null;

		if (modelTransporter.getAttemptId() != null) {
			existingModelTransporter = repositoryTransporter
					.findFirstByAttemptId(modelTransporter.getAttemptId());
			if (existingModelTransporter != null) {
				try {

					for (Step step : existingModelTransporter.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelTransporter.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryTransporter.save(modelTransporter);
					return modelTransporter;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelTransporter> getListByPropertyCodeAndValue(FilterTransporter filterTransporter) {
		List<ModelTransporter> modelTransporterList = new ArrayList<ModelTransporter>();
		List<ModelTransporter> modifedModelTransporterList = new ArrayList<ModelTransporter>();
		boolean isRecordFound = false;
		if (filterTransporter.getTanentId() != null) {
			modelTransporterList = repositoryTransporter
					.getByTanentIdAndRoleId(filterTransporter.getTanentId(), filterTransporter.getRoleId());
		}

		if (modelTransporterList.size() > 0) {
			for (ModelTransporter modelTransporter : modelTransporterList) {
				for (Step step : modelTransporter.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterTransporter.getPropertyCode())) {
								modifedModelTransporterList.add(modelTransporter);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelTransporterList;
	}

}
