package maxis.route.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge {
	private Integer sourceNode;
	private Integer destinationNode;

	public Edge() {
		this.sourceNode = -1;
		this.destinationNode = -1;
	}

}
