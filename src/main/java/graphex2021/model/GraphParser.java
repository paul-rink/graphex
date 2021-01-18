package graphex2021.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
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

    private GraphParser () {
    }

    public GraphParser getGraphParser() {
        return singleInstance;
    }


    public Collection<GXVertex<V>> parseVertices(File input) {
        String inputFile = "";
        try {
            inputFile = Files.readString(input.toPath());
        } catch (IOException e) {
            //TODO shouldnt happen
        }
        JSONObject graphObject = new JSONObject(inputFile);
        JSONArray verticesArray = graphObject.getJSONArray("vertices");
        for(int i = 0; i < verticesArray.length(); i++) {
            String VertexName = verticesArray.getString(i);

        }
        return null;
    }

    public Collection<GXEdge<E,V>> parseEdges(File input) {
        return null;
    }

    public GXVertex parseStarting(File input){
        return null;
    }

    public GXVertex parseEnding(File input) {
        return null;
    }
}
