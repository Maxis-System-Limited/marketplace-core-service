package maxis.jobmanagement.viewmodel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import maxis.jobmanagement.model.Job;

@Getter
@Setter
public class JobListViewModel {
	private List<Job> data;
	private String status;
	private String message;
}
