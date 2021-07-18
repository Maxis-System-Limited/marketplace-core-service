package maxis.jobmanagement.olmworkflow;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import maxis.jobmanagement.model.FilterJob;
import maxis.jobmanagement.service.JobDomainService;

@Service
public class CategoryWiseStateActionService {

	private RestTemplate restTemplate;
	
	@Autowired
	private JobDomainService jobDomainService;

	public CategoryWiseStateActionService(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public CategoryWiseStateActions getStateActionByCategoryId(FilterJob filterJob) {

		//CategoryWiseStateActionsReponseModel categoryWiseStateActionsReponseModel = new CategoryWiseStateActionsReponseModel();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		Long id = filterJob.getCategoryid();
		Long roleId = Long.valueOf(jobDomainService.getRoleIdByRolecode(filterJob.getRoleCode()));
		HttpEntity entity = new HttpEntity(httpHeaders);
		
//		ResponseEntity<CategoryWiseStateActionsReponseModel> response = restTemplate.exchange(
//				"http://elog-workflow-service-maxis.nagadpay.com/api/category?id="+id, HttpMethod.GET, entity,
//				CategoryWiseStateActionsReponseModel.class);

		ResponseEntity<CategoryWiseStateActions> response = restTemplate.exchange(
				"http://elog-workflow-service-maxis.nagadpay.com/api/category?id="+id+"&roleId="+roleId, HttpMethod.GET, entity,
				CategoryWiseStateActions.class);
		

		CategoryWiseStateActions categoryWiseStateActions = new CategoryWiseStateActions();
		
		categoryWiseStateActions.setData(response.getBody().getData());
		
		//JsonNode root = mapper.readTree(response.getBody());
		//JsonNode balanceResponse = root.path("data");
		
		//categoryWiseStateActionsReponseModel.setData(response.getBody().getData());

		return categoryWiseStateActions;
	}
}
