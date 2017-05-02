import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.jdesktop.layout.GroupLayout;

public class DeTriGUI extends JPanel{	
	private ArrayList<Vertex> vertices;
    private boolean allowToAddVertex;
    private ArrayList<ArrayList<Integer>> adjacencyList;
    
    private JButton buttonClear;
    private JButton buttonSave;
    private JButton buttonStretchFactor;
	
	public DeTriGUI(){
		vertices = new ArrayList<>();
        allowToAddVertex = true;
        adjacencyList = new ArrayList<>();		
		buttonStretchFactor = new JButton();
        buttonClear = new JButton();
        buttonSave = new JButton();

        // Set background as white
        setBackground(new java.awt.Color(255, 255, 255));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        // Set up button for Stretch Factor
        buttonStretchFactor.setText("Stretch Factor");
        buttonStretchFactor.setName("buttonStretchFactor");
        buttonStretchFactor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStretchFactorActionPerformed(evt);
            }
        });

        // Set up button for clear
        buttonClear.setText("Clear");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearActionPerformed(evt);
            }
        });

        // Set up button for save
        buttonSave.setText("Save");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	// Try-catch XML file stream exception
                try {
					buttonSaveActionPerformed(evt);
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				}
            }
        });

        // Swing jdesktop layout
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(buttonStretchFactor)
            .add(buttonSave)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(buttonClear)
            .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
            .addContainerGap(359, Short.MAX_VALUE)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(buttonClear)
            .add(buttonStretchFactor)
            .add(buttonSave))
            .addContainerGap())
        );		
	}
	
	@Override
    public void paint(Graphics g) {
        super.paint(g);   
        if (vertices == null) { return; }      
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);      
        // Draw points
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(4));
        for (Vertex v: vertices) {
            g2D.drawOval((int)(v.getX()), (int)(v.getY()), 4, 4);
        }        
        // Draw lines
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                   g2D.drawLine((int)(vertices.get(i).getX()), (int)(vertices.get(i).getY()), (int)(vertices.get(adjacencyList.get(i).get(j)).getX()), (int)(vertices.get(adjacencyList.get(i).get(j)).getY()));
            }
        }
    }
	
	// Compute Stretch Factor
	private void buttonStretchFactorActionPerformed(java.awt.event.ActionEvent evt) {
		int vertexNum = vertices.size();
		double stretchFactor = 1.0f;
		String resultMsg = "";
		List<Integer> shortestPath = new ArrayList<>();
		Graph DeTriGraph = new Graph(vertexNum);
		
		for(int i=0; i<adjacencyList.size(); i++){
			for(int j=0; j<adjacencyList.get(i).size(); j++){
				Vertex origin = vertices.get(i);
				Vertex end = vertices.get(adjacencyList.get(i).get(j));
				double weight = origin.getDistance(end);
				DeTriGraph.setWeight(i, adjacencyList.get(i).get(j), weight);
			}
		}
		
		for(int i=0; i<vertexNum; i++){
			for(int j=0; j<vertexNum; j++){
				Vertex origin = vertices.get(i);
				Vertex end = vertices.get(j);
				// Compute Dijkstra
				List<Integer> candidatePath = DeTriGraph.computeDijkstra(i, j);
				double euclideanDist = origin.getDistance(end);
				// Stretch Factor
				double ratio = DeTriGraph.graph[i][j]/euclideanDist;
				// Choose the max ratio
				if(ratio > stretchFactor){
					stretchFactor = ratio;
					shortestPath = candidatePath;
				}
			}
		}		
        this.repaint();        
        resultMsg = "Stretch Factor: "+ stretchFactor;       
        // Show stretch factor
        JOptionPane.showMessageDialog(null, resultMsg, "Stretch Factor Information", JOptionPane.PLAIN_MESSAGE);
    }

    private void buttonClearActionPerformed(java.awt.event.ActionEvent evt) {
        vertices.clear();
        adjacencyList.clear();
        this.repaint();
    }

    private void formMousePressed(java.awt.event.MouseEvent evt) {
    	int offset = 6;
    	int deleteIndex = -1;
    	String message = "";
    	// Select a point
        for (int i = 0; i < vertices.size(); i++) {
            if ((evt.getX() <= vertices.get(i).getX()+offset) && (evt.getX() >= vertices.get(i).getX()-offset)
            		&& (evt.getY() <= vertices.get(i).getY()+offset) && (evt.getY() >= vertices.get(i).getY()-offset)) {
            	allowToAddVertex = false;
            	deleteIndex = i;
            	message = "X-Coordinate: "+vertices.get(i).getX()+"\nY-Coordiante: "+vertices.get(i).getY();
                break;
            }
        }
        // Add a new vertex
        if (allowToAddVertex) {
            vertices.add(new Vertex(evt.getX(), evt.getY(), vertices.size()));
            adjacencyList.add(new ArrayList<Integer>());
            for (ArrayList<Integer> al : adjacencyList) {
                al.clear();
            }
            // Random Permutation
            Collections.shuffle(vertices);
            
            for (int i = 0; i < vertices.size(); i++) {
                vertices.get(i).setId(i);
            }
            // Compute Delaunay triangulation
            DeTriAlgorithm.DelaunayTriangulation(vertices, adjacencyList);
            this.repaint();
        }
        else{
        	// Mouse left click event
        	if(evt.getButton()==java.awt.event.MouseEvent.BUTTON1){
        		JOptionPane.showMessageDialog(null, message, "Point Information", JOptionPane.PLAIN_MESSAGE);
        	}
        	// Mouse right click event
        	else{
        		String confirmMsg = "Delete the selected point?";
        		int response = JOptionPane.showConfirmDialog(null, confirmMsg, "Confirmation", JOptionPane.YES_NO_OPTION);
        		if(response==JOptionPane.YES_OPTION && deleteIndex >= 0){
        			vertices.remove(deleteIndex);
        			adjacencyList.add(new ArrayList<Integer>());
                    for (ArrayList<Integer> al : adjacencyList) {
                        al.clear();
                    }
                    Collections.shuffle(vertices);
                    for (int i = 0; i < vertices.size(); i++) {
                        vertices.get(i).setId(i);
                    }
                    DeTriAlgorithm.DelaunayTriangulation(vertices, adjacencyList); // Compute Delaunay triangulation
                    this.repaint();
        		}
        	}
        }  
        allowToAddVertex = true;     
    }

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) 
    		throws ParserConfigurationException, IOException, TransformerException {
    	String saveMsg = "Save as";
    	// Input filename
    	String IPEname = JOptionPane.showInputDialog(null, saveMsg, "Save", JOptionPane.PLAIN_MESSAGE);
    	if(IPEname != null && !IPEname.contains(".")){
    		String savePath = System.getProperty("user.dir");
    		IPEname = savePath + "/" + IPEname + ".ipe";
    		System.out.println(IPEname);
    		IPE ipe = new IPE();
    		// Generate ipe
        	if(ipe.IpeGenerator(vertices, adjacencyList, IPEname)){
        		String confirmMsg = "Saved Successfully! \nLocation: "+ IPEname;
        		JOptionPane.showMessageDialog(null, confirmMsg, "Save", JOptionPane.PLAIN_MESSAGE);
        	}
        	else
        		JOptionPane.showMessageDialog(null, "Unable to save file", "Error", JOptionPane.ERROR_MESSAGE);
    	}
    	else
    		JOptionPane.showMessageDialog(null, "File name error", "Error", JOptionPane.ERROR_MESSAGE);
    }
}