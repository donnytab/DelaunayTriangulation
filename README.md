# DelaunayTriangulation
1. Introduction
1.1 Overview
The Delaunay Triangulation project is an implementation of randomized incremental algorithm for Delaunay triangulation. This project application is implemented using Java under the environment of JDK 1.8 in Eclipse (Neon.2 Release (4.6.2)).

1.2 Goals and Objectives
The main goal of this application is to provide the following functionalities:
(1) Add new points to the point set and perform a Delaunay triangulation.
(2) Delete existing points from the point set and maintain a Delaunay triangulation.
(3) Compute the stretch factor for the current graph.
(4) Save current graph as an “.ipe” format file.


2. Implementation
2.1 Software Files Structure

2.2 DeTriAlgorithm
The DeTriAlgorithm.java is the core component of this application. It implements the randomized incremental algorithm for Delaunay triangulation according to the pseudo code of this algorithm in Computational Geometry - Algorithms and Applications 3rd Ed. This implementation relies on the class structure of Triangle for the computation of the triangulation. The initialization involves the constructions of three initial points and Collections.shuffle() to randomize the point set. Based on the position of the new point, there are 2 cases for the triangulation. The function of legalizeEdge() has also been implemented to verify the correctness of the existing edges.

2.3 DeTriGUI
The DeTriGUI.java is the setup file for the graphical user interface for the application. It includes two important data structures: vertices (the ArrayList of the point set) and adjacencyList (the two dimensional ArrayList of the connecting vertices). Also there are three buttons: Stretch Factor, Save, Clear. 

The computation of stretch factor of the graph is to use the definition of stretch factor. For each vertex, the shortest paths to any other vertices are computed using the Dijkstra Algorithm built in the Graph.java and then divided by their Euclidean distances. The operation of save is implemented in IPE.java. The operation of clear is to remove all the elements in vertices and adjacencyList. 

To demonstrated the change of the Delaunay triangulation of the current point set, DeTriGUI extends JPanel and overrides the function of paint() so that when there is a operation applied to the point set, the repaint() function will call the overridden paint() to draw the new Delaunay triangulation.

2.4 DeTriMain
The DeTriMain.java is the main file for this application. It extends JFrame to initialize and display the GUI and start the entire application in run().

2.5 Edge
The Edge.java is the class structure representing the edges in Delaunay triangulation. It mainly includes the id of the edge, the origin vertex of the edge and the end vertex of the edge. This class is mainly used in the DeTriAlgorithm.

2.6 Graph
The Graph.java is to represent the weighted undirected graph of the Delaunay triangulation using a two dimensional array. It includes a simple implementation of Dijkstra algorithm using a queue-like set and the idea of breadth first search. The values stored in the graph are the distances of shortest path between any two vertices in the current Delaunay triangulation and will be used to calculate the stretch factor of the graph.

2.7 IPE
The IPE.java is a class structure to build Ipe format files. Ipe files are XML files. They have unique doctype called ipe and are defined by ipe.dtd. In my implementation, this XML files are simplified with only key elements left. They have an XML document as follow:

<ipe version="70206" creator="Ipe 7.2.7" >
	<page>
		<path>
		</path>
		<path>
		     …
		</path>
	</page>
</ipe>

Based on the documentation of the IPE official website (http://ipe.otfried.org/manual/manual.html ), m (move to) can be used to begin new path and l (line to) can be used to add straight line to the path.  And h is used to close a path. Since this application is about triangulation, operator m, l and h are enough for the construction of all paths in a Delaunay triangulation. By storing all the X and Y coordinate values to m and l respectively for each two points in every edge in the graph, the XML files can record the entire graph. 

2.8 Triangle
The Triangle.java is a class structure representing the shape of triangle of Delaunay triangulation in this application. It includes auxiliary functions like pointInCircumcircle() to check whether a point is inside the circumcircle and findNearestEdge() to get the nearest edge inside a triangle.

2.9 Vertex
The Vertex.java is a class structure representing the vertex in the graph of Delaunay triangulation. It has auxiliary functions for distance calculation and basic arithmetic operations for two given vertices.


3. User Guide
3.1 UI Design

3.2 Add Points
	Click left mouse button on a blank space to add points.

3.3 Display Point Information
	Click left mouse button on a point to display the information of this point.

3.4 Delete Points
	Click right mouse button on a point to delete a point.

3.5 Display Stretch Factor
	Click Stretch Factor button to display the stretch factor of the current graph.

3.6 Save
	Save the current graph as an Ipe file with a given filename in the folder of the root path of this application. Any cancel operation will result in error and the file will not be saved. You can also save as ”files/...” to save the file to the “files” folder.
	
Saved Ipe file can be open by Ipe application. In this documentation, the version of Ipe for testing is 7.2.7. Since the Y coordinate in IPE is opposite to the one used by mouse motion capture system, the loaded Ipe file is not identical to the original file visually but with the correct coordinates.

3.7 Clear
	Clear all points and the current graph.


4. Reference
[1] “Delaunay Triangulation Algorithm and Application to Terrain Generation”, Faniry Harijaona Razafindrazaka
[2] ”Voronoi diagrams and Delaunay triangulations”, Steven Fortune, 1995
[3] "Computational Geometry - Algorithms and Applications 3rd Ed”, Mark de Berg, Otfried Cheong, Marc van Kreveld, Mark Overmars
[4] The Ipe manual (http://ipe.otfried.org/manual/manual.html )
