package maxis.route.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyMatrixGraph implements IGraph {
	private int[][] adjacencyMatrix;
	private GraphType graphType = GraphType.DIRECTED;
	private int numVertices = 0;

	public AdjacencyMatrixGraph(GraphType graphType, int numVertices) {
		this.graphType = graphType;
		this.numVertices = numVertices;

		adjacencyMatrix = new int[numVertices][numVertices];
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				adjacencyMatrix[i][j] = 0;
			}
		}

	}

	public void addEdge(int v1, int v2) {
		if (v1 >= numVertices || v1 < 0 || v2 >= numVertices || v2 < 0) {
			throw new IllegalArgumentException("Vertex Number is not valid");
		}
		adjacencyMatrix[v1][v2] = 1;
		if (graphType == GraphType.UNDIRECTED) {
			adjacencyMatrix[v2][v1] = 1;
		}

	}

	@Override
	public List<Integer> getAdjacentVertices(int v) {
		List<Integer> adjacencyVerticesList = new ArrayList<Integer>();
		if (v < 0 || v >= numVertices) {
			throw new IllegalArgumentException("Vertex Number is not valid");
		}

		for (int i = 0; i < numVertices; i++) {
			if (adjacencyMatrix[v][i] == 1) {
				adjacencyVerticesList.add(i);
			}
		}

		Collections.sort(adjacencyVerticesList);
		return adjacencyVerticesList;
	}

}
