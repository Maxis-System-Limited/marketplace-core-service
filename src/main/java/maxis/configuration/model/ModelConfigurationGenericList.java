package maxis.configuration.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModelConfigurationGenericList {
	private ArrayList<ModelConfigurationGeneric> list;
}
