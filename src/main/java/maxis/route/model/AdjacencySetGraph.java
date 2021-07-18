package maxis.route.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdjacencySetGraph implements IGraph {

	private List<Node> vertexList = new ArrayList<>();
	public int numVertices = 0;
	private GraphType graphType = GraphType.DIRECTED;

	public AdjacencySetGraph(int numVertices, GraphType graphType) {
		
		this.numVertices = numVertices;
		this.graphType = graphType;
		
		for(int i = 0; i < numVertices; i++) {
			vertexList.add(new Node(i));
		}
		
	}

	@Override
	public void addEdge(int v1, int v2) {
		if(v1 < 0 || v1 >= numVertices || v2 < 0 || v2 >= numVertices ) {
			throw new IllegalArgumentException("Invalid Vertex.");
		}
		vertexList.get(v1).addEdge(v2);
		if(graphType == GraphType.UNDIRECTED) {
			vertexList.get(v2).addEdge(v1);
		}
	}

	@Override
	public List<Integer> getAdjacentVertices(int v) {
		
		if(v < 0 || v >= numVertices  ) {
			throw new IllegalArgumentException("Invalid Vertex.");
		}
		return vertexList.get(v).getAdjacentVertices();
	}

}
