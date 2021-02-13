package graphex2021.model;


import org.junit.*;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.ExpectedException;

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

import static org.junit.Assert.*;


public class GraphParserTest {

    private static final File originalFile = new File("src/test/resources/GraphData/testGraph.json");
    private static final File TEST_DIR = new File("src/test/resources/GraphData/test");
    private static final File copiedFile = new File("src/test/resources/GraphData/test/testFile.json");
    private static final File GRAPH_MISSING_ATTRIBUTE = new File("src/test/resources/GraphData/wrongGXFormat.json");
    private static final  File NOT_JSON = new File("src/test/resources/GraphData/noJson.json");
    private static final File copy_GRAPH_MISSING_ATTRIBUTE = new File("src/test/resources/GraphData/test/wrongGXFormat.json");
    private static final  File copy_NOT_JSON = new File("src/test/resources/GraphData/test/noJson.json");

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


    }
    @Before
    public void setUp() throws Exception {
        this.parser = GraphParser.getGraphParser();
        Path copiedCorrect = copiedFile.toPath();
        Path originalPathCorrect = originalFile.toPath();
        Path originalmissingAttribute = GRAPH_MISSING_ATTRIBUTE.toPath();
        Path copiedMissingAttribute = copy_GRAPH_MISSING_ATTRIBUTE.toPath();
        Path originalWrongJson = NOT_JSON.toPath();
        Path copiedWrongJson = copy_NOT_JSON.toPath();
        Files.copy(originalPathCorrect, copiedCorrect, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(originalmissingAttribute,copiedMissingAttribute, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(originalWrongJson, copiedWrongJson, StandardCopyOption.REPLACE_EXISTING);
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
            fail();
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


    @Test
    public void testParseEdges() {
        Collection<GXVertex> expectedVertices = createExpectedVertices();
        Collection<GXEdge> expectedEdges = createExpectedEdges(expectedVertices);

        Collection<GXEdge> readEdges = null;
        try {
            readEdges = parser.parseEdges(copiedFile, expectedVertices);
        } catch (WrongFileFormatException e) {
            fail();
        }
        assertEquals(expectedEdges.size(), readEdges.size());
        Iterator<GXEdge> it = readEdges.iterator();
        Iterator<GXEdge> ite = expectedEdges.iterator();
        while (it.hasNext() && ite.hasNext()) {
            assertTrue(sameEdge(ite.next(), it.next()));
        }
    }

    @Test
    public void testParseStartingVertex() {
        Collection expectedVertices = createExpectedVertices();
        GXVertex expectedStartingVertex = getExpectedStart(expectedVertices);
        GXVertex readStartingVertex = null;
        try {
            readStartingVertex = parser.parseStarting(copiedFile, expectedVertices);
        } catch (WrongFileFormatException e) {
            fail();
        }
        assertTrue(sameVertices(expectedStartingVertex, readStartingVertex));
    }

    @Test
    public void testParseEndingVertex() {
        Collection expectedVertices = createExpectedVertices();
        GXVertex expectedEndingVertex = getExpectedEnd(expectedVertices);
        GXVertex readEndingVertex = null;
        try {
            readEndingVertex = parser.parseEnding(copiedFile, expectedVertices);
        } catch (WrongFileFormatException e) {
            fail();
        }
        assertTrue(sameVertices(expectedEndingVertex, readEndingVertex));
    }


    @Test
    public void testFileNullException() {
        String expected = "File ist null.";
        try {
            parser.parseVertices(null);
        } catch (WrongFileFormatException e) {
            String message = e.getMessage();
            if(!expected.equals(message)) {
                fail();
            }
        }
    }

    @Test
    public void testNotGXGraph() {
        String expected = "required key";
        try{
            parser.parseVertices(copy_GRAPH_MISSING_ATTRIBUTE);
            } catch (WrongFileFormatException e) {
            if (!e.getMessage().contains(expected)) {
                fail();
            }
        }
    }

    @Test
    public void testNotAJson() {
        String expected = "Expected";
        try{
            parser.parseVertices(copy_NOT_JSON);
        } catch (WrongFileFormatException e) {
            if(!e.getMessage().contains(expected)) {
                fail();
            }
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

    private GXVertex getExpectedStart(Collection<GXVertex> vertices){
        return findMatchingVertex("A", vertices);
    }

    private GXVertex getExpectedEnd(Collection<GXVertex> vertices) {
        return findMatchingVertex("B", vertices);
    }

    private Collection<GXEdge> createExpectedEdges(Collection<GXVertex> expectedVertices) {
        Collection<GXEdge> expectation = new ArrayList<>();
        GXVertex firstVertex = findMatchingVertex("A", expectedVertices);
        GXVertex secondVertex = findMatchingVertex("B", expectedVertices);
        GXEdge expectedEdge = new GXEdge(firstVertex, secondVertex, "3", 3, 0);
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