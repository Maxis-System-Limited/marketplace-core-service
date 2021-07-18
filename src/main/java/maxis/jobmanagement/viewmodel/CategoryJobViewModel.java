package maxis.jobmanagement.viewmodel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import maxis.jobmanagement.model.Job;
import maxis.jobmanagement.olmworkflow.CategoryStateViewModel;

@Getter
@Setter
public class CategoryJobViewModel {
 private Job job;
 private List<CategoryStateViewModel> actions;
}
