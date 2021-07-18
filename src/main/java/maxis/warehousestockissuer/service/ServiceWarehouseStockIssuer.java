package maxis.warehousestockissuer.service;

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
import maxis.warehousestockissuer.model.FilterWarehouseStockIssuer;
import maxis.warehousestockissuer.model.ModelWarehouseStockIssuer;
import maxis.warehousestockissuer.repository.RepositoryWarehouseStockIssuer;

@Component
public class ServiceWarehouseStockIssuer {

	@Autowired
	private RepositoryWarehouseStockIssuer repositoryWarehouseStockIssuer;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceWarehouseStockIssuer(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelWarehouseStockIssuer add(ModelWarehouseStockIssuer modelWarehouseStockIssuer) {

		modelWarehouseStockIssuer.setId(UUID.randomUUID().toString());
		// modelWarehouseStockIssuer.setCreatedDate(new Date().toString());
		// modelWarehouseStockIssuer.setModifiedDate(new Date().toString());

		try {
			repositoryWarehouseStockIssuer.save(modelWarehouseStockIssuer);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelWarehouseStockIssuer;
	}

	public List<ModelWarehouseStockIssuer> getList(FilterWarehouseStockIssuer filterWarehouseStockIssuer) {
		List<ModelWarehouseStockIssuer> modelWarehouseStockIssuer = new ArrayList<ModelWarehouseStockIssuer>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockIssuer.getCode())) {
			modelWarehouseStockIssuer = repositoryWarehouseStockIssuer.findByCode(filterWarehouseStockIssuer.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockIssuer.getTanentId())) {
			modelWarehouseStockIssuer = repositoryWarehouseStockIssuer.findByTanentId(filterWarehouseStockIssuer.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseStockIssuer.getCreatedById())) {
			modelWarehouseStockIssuer = repositoryWarehouseStockIssuer.findByCreatedById(filterWarehouseStockIssuer.getCreatedById());
		} else {
			modelWarehouseStockIssuer = repositoryWarehouseStockIssuer.findAll();
		}
		return modelWarehouseStockIssuer;
	}

	public ModelWarehouseStockIssuer create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelWarehouseStockIssuer modelWarehouseStockIssuer = new ModelWarehouseStockIssuer();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelWarehouseStockIssuer = repositoryWarehouseStockIssuer.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelWarehouseStockIssuer == null) {
				ModelWarehouseStockIssuer newModelWarehouseStockIssuer = new ModelWarehouseStockIssuer();
				newModelWarehouseStockIssuer = (ModelWarehouseStockIssuer) UtilityProperty.initializeBaseModel(newModelWarehouseStockIssuer,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPWSISSUER");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelWarehouseStockIssuer.setId(corelationId);
				newModelWarehouseStockIssuer.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelWarehouseStockIssuer.setCreatedDate(new Date().toString());
				newModelWarehouseStockIssuer.setModifiedDate(new Date().toString());
				
				newModelWarehouseStockIssuer.setRole(modelDesign.getRole());
				newModelWarehouseStockIssuer.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelWarehouseStockIssuer.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelWarehouseStockIssuer.getRole().getRoleId());
				authenticationRole.setCode(newModelWarehouseStockIssuer.getRole().getCode());
				authenticationRole.setCreatedAt(newModelWarehouseStockIssuer.getCreatedDate());
				authenticationRole.setCreatedBy(newModelWarehouseStockIssuer.getCreatedById());
				authenticationRole.setDisplayName(newModelWarehouseStockIssuer.getRole().getDisplayName());
				authenticationRole.setLevel(newModelWarehouseStockIssuer.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelWarehouseStockIssuer.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelWarehouseStockIssuer.getRole().getParentId());
				authenticationRole.setStatus(newModelWarehouseStockIssuer.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelWarehouseStockIssuer.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelWarehouseStockIssuer.getModifiedById());

				for (Step step : newModelWarehouseStockIssuer.getSteps()) {
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
				authenticationModel.setTanentId(newModelWarehouseStockIssuer.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryWarehouseStockIssuer.save(newModelWarehouseStockIssuer);
					return newModelWarehouseStockIssuer;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelWarehouseStockIssuer update(ModelWarehouseStockIssuer modelWarehouseStockIssuer) {

		ModelWarehouseStockIssuer existingModelWarehouseStockIssuer = new ModelWarehouseStockIssuer();
		String password = null;

		if (modelWarehouseStockIssuer.getAttemptId() != null) {
			existingModelWarehouseStockIssuer = repositoryWarehouseStockIssuer
					.findFirstByAttemptId(modelWarehouseStockIssuer.getAttemptId());
			if (existingModelWarehouseStockIssuer != null) {
				try {

					for (Step step : existingModelWarehouseStockIssuer.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelWarehouseStockIssuer.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryWarehouseStockIssuer.save(modelWarehouseStockIssuer);
					return modelWarehouseStockIssuer;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelWarehouseStockIssuer> getListByPropertyCodeAndValue(FilterWarehouseStockIssuer filterWarehouseStockIssuer) {
		List<ModelWarehouseStockIssuer> modelWarehouseStockIssuerList = new ArrayList<ModelWarehouseStockIssuer>();
		List<ModelWarehouseStockIssuer> modifedModelWarehouseStockIssuerList = new ArrayList<ModelWarehouseStockIssuer>();
		boolean isRecordFound = false;
		if (filterWarehouseStockIssuer.getTanentId() != null) {
			modelWarehouseStockIssuerList = repositoryWarehouseStockIssuer
					.getByTanentIdAndRoleId(filterWarehouseStockIssuer.getTanentId(), filterWarehouseStockIssuer.getRoleId());
		}

		if (modelWarehouseStockIssuerList.size() > 0) {
			for (ModelWarehouseStockIssuer modelWarehouseStockIssuer : modelWarehouseStockIssuerList) {
				for (Step step : modelWarehouseStockIssuer.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterWarehouseStockIssuer.getPropertyCode())) {
								modifedModelWarehouseStockIssuerList.add(modelWarehouseStockIssuer);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelWarehouseStockIssuerList;
	}

}
