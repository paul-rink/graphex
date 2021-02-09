package graphex2021.model;


import org.junit.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


import java.util.Iterator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



public class GraphParserTest {

    private static final File originalFile = new File("src/test/resources/GraphData/testGraph.json");
    private static final File TEST_DIR = new File("src/test/resources/GraphData/test");
    private static final File copiedFile = new File("src/test/resources/GraphData/test/testFile.json");
    private static GraphParser parser;

    /**
     * make sure output folder is empty
     */
    @BeforeClass
    public static void beforeClass() {

        int i;
        if (TEST_DIR.exists()) {
            for (File f : TEST_DIR.listFiles()) {
                f.delete();
            }
        } else {
            TEST_DIR.mkdirs();
        }

        //String testPathFile = originalFile.getAbsolutePath();
        //String testPathDir = TEST_DIR.getAbsolutePath();



    }
    @Before
    public void setUp() throws Exception {
        this.parser = GraphParser.getGraphParser();
        Path copied = copiedFile.toPath();

        Path originalPath = originalFile.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }

    //@Ignore
    @Test
    public void testParseVertices() {
        Collection<GXVertex> expectation = createExpectedVertices();

        //read the vertex from the file
        Collection<GXVertex> verticesList = null;
        try {
            verticesList = parser.parseVertices(copiedFile);
        } catch (WrongFileFormatException e) {
            assertTrue(false);
        }

        Iterator<GXVertex> it = verticesList.iterator();
        Iterator<GXVertex> ite = expectation.iterator();
        Boolean allSame = true;
        assertEquals(expectation.size(), verticesList.size());
        while (it.hasNext() && ite.hasNext()) {
            GXVertex expectedVertex = ite.next();
            GXVertex readVertex = it.next();
            if (!sameVertices(expectedVertex, readVertex)) {
                allSame = false;
            }
        }
        assertTrue(allSame);
    }


    //@Test
    @Ignore
    public void testParseEdges() {
        Collection<GXVertex> expectedVertices = createExpectedVertices();
        Collection<GXEdge> expectedEdges = createExpectedEdges(expectedVertices);

        Collection<GXEdge> readEdges = null;
        try {
            readEdges = parser.parseEdges(copiedFile, expectedVertices);
        } catch (WrongFileFormatException e) {
            assertTrue(false);
        }
        assertEquals(expectedEdges.size(), readEdges.size());
        Iterator<GXEdge> it = readEdges.iterator();
        Iterator<GXEdge> ite = expectedEdges.iterator();
        while (it.hasNext() && ite.hasNext()) {
            assertTrue(sameEdge(ite.next(), it.next()));
        }

    }

    private boolean sameEdge(GXEdge expectedEdge, GXEdge readEdge) {
        GXVertex[] expVert = expectedEdge.vertices();
        GXVertex[] readVert = readEdge.vertices();
        for(int i = 0; i < expVert.length; i++) {
            if(!sameVertices(expVert[i], readVert[i])) {
                return false;
            }
        }
        if(!expectedEdge.element().equals(readEdge.element())) {
            return false;
        }
        if(expectedEdge.getWeight() != readEdge.getWeight()) {
            return false;
        }

        return true;
    }


    //TODO make this more elegant
    private Boolean sameVertices(GXVertex expectation, GXVertex readVertex) {
        Boolean result = false;
        if(expectation.element().equals(readVertex.element())) {
            if(expectation.getId() == readVertex.getId()) {
                if(expectation.getPosition().getPosition()[0] == readVertex.getPosition().getPosition()[0]) {
                    if(expectation.getPosition().getPosition()[1] == readVertex.getPosition().getPosition()[1]) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private Collection<GXVertex> createExpectedVertices() {
        GXVertex expectationv1 = new GXVertex("A", 0, new GXPosition(6,7));
        GXVertex expectationv2 = new GXVertex("B", 1, new GXPosition(6,5));

        Collection<GXVertex> expectation = new ArrayList<GXVertex>();
        expectation.add(expectationv1);
        expectation.add(expectationv2);
        return expectation;
    }


    private Collection<GXEdge> createExpectedEdges(Collection<GXVertex> expectedVertices) {
        Collection<GXEdge> expectation = new ArrayList<>();
        GXVertex firstVertex = findMatchingVertex("A", expectedVertices);
        GXVertex secondVertex = findMatchingVertex("B", expectedVertices);
        GXEdge expectedEdge = new GXEdge(firstVertex, secondVertex, "0", 3, 0);
        expectation.add(expectedEdge);
        return expectation;
    }
    private GXVertex findMatchingVertex(String vertexName, Collection<GXVertex> vertices){
        //TODO if element is the name and its not unique this will have problems (should we allow vertices with the same name?)
        Iterator<GXVertex> it = vertices.iterator();
        GXVertex matchingVertex = null;
        while(it.hasNext()) {
            GXVertex vertex = (GXVertex) it.next();
            if (vertex.element().equals(vertexName)) {
                matchingVertex = vertex;
            }
        }
        return  matchingVertex;
    }

    @After
    public void tearDown() throws Exception {
        for (File f : TEST_DIR.listFiles()) {
            f.delete();
        }
    }

}