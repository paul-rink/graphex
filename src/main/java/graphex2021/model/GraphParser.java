package graphex2021.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;


import org.everit.json.schema.ValidationException;
import org.json.*;
import org.everit.json.schema.*;
import org.everit.json.schema.loader.*;

/**
 * Class to parse JSON files which specify a graph
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GraphParser {

    private static final GraphParser singleInstance = new GraphParser();
    private static final String GRAPH_TYPE_FILE = "resources/graphex2021/GraphData/graph-scheme.json";
    private int freeVertexId;
    private  int freeEdgeId;


    /**
     * private constructor for the singleton
     */
    private GraphParser () {
        freeEdgeId = 0;
        freeVertexId = 0;
    }

    /**
     * method to get the single instance of the GraphParser
     * @return the instance
     */
    public static GraphParser getGraphParser() {
        return singleInstance;
    }

    /**
     * method to parse a input file and read all vertices from it
     * parsing vertices indicates a new graph is being read and the ids will be reset
     * @param input the file the graph should be read from
     * @return a collection of GXVertex specified in the file
     */
    public Collection<GXVertex> parseVertices(File input) throws WrongFileFormatException {
        freeVertexId = 0;
        freeEdgeId = 0;
        Collection<GXVertex> vertexList = new <GXVertex>ArrayList();

        JSONObject graphObject = null;
        graphObject = getJsonObject(input);

        JSONArray verticesArray = graphObject.getJSONArray("vertices");
        for (int i = 0; i < verticesArray.length(); i++) {
            JSONObject jsonVertex = verticesArray.getJSONObject(i);
            String vertexName = jsonVertex.getString("name");
            int posx = jsonVertex.getInt("posx");
            int posy = jsonVertex.getInt("posy");
            GXPosition vertexPosition = new GXPosition(posx, posy);
            int id = getNextVertexId();
            GXVertex vertex = new GXVertex(vertexName, id, vertexPosition);
            vertexList.add(vertex);
        }
        return vertexList;
    }

    /**
     * method to parse the vertices from a file
     * @param input the file from which the vertices are parsed
     * @param vertices the collection of vertices the edges will connect
     * @return a Collection of edges for the graph
     */
    public Collection<GXEdge> parseEdges(File input, Collection<GXVertex> vertices) throws WrongFileFormatException {
        Collection<GXEdge> edgeList = new ArrayList<>();

        JSONObject graphObject = null;
        graphObject = getJsonObject(input);

        JSONArray edgeArray = graphObject.getJSONArray("edges");

        for (int i = 0; i < edgeArray.length(); i++) {
            JSONObject jsonEdge = edgeArray.getJSONObject(i);
            String vertexOneName = jsonEdge.getString("vertex1");
            String vertexTwoName = jsonEdge.getString("vertex2");
            int edgeWeight = jsonEdge.getInt("weight");
            //find first vertex
            GXVertex firstVertex = findMatchingVertex(vertexOneName, vertices);
            GXVertex secondVertex = findMatchingVertex(vertexTwoName, vertices);
            if (firstVertex != null && secondVertex != null) {
                int edgeId = getNextEdgeId();
                GXEdge edge = new GXEdge(firstVertex, secondVertex, Integer.toString(edgeWeight), edgeWeight, edgeId);
                edgeList.add(edge);
            }
        }
        return edgeList;
    }

    /**
     * method that parses which vertex is designated as the start vertex in the file
     * @param input the file in which the graph is specified
     * @param vertices the list of vertices in the graph
     * @return GXVertex that should be the designated starting vertex
     */
    public GXVertex parseStarting(File input, Collection<GXVertex> vertices)  throws WrongFileFormatException {
        JSONObject graphObject = null;
        graphObject = getJsonObject(input);

        String startName =  graphObject.getString("startVertex");
        return findMatchingVertex(startName, vertices);
    }

    /**
     * method that parses which vertex is designated as the end vertex in the file
     * @param input the file in which the graph is specified
     * @param vertices the list of vertices in the graph
     * @return GXVertex that should be the designated ending vertex
     */
    public GXVertex parseEnding(File input, Collection<GXVertex> vertices) throws WrongFileFormatException{
        JSONObject graphObject = null;
        graphObject = getJsonObject(input);

        String endName =  graphObject.getString("endVertex");
        return findMatchingVertex(endName, vertices);
    }

    /**
     * method that takes a file and creates a JSONObject from it
     * @param input the file which is being read
     * @return a JSONObject which is defined in the file
     */
    private JSONObject getJsonObject(File input) throws WrongFileFormatException {
        String inputFile = readFromFile(input);

        JSONObject object = null;
        try {
            object = new JSONObject(inputFile);
        } catch (JSONException e) {
            throw new WrongFileFormatException(e.getMessage());
        }
        try {
            checkFileFormat(object);
        } catch (ValidationException e) {
            throw new WrongFileFormatException(e.getErrorMessage());
        }
        return object;
    }

    private void checkFileFormat(JSONObject input) throws ValidationException, WrongFileFormatException {
        File graphType = null;
        try {
            graphType = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        } catch (URISyntaxException e) {
            graphType = null;
            throw new WrongFileFormatException(e.getMessage());
        }
        String jsonSchemaString = readFromFile(new File(graphType, GRAPH_TYPE_FILE));
        JSONObject jsonSchema = new JSONObject(jsonSchemaString);
        Schema schema = SchemaLoader.load(jsonSchema);
        schema.validate(input);
    }

    /**
     * method to read a string from a given File
     * @param input the File the string should be read from
     * @return a String with the contents of the File
     */
    private String readFromFile(File input) throws WrongFileFormatException {
        String output = "";
        try {
            output = Files.readString(input.toPath());
        } catch (IOException e) {
            throw new WrongFileFormatException(input.getAbsolutePath()+ "couldnt read.");
        } catch (NullPointerException r) {
            r.printStackTrace();
        }
        return output;
    }

    /**
     * method that takes a String and a Collection of vertices and tries to find a vertex
     * with a matching name/element
     * @param vertexName the name of the vertex being searched
     * @param vertices collection of vertices
     * @return either a vertex with matching name or null
     */
    private GXVertex findMatchingVertex(String vertexName, Collection<GXVertex> vertices) {
        Iterator<GXVertex> it = vertices.iterator();
        GXVertex matchingVertex = null;
        while (it.hasNext()) {
            GXVertex vertex = (GXVertex) it.next();
            if (vertex.element().equals(vertexName)) {
                matchingVertex = vertex;
            }
        }
        return  matchingVertex;
    }

    /**
     * returns an integer and counts up one
     * this is used to identify vertices
     * @return the next free id
     */
    private int getNextVertexId() {
        return freeVertexId++;
    }

    /**
     * returns an integer and counts up one
     * this is used to identify edges
     * @return the next free id
     */
    private int getNextEdgeId() {
        return  freeEdgeId++;
    }
}
