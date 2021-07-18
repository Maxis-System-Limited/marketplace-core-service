package maxis.jobmanagement.viewmodel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import maxis.jobmanagement.model.Status;
import maxis.jobmanagement.model.StatusLog;

@Getter
@Setter
public class StatusLogListViewModel {
	private List<StatusLog> data;
	private String status;
	private String message;
}
