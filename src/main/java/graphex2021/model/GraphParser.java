package graphex2021.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.*;


/**
 * Class to parse JSON files which specify a graph
 * as only one parser is ever needed this is a singleton
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GraphParser {

    private static final GraphParser singleInstance = new GraphParser();
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
    public GraphParser getGraphParser() {
        return singleInstance;
    }

    /**
     * method to parse a input file and read all vertices from it
     * parsing vertices indicates a new graph is being read and the ids will be reset
     * @param input the file the graph should be read from
     * @return a collection of GXVertex specified in the file
     */
    public Collection<GXVertex> parseVertices(File input) {
        freeVertexId = 0;
        freeEdgeId = 0;

        Collection<GXVertex> vertexList = new <GXVertex>LinkedList();
        String inputFile = "";
        try {
            inputFile = Files.readString(input.toPath());
        } catch (IOException e) {
            //TODO shouldnt happen
        }
        JSONObject graphObject = new JSONObject(inputFile);
        JSONArray verticesArray = graphObject.getJSONArray("vertices");
        for(int i = 0; i < verticesArray.length(); i++) {
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

    public Collection<GXEdge> parseEdges(File input) {
        return null;
    }

    public GXVertex parseStarting(File input){
        return null;
    }

    public GXVertex parseEnding(File input) {
        return null;
    }

    private int getNextVertexId() {
        return freeVertexId++;
    }

    private int getNextEdgeId() {
        return  freeEdgeId++;
    }
}
