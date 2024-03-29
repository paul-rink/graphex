package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;
import com.brunomnsilva.smartgraph.graph.InvalidVertexException;
import com.brunomnsilva.smartgraph.graph.Vertex;
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
    @Mock
    private GXGraph mockedGraph;

    private GraphAdapter graphAdapter;

    @BeforeClass
    public static void setUpBeforeClass() {
        vertices = new ArrayList<>(Arrays.asList(vertex1, vertex2, vertex3));

        edges = new ArrayList<>(Arrays.asList(edge1, edge2, edge3));
    }

    @Before
    public void setUp() throws Exception {
        graphAdapter = new GraphAdapter();
        graphAdapter.setGXGraph(mockedGraph);
    }

    @After
    public void tearDown() throws Exception {
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

    @Test
    public void testAreAdjacent() throws ElementNotInGraphException {
        when(mockedGraph.areAdjacent(vertex1, vertex2)).thenReturn(true);
        when(mockedGraph.areAdjacent(vertex1, vertex3)).thenReturn(false);
        assertTrue(graphAdapter.areAdjacent(vertex1, vertex2));
        assertFalse(graphAdapter.areAdjacent(vertex1, vertex3));
    }

    @Test (expected = InvalidVertexException.class)
    public void testAreAdjacentNotInGraph() throws InvalidVertexException, ElementNotInGraphException {
        when(mockedGraph.areAdjacent(vertex1, vertex3)).thenThrow(ElementNotInGraphException.class);
        graphAdapter.areAdjacent(vertex1, vertex3);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testInsertVertex() {
        graphAdapter.insertVertex(null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testInsertEdge() {
        graphAdapter.insertEdge(null, null, null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testInsertEdgeElements() {
        graphAdapter.insertEdge((Object) null, null, null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testRemoveVertex() {
        graphAdapter.removeVertex(null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testRemoveEdge() {
        graphAdapter.removeEdge(null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testReplaceVertex() {
        graphAdapter.replace((Vertex) null, null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testRelaceEdge() {
        graphAdapter.replace((Edge) null, null);
    }
}