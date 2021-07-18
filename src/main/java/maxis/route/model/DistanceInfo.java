package maxis.route.model;

import lombok.*;

@Getter
@Setter
public class DistanceInfo {
	private int distance;
	private int lastVertex;

	public DistanceInfo() {
		super();
		this.distance = -1;
		this.lastVertex = -1;
	}

}
