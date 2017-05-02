import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
	// Weighted graph
	protected double graph[][];
	
	public Graph(int vertexNum){
		graph = new double [vertexNum][vertexNum];
	}
	
	public void setWeight(int origin, int end, double weight){
		graph[origin][end] = weight;
	}
	
	public List<Integer> getAdjacentVertex(int vertexId){
		List<Integer> adjacentVertex = new ArrayList<>();
		for(int i=0; i<graph[vertexId].length; i++){
			if(graph[vertexId][i]>0)
				adjacentVertex.add(i);
		}
		return adjacentVertex;
	}
	
	// Shortest Path calculation
	public List<Integer> computeDijkstra(int origin, int end){
		double pathSum[] = new double[graph.length];
        int parent[] = new int[graph.length];
        Set<Integer> unvisited = new HashSet<>();
        pathSum[origin] = 0;

        // Initialization
        for (int i=0; i<graph.length; i++) {
            if(i!= origin) {
                pathSum[i] = Integer.MAX_VALUE;
            }
            parent[i] = -1;
            unvisited.add(i);
        }

        // Breadth First Search
        while (!unvisited.isEmpty()) {
            int next = closest(pathSum, unvisited);
            unvisited.remove(next);
            // Try shorter path
            for (Integer neighbor : getAdjacentVertex(next)) {
                double totalpathSum = pathSum[next] + getpathSum(next, neighbor);
                if (totalpathSum < pathSum[neighbor]) {
                    pathSum[neighbor] = totalpathSum;
                    parent[neighbor] = next;
                }
            }
            // Reach end point 
            if (next == end) {
            	graph[origin][end] = pathSum[end];
                return makePathList(parent, next);
            }
        }
        return Collections.emptyList();
	}
	
	// Find the id of the closest point
	private int closest(double[] dist, Set<Integer> unvisited) {
        double minDist = Integer.MAX_VALUE;
        int minIndex = 0;
        for (Integer i : unvisited) {
            if (dist[i] < minDist) {
                minDist = dist[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    private List<Integer> makePathList(int[] parent, int u) {
        List<Integer> path = new ArrayList<>();
        path.add(u);
        while (parent[u] != -1) {
            path.add(parent[u]);
            u = parent[u];
        }
        Collections.reverse(path);
        return path;
    }
    
    public double getpathSum(int origin, int end) {
        return graph[origin][end];
    }
}
