package maxis.jobmanagement.viewmodel;

import java.util.List;

import lombok.*;

@Setter
@Getter
public class CategoryJobListViewModel {
	private List<CategoryJobViewModel> data;
	private String status;
	private String message;
}
