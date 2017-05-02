import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class IPE {
	
	public boolean IpeGenerator(List<Vertex> vertexList, ArrayList<ArrayList<Integer>> graph, String IPEname) 
			throws ParserConfigurationException, IOException, TransformerException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		
		// Generate DOCTYPE
		DOMImplementation domImpl = document.getImplementation();
		DocumentType docType = domImpl.createDocumentType("ipe", "SYSTEM", "ipe.dtd");
		document.appendChild(docType);
		
		// Generate root element
		Element rootIpe = document.createElement("ipe");		
		rootIpe.setAttribute("version", "70206");
		rootIpe.setAttribute("creator", "Ipe 7.2.7");
		document.appendChild(rootIpe);
		
		// Generate page element
		Element page = document.createElement("page");
		rootIpe.appendChild(page);
		
		// Generate path elements
		for(int i=0; i<graph.size(); i++){
			for(int j=0; j<graph.get(i).size(); j++){
				Vertex origin = vertexList.get(i);
				Vertex end = vertexList.get(graph.get(i).get(j));
				Element path = document.createElement("path");
				String pathXML = "\n"+origin.getX()+" "+origin.getY()+" m\n" +end.getX()+" "+end.getY()+" l\n"+"h\n";
				path.setTextContent(pathXML);
				page.appendChild(path);
			}
		}
		
		// Generate XML
		TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transFormer = transFactory.newTransformer(); 
        transFormer.setOutputProperty(OutputKeys.INDENT, "yes");

        //String savePath = System.getProperty("user.dir") + "/files/";
        //IPEname = savePath + IPEname + ".ipe";
        DOMSource domSource = new DOMSource(document);
        File file = new File(IPEname); 
        if (!file.exists())  
        	file.createNewFile();

        // Output file
        FileOutputStream out = new FileOutputStream(file);       
        StreamResult xmlResult = new StreamResult(out);   
        transFormer.transform(domSource, xmlResult);
        
        return true;
	}
}
