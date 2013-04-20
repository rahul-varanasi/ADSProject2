import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * @author Rahul Varanasi
 * @800#   800807351
 * Vertex class implements Comparable
 * because it is needed in PriorityQueue
 */
class Vertex implements Comparable<Vertex> {
	String vertexName = null;
	Double transmitTime = Double.POSITIVE_INFINITY;
	ArrayList<Edge> adj = new ArrayList<Edge>();
	Vertex previousVertex;
	boolean vertexUP = true;
	
	/**
	 * Constructor method
	 * @param vertex the name of the vertex
	 */
	public Vertex(String vertex) {
		vertexName = vertex;
		vertexUP = true;
	}
	
	/**
	 * Overriding the object method for printing vertex name
	 * @return vertexName the name of the vertex
	 */
	@Override
	public String toString() {
		return vertexName; 
	}
	
	/**
	 * Overriding the abstract compareTo() method
	 * because Vertex class implements Comparable class
	 */
	@Override
	public int compareTo(Vertex v) {
	    return Double.compare(transmitTime, v.transmitTime);
	}
	
}

/**
 * Edge class is for storing Edge information
 */
class Edge {
	Vertex vertexTo = null;
    double transmitTime = Double.POSITIVE_INFINITY;
    boolean edgeUP = true;
    
    /**
     * Constructor
     * @param argVertexName the name of the tail vertex
     * @param argTransmitTime the transmit time of the edge
     */
    public Edge(Vertex argVertexName, double argTransmitTime) { 
    	vertexTo = argVertexName; 
    	transmitTime = argTransmitTime;
    	edgeUP = true;
    }
    
}

public class Project2 {
	/* Master Vertex List for having a unique list of Vertices */
	static HashSet<String> masterVertexList = new HashSet<String>();
	
	/* Array List for storing Vertices. It is used for further computation */
	static ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	/* Current Vertex for computation. Used in some methods */
	static Vertex currentSource;
	
	/**
	 * Method to compute the shortest path between
	 * two vertices using Dijkstra's algorithm
	 * @param src the source vertex
	 */
	public static void computePaths(String src) {
	    Vertex srcVertex = null;
		srcVertex = getVertexFromVertexName(src);
		
		/* While computing a vertex, we have to make its weight = 0 and others INFINITY */
		for(Vertex v : vertices) {
			v.transmitTime = Double.POSITIVE_INFINITY;
		}
		srcVertex.transmitTime = 0.0;
		
		/* All the previous vertices should be null */
		srcVertex.previousVertex = null;
		
		/* This PriorityQueue is used for maintaining the list of computed vertices */ 
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(srcVertex);
		
		while (!vertexQueue.isEmpty()) {
			/* Vertex u is the src vertex */
			Vertex u = vertexQueue.poll();
			for(Edge e : u.adj) {
	            /* Vertex v is the dest vertex */
	        	Vertex v = e.vertexTo;
	            Double transmitTime = e.transmitTime;
	        	
	            /* If vertex or edge is DOWN continue the loop */
	            if(!u.vertexUP || !v.vertexUP || !e.edgeUP)
	        		continue;
	            
	            /* Current transmit time */
	            double currentTransmitTime = u.transmitTime + transmitTime;
	            if (currentTransmitTime < v.transmitTime) {
	                /* Code for relaxing the edges */
	            	vertexQueue.remove(v);
	                v.transmitTime = currentTransmitTime ;
	                v.previousVertex = u;
	                vertexQueue.add(v);
	            }
	            
	        }
	    }
	}
	
	/**
	 * Method to compute the shortest paths
	 * @param destVertex the destination vertex
	 * @param writer the BufferedWriter for writing output to file
	 * @throws IOException 
	 */
	public static void getShortestPath(String dest, BufferedWriter writer) throws IOException {
		
		Vertex destVertex = null;
				
		destVertex = getVertexFromVertexName(dest);
		
		/* List for storing the shortest path */
		ArrayList<Vertex> shortestPath = new ArrayList<Vertex>();
	    for (Vertex v = destVertex; v != null; v = v.previousVertex) {
	    	/* If vertex is down continue loop, else add the vertex to list */
	    	if(!v.vertexUP)
	    		continue;
	    	else
	    		shortestPath.add(v);
	    }
	    /* Reverse the list for correct order */
	    Collections.reverse(shortestPath);
	    	    
	    for(Vertex v : shortestPath) {
//			System.out.print(v.vertexName + " ");
			writer.append(v.vertexName);
	    }
	    DecimalFormat df = new DecimalFormat("0.00");
//		System.out.println(df.format(destVertex.transmitTime) + "\n");
		writer.append(df.format(destVertex.transmitTime) + "\n");
	}
	
	/**
	 * Method for getting a Vertex when vertexName is passed
	 * @param vertexName the name of the vertex
	 * @return Vertex the Vertex which matches with the vertexName
	 */
	public static Vertex getVertexFromVertexName(String vertexName) {
		for(Vertex v : vertices) {
			if(v.vertexName.equals(vertexName))
				return v;
		}
		return null;
	}
	
	/**
	 * Method for getting an Edge when vertexName is passed
	 * @param vertexName the name of the destination vertex of the edge
	 * @return the edge which corresponds to the destination vertex
	 */
	public static Edge getEdgeFromEdgeName(String vertexName) {
		ArrayList<Edge> edges = currentSource.adj;
		for(Edge e : edges) {
			if(e.vertexTo.vertexName.equals(vertexName))
				return e;
		}
		return null;
	}
	
	
	/**
	 * Method for printing the graph
	 * @param writer BufferedWriter for writing output to file
	 * @throws IOException 
	 */
	public static void printNetworkGraph(BufferedWriter writer) throws IOException {
				
		ArrayList<Vertex> tempVertices = vertices;
		ArrayList<String> tempVertex = new ArrayList<String>();
				
		/* Get a list of srcVertex in alphabetical order
		 * Add the vertexName to an ArrayList and sort the
		 * ArrayList Collection for making the list alphabetical 
		 *  */
		for(Vertex v : tempVertices) {
			tempVertex.add(v.vertexName);
		}
		/* Sort the list for alphabetical order */
		Collections.sort(tempVertex);
		
		for(String stV : tempVertex) {
			currentSource = getVertexFromVertexName(stV);
			if(!currentSource.vertexUP) {
//				System.out.println(stV + " DOWN");
				writer.append(stV + " DOWN");
			}
			else {
//				System.out.println(stV);
				writer.append(stV);
			}
			
			ArrayList<Edge> edges = currentSource.adj;
			ArrayList<String> tempEdges = new ArrayList<String>();
			/* For each edge in current vertex's adjacencies */
			for(Edge e : edges) {
				tempEdges.add(e.vertexTo.vertexName);
			}
			/* Sort the list for alphabetical order */
			Collections.sort(tempEdges);
			
			for(String stE : tempEdges) {
				Edge e = getEdgeFromEdgeName(stE);
				if(!e.edgeUP) {
//					System.out.println("\t" + stE + " " + e.transmitTime + " DOWN");
					writer.append("\t" + stE + " " + e.transmitTime + " DOWN");
				}
				else {
//					System.out.println("\t" + stE + " " + e.transmitTime);
					writer.append("\t" + stE + " " + e.transmitTime);
				}
			}
		}
	}
	
	/**
	 * Method for printing reachable vertices
	 * @param writer the BufferedWriter for writing output to file
	 * @throws IOException
	 */
	public static void printReachable(BufferedWriter writer) throws IOException {
				
		ArrayList<Vertex> tempVertices = vertices;
		ArrayList<String> tempVertex = new ArrayList<String>();
				
		/* Get a list of srcVertex in alphabetical order
		 * Add the vertexName to an ArrayList and sort the
		 * ArrayList Collection for making the list alphabetical 
		 *  */
		for(Vertex v : tempVertices) {
			tempVertex.add(v.vertexName);
		}
		Collections.sort(tempVertex);
		
		for(String stV : tempVertex) {
			currentSource = getVertexFromVertexName(stV);
			if(currentSource.vertexUP) {
//				System.out.println(stV);
				writer.append(stV);
			}
			else
				continue;
									
			ArrayList<Edge> edges = currentSource.adj;
			ArrayList<String> tempEdges = new ArrayList<String>();
			
			for(Edge e : edges) {
				tempEdges.add(e.vertexTo.vertexName);
			}
			Collections.sort(tempEdges);
			
			for(String stE : tempEdges) {
				Edge e = getEdgeFromEdgeName(stE);
				if(e.edgeUP) {
//					System.out.println("\t" + stE);
					writer.append("\t" + stE);
				}
				
			}
		}
	}
	
	/**
	 * Method for adding a new edge
	 * @param src the source vertex
	 * @param dest the destination vertex
	 * @param time the transmit time for the edge
	 */
	public static void addEdge(String src, String dest, Double time) {
		
		boolean srcExists = false;
		boolean destExists = false;
		boolean edgeExists = false;
		
		for(Vertex v : vertices) {
			if(v.vertexName.equals(src))
				srcExists = true;
			
			if(v.vertexName.equals(dest))
				destExists = true;
		
			for(Edge e : v.adj) {
				if(srcExists && destExists && (e.vertexTo.vertexName.equals(dest)))
					edgeExists = true;
			}
		}
				
		if(!srcExists) {
			vertices.add(new Vertex(src));
			srcExists = true;
		}
		if(!destExists) {
			vertices.add(new Vertex(dest));
			destExists = true;
		}
		
		if(srcExists && destExists && !edgeExists) {
			for(Vertex v : vertices) {
				if(v.vertexName.equals(src)) {
					v.adj.add(new Edge(getVertexFromVertexName(dest), time));
				}
			}
		}
		/* If source, destination and edge exists, change the transmission time */
		else {
			for(Vertex v : vertices) {
				if(v.vertexName.equals(src)) {
					for(Edge e : v.adj) {
						if(e.vertexTo.vertexName == dest) {
							e.vertexTo.transmitTime = time;
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Method for deleting an edge
	 * @param src the source vertex
	 * @param dest the destination vertex
	 */
	public static void deleteEdge(String src, String dest) {
		
		boolean srcExists = false;
		boolean destExists = false;
		boolean edgeExists = false;
		
		for(Vertex v : vertices) {
			if(v.vertexName.equals(src))
				srcExists = true;
			
			if(v.vertexName.equals(dest))
				destExists = true;
		
			for(Edge e : v.adj) {
				if(srcExists && destExists && (e.vertexTo.vertexName.equals(dest)))
					edgeExists = true;
			}
		}
		/* If source, destination and edge exists, delete the edge */
		if(srcExists && destExists && edgeExists) {
			for(Vertex v : vertices) {
				if(v.vertexName.equals(src)) {
					for(Edge e : v.adj) {
						if(e.vertexTo.vertexName.equals(dest)) {
							v.adj.remove(e);
						}
					}
				}
			}
		}
	}
	
	/**
	 * This method is used to quit the program
	 */
	public static void quit() {
		System.exit(0);
	}
	
	/**
	 * This method is for marking a Vertex as "DOWN"
	 * @param vertex the Vertex which has to be marked "DOWN"
	 */
	public static void vertexDown(String vertex) {
		
		for(Vertex v : vertices) {
			if(v.vertexName.equals(vertex)) {
				v.vertexUP = false;
			}
		}
	}
	
	/**
	 * This method is for marking a Vertex as "UP"
	 * By default, every Vertex is marked "UP"
	 * This method can be used to marked a Vertex which
	 * recently went down as "UP"
	 * @param vertex the Vertex which has to be marked "UP"
	 */
	public static void vertexUp(String vertex) {
		
		for(Vertex v : vertices) {
			if(v.vertexName.equals(vertex)) {
				v.vertexUP = true;
			}
		}
	}
	
	/**
	 * This method is for marking an Edge as "DOWN"
	 * @param src the source Vertex
	 * @param dest the destination Vertex
	 */
	public static void edgeDown(String src, String dest) {
		
		for(Vertex v : vertices) {
			if(v.vertexName.equals(src)) {
				for(Edge e : v.adj) {
					if(e.vertexTo.vertexName.equals(dest)) {
						e.edgeUP = false;
					}
				}
			}
		}
	}
	
	/**
	 * This method is for marking an Edge as "UP"
	 * @param src the source Vertex
	 * @param dest the destination Vertex
	 */
	public static void edgeUp(String src, String dest) {
		
		for(Vertex v : vertices) {
			if(v.vertexName.equals(src)) {
				for(Edge e : v.adj) {
					if(e.vertexTo.vertexName.equals(dest)) {
						e.edgeUP = true;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
				
		/* Try Catch block for BufferedReader and BufferedWriter 
		 * This is used for file reading, building the graph and running the queries */		
		try {
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(args[1]));
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
		    String line = reader.readLine();
		    String graphPath = null;
		    
		    String[] path = line.split(" ");
		    graphPath = path[1];
//		    System.out.println("Path of graph in queries file: " + graphPath + "\n");
		        
		    /* pathLine[1], which is the 2nd token in the first line of 
		     * args[0] file, stores the path of the network file */
		    BufferedReader readGraph = new BufferedReader(new FileReader(graphPath));
		    String graphLine = null; 
	        
		    while((graphLine = readGraph.readLine()) != null) {
		      	
			    String[] lineSplits = graphLine.split(" ");
			        
			    masterVertexList.add(lineSplits[0]);
			    masterVertexList.add(lineSplits[1]);
			}
		    Iterator<String> itr = masterVertexList.iterator();
		    while(itr.hasNext()) {
		       	vertices.add(new Vertex(itr.next()));
		    }
//		    System.out.println(masterVertexList);
//		    System.out.println(vertices);
		    readGraph.close();
		    
		    BufferedReader makeGraph = new BufferedReader(new FileReader(graphPath));
		    while((graphLine = makeGraph.readLine()) != null) {
//		    	System.out.println(graphLine);
		    	String[] lineSplits = graphLine.split(" ");
		    			    	
		    	for(Vertex v : vertices) {
		    		if(v.vertexName.equals(lineSplits[0])) {
		    			Vertex temp = getVertexFromVertexName(lineSplits[1]);
		    			v.adj.add(new Edge(temp, Double.parseDouble(lineSplits[2])));
		    		}
		    	}
		    	for(Vertex v : vertices) {
		    		if(v.vertexName.equals(lineSplits[1])) {
		    			Vertex temp = getVertexFromVertexName(lineSplits[0]);
		    			v.adj.add(new Edge(temp, Double.parseDouble(lineSplits[2])));
		    		}
		    	}
		    }
		    makeGraph.close();
		    
		    String commands;
		    while((line = reader.readLine()) != null) {
		    	String[] queryArr = line.split(" ");
		    	commands = queryArr[0];
		    	if(commands.equals("print")) {
		    		printNetworkGraph(outputWriter);
		    	}
		    	
		    	if(commands.equals("reachable")) {
		    		printReachable(outputWriter);
		    	}
		    	
		    	if(commands.equals("vertexdown")) {
		    		vertexDown(queryArr[1]);
		    	}
		    	
		    	if(commands.equals("vertexup")) {
		    		vertexUp(queryArr[1]);
		    	}
		    	
		    	if(commands.equals("edgedown")) {
		    		edgeDown(queryArr[1], queryArr[2]);
		    	}
		    	
		    	if(commands.equals("edgeup")) {
		    		edgeUp(queryArr[1], queryArr[2]);
		    	}
		    	
		    	if(commands.equals("quit")) {
		    		quit();
		    	}
		    	
		    	if(commands.equals("path")) {
		    		computePaths(queryArr[1]);
		    		getShortestPath(queryArr[2], outputWriter);
		    	}
		    	
		    	if(commands.equals("addedge")) {
		    		addEdge(queryArr[1], queryArr[2], Double.parseDouble(queryArr[3]));
		    	}
		    	
		    	if(commands.equals("deleteedge")) {
		    		deleteEdge(queryArr[1], queryArr[2]);
		    	}
		    	
		    }
		    reader.close();
		    outputWriter.close();
		} catch(IOException ex) {
		    ex.printStackTrace();
		}
		
	}
	
}