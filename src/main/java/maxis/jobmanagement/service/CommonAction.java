package maxis.jobmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonAction extends CommonPerformableActions{

	@Autowired
	public CommonAction(JobDomainService jobDomainService) {
		super(jobDomainService);
	}
	
}
