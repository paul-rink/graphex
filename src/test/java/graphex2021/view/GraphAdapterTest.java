package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;
import com.brunomnsilva.smartgraph.graph.InvalidVertexException;
import graphex2021.model.ElementNotInGraphException;
import graphex2021.model.GXEdge;
import graphex2021.model.GXGraph;
import graphex2021.model.GXVertex;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith (MockitoJUnitRunner.class)
public class GraphAdapterTest {

    @Mock
    private GXGraph mockedGraph;

    private static ArrayList<GXVertex> vertices;
    private static ArrayList<GXEdge> edges;

    @Mock
    private static GXEdge edge1;
    @Mock
    private static GXEdge edge2;
    @Mock
    private  static GXEdge edge3;

    @Mock
    private static GXVertex vertex1;
    @Mock
    private static GXVertex vertex2;
    @Mock
    private static GXVertex vertex3;

    private static final File GRAPH_FILE = new File("src/test/resources/GraphData/exampleGraph.json");
    private GXGraph example;
    private GraphAdapter graphAdapter;

    @BeforeClass
    public static void setUpBeforeClass() {
        vertices = new ArrayList<>(Arrays.asList(vertex1, vertex2, vertex3));

        edges = new ArrayList<>(Arrays.asList(edge1, edge2, edge3));
    }

    @Before
    public void setUp() throws Exception {
        example = new GXGraph(GRAPH_FILE);
        graphAdapter = new GraphAdapter();
        graphAdapter.setGXGraph(mockedGraph);




    }

    @After
    public void tearDown() throws Exception {
        this.example = null;
        graphAdapter = null;
    }

    @Test
    public void testNumVertices() {
        final int bound = 1000;
        int numVertices = new Random().nextInt(bound);
        when(mockedGraph.numVertices()).thenReturn(numVertices);
        assertEquals(numVertices, graphAdapter.numVertices());
    }


    @Test
    public void testNumEdges() {
        final int bound = 1000;
        int numEdges = new Random().nextInt(bound);
        when(mockedGraph.numVertices()).thenReturn(numEdges);
        assertEquals(numEdges, graphAdapter.numVertices());
    }

    @Test
    public void testVertices() {
        when(mockedGraph.vertices()).thenReturn(vertices);
        assertArrayEquals(vertices.toArray(), graphAdapter.vertices().toArray());
    }

    @Test
    public void testEdges() {
        when(mockedGraph.edges()).thenReturn(edges);
        assertArrayEquals(edges.toArray(), graphAdapter.edges().toArray());
    }

    @Test
    public void testIncidentEdges() throws ElementNotInGraphException {
        when(mockedGraph.incidentEdges(vertex1)).thenReturn(edges);
        assertArrayEquals(edges.toArray(), graphAdapter.incidentEdges(vertex1).toArray());
    }

    @Test (expected = InvalidVertexException.class)
    public void testIncidentEdgesException() throws ElementNotInGraphException, InvalidVertexException {
        when(mockedGraph.incidentEdges(vertex1)).thenThrow(ElementNotInGraphException.class);
        graphAdapter.incidentEdges(vertex1);
    }

    @Test
    public void testOppositeVertex() throws ElementNotInGraphException {
        when(mockedGraph.opposite(vertex1, edge1)).thenReturn(vertex2);
        assertEquals(vertex2, mockedGraph.opposite(vertex1, edge1));
    }

    @Test (expected = InvalidEdgeException.class)
    public void testOppositeVertexWrongEdge() throws ElementNotInGraphException, InvalidEdgeException {
        when(mockedGraph.opposite(vertex1, edge2)).thenThrow(ElementNotInGraphException.class);
        when(mockedGraph.vertices()).thenReturn(new ArrayList<>(Arrays.asList(vertex1, vertex2)));
        graphAdapter.opposite(vertex1, edge2);
    }

    @Test (expected = InvalidVertexException.class)
    public void testOppositeVertexNotInGraph() throws InvalidVertexException, ElementNotInGraphException {
        when(mockedGraph.opposite(vertex3, edge1)).thenThrow(ElementNotInGraphException.class);
        when(mockedGraph.vertices()).thenReturn(new ArrayList<>(Arrays.asList(vertex1, vertex2)));
        graphAdapter.opposite(vertex3, edge1);
    }
}