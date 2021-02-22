package graphex2021.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Collection;


import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class DisplayModelTest {
    private GXGraph testGraph;
    private GXVertex S;
    private GXVertex A;
    private GXVertex B;
    private GXVertex Z;
    private GXEdge SA;
    private GXEdge SB;
    private GXEdge AB;
    private GXEdge BZ;
    private static final File GRAPH_FILE = new File("src/test/resources/GraphData/exampleGraph.json");


    private DisplayModel testModel;

    @Before
    public void setUp() throws WrongFileFormatException {
        /*
        try {
            testGraph = Mockito.spy(new GXGraph(GRAPH_FILE));
        } catch (WrongFileFormatException e) {
            e.printStackTrace();
        }*/
        testGraph = Mockito.spy(new GXGraph());
        addGraph(testGraph);
        testModel = Mockito.spy(new DisplayModel(testGraph));
    }

    private void addGraph(GXGraph graph) {
        GXPosition posS = new GXPosition(0,0);
        GXPosition posA = new GXPosition(1,1);
        GXPosition posB = new GXPosition(2,2);
        GXPosition posZ = new GXPosition(4,3);
        S = Mockito.spy(new GXVertex("S", 0, posS));
        A = Mockito.spy(new GXVertex("A", 1, posA));
        B = Mockito.spy(new GXVertex("B", 2, posB));
        Z = Mockito.spy(new GXVertex("Z", 3, posZ));
        SA = Mockito.spy(new GXEdge(S, A, "2", 2, 0));
        SB = Mockito.spy(new GXEdge(S, B, "8", 8, 1));
        AB = Mockito.spy(new GXEdge(A, B, "2", 2, 2));
        BZ = Mockito.spy(new GXEdge(B, Z, "2", 2, 3));
        graph.insertVertex(S);
        graph.insertVertex(A);
        graph.insertVertex(B);
        graph.insertVertex(Z);
        try {
            graph.insertEdge(SA);
            graph.insertEdge(SB);
            graph.insertEdge(AB);
            graph.insertEdge(BZ);
            graph.setStartingVertex(S);
            graph.setEndingVertex(Z);
        } catch (ElementNotInGraphException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        testGraph = null;
        testModel = null;
    }


    @Test
    public void testDefaultContructor() throws WrongFileFormatException {
        testModel =new DisplayModel();
    }

    @Test
    public void testConstructorNullGraph() throws WrongFileFormatException {
        testGraph = null;
        testModel = new DisplayModel(testGraph);
    }

    @Test
    public void testConstructorWithFile() throws WrongFileFormatException{
        testModel = new DisplayModel(GRAPH_FILE);
    }

    @Test
    public void testNextStepNothingMarked() {
        testModel.nexStep();
        Mockito.verify(SA).setHint(true);
    }

    @Test
    public void testNextStepFirstCorrectlyMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.nexStep();
        assertFalse(SA.isHint());
        Mockito.verify(AB).setHint(true);
    }

    @Test
    public void testNextStepTwoMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(SB);
        testModel.nexStep();
        assertFalse(SA.isHint());
        Mockito.verify(AB).setHint(true);
    }

    @Test
    public void testCheckCorrectNothingSelected() {
        assertTrue(testModel.checkCorrect());
    }

    @Test
    public void testCheckCorrectOneSelected() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        assertTrue(testModel.checkCorrect());
    }

    @Test
    public void testCheckCorrectOneRightOneWrongSelected() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(SB);
        assertFalse(testModel.checkCorrect());
    }

    @Test
    public void testCheckCorrectOneWrongSelected() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SB);
        assertFalse(testModel.checkCorrect());
    }

    @Test
    public void testCheckCorrectCompletePathSelected() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(AB);
        testModel.markEdge(BZ);
        assertTrue(testModel.checkCorrect());
    }

    @Test
    public void checkFoundShortestPathNothingSelectedCorrectNumber() {
        assertFalse(testModel.checkFoundShortestPath(6));
    }

    @Test
    public void checkFoundShortestPathEndvertexMarkedCorrectNumber() {
        Z.mark();
        assertFalse(testModel.checkFoundShortestPath(6));
    }

    @Test
    public void checkFoundShortestPathCorrectPathCorrectNumber() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(AB);
        testModel.markEdge(BZ);
        assertTrue(testModel.checkFoundShortestPath(6));
    }

    @Test
    public void checkFoundShortestPathCorrectPathWrongNumber() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(AB);
        testModel.markEdge(BZ);
        assertFalse(testModel.checkFoundShortestPath(8));
    }

    @Test (expected = EdgeCompletesACircleException.class)
    public void testMarkEdgeEdgeBlocked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        SA.setBlocked(true);
        testModel.markEdge(SA);
    }

    @Test
    public void testMarkEdgeEdgeNoVertexMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(AB);
        Mockito.verify(AB,Mockito.never()).mark();
        assertFalse(AB.isMarked());
    }

    @Test
    public void testMarkEdgeEdgeCorrect() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        Mockito.verify(SA).mark();
        Mockito.verify(A).mark(0, SA);
        Mockito.verify(AB).setVisible(true);
        Mockito.verify(testGraph).blockCircles(A);
    }

    @Test (expected = ElementNotInGraphException.class)
    public void testMarkEdgeNull() throws ElementNotInGraphException, EdgeCompletesACircleException {
        GXEdge edge = null;
        testModel.markEdge(edge);
    }

    @Test
    public void testMarkVertexOld() {
        testModel.markVertex(A);
        Mockito.verify(A).mark();
    }

    @Test
    public void testGetStateNothingMarkedNoHint() {
        GXGraph state = testModel.getState();
        assertTrue(expectedVisibleGraphNothingMarked(state));
    }

    @Test
    public void testGetStateNothingMarkedWithHint() {
        testModel.nexStep();
        GXGraph state = testModel.getState();
        assertTrue(expectedVisibleGraphNothingMarked(state));
    }

    private boolean expectedVisibleGraphNothingMarked(GXGraph state) {
        Collection<GXVertex> visibleVertices = state.vertices();
        Collection<GXEdge> visibleEdges = state.edges();
        boolean vertices = visibleVertices.contains(S) && visibleVertices.contains(A)
                && visibleVertices.contains(B) && visibleVertices.contains(Z);
        boolean edges = visibleEdges.contains(SA) && visibleEdges.contains(SB) && !visibleEdges.contains(AB)
                && !visibleEdges.contains(BZ);
        boolean marked = S.isMarked() && !A.isMarked() && !B.isMarked() && !Z.isMarked()
                && !SA.isMarked() && !SB.isMarked() && !AB.isMarked() && !BZ.isMarked();
        boolean hint = !(S.isHint() | A.isHint() | B.isHint() | Z.isHint() | SA.isHint() | SB.isHint()
                | AB.isHint() | BZ.isHint());
        return vertices && edges && marked && hint;
    }

    @Test
    public void testGetStateSAMarkedNoHint() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        GXGraph state = testModel.getState();
        assertTrue(expectedVisibleGraphSAMarked(state));
    }

    @Test
    public void testGetStateSAMarkedWithHint() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.nexStep();
        testModel.markEdge(SA);
        GXGraph state = testModel.getState();
        assertTrue(expectedVisibleGraphSAMarked(state));
    }

    private boolean expectedVisibleGraphSAMarked(GXGraph state) {
        Collection<GXVertex> visibleVertices = state.vertices();
        Collection<GXEdge> visibleEdges = state.edges();
        boolean vertices = visibleVertices.contains(S) && visibleVertices.contains(A)
                && visibleVertices.contains(B) && visibleVertices.contains(Z);
        boolean edges = visibleEdges.contains(SA) && visibleEdges.contains(SB) && visibleEdges.contains(AB)
                && !visibleEdges.contains(BZ);
        boolean marked = S.isMarked() && A.isMarked() && !B.isMarked() && !Z.isMarked()
                && SA.isMarked() && !SB.isMarked() && !AB.isMarked() && !BZ.isMarked();
        boolean hint = !(S.isHint() | A.isHint() | B.isHint() | Z.isHint() | SA.isHint() | SB.isHint()
                | AB.isHint() | BZ.isHint());
        return vertices && edges && marked && hint;
    }

    @Test
    public void testUndoNothingMarked() throws ElementNotInGraphException {
        testModel.undo();
        assertTrue(expectedGraphInitial());
    }

    @Test
    public void testUndoSAMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.undo();
        assertTrue(expectedGraphInitial());
    }

    private boolean expectedGraphInitial() {
        boolean visible = S.isVisible() && A.isVisible() && B.isVisible() && Z.isVisible()
                && SA.isVisible() && SB.isVisible() && !AB.isVisible() && !BZ.isVisible();
        boolean marked = !(S.isMarked() && A.isMarked() && B.isMarked() && Z.isMarked()
                && SA.isMarked() && SB.isMarked()&& AB.isMarked() && BZ.isMarked());
        boolean hint = !(S.isHint() | A.isHint() | B.isHint() | Z.isHint() | SA.isHint() | SB.isHint()
                | AB.isHint() | BZ.isHint());
        return visible && marked && hint;
    }

    @Test
    public void testUndoSAABMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(AB);
        testModel.undo();
        assertTrue(expectedGraphSAMarked());
    }

    private boolean expectedGraphSAMarked() {
        boolean visible = S.isVisible() && A.isVisible() && B.isVisible() && Z.isVisible()
                && SA.isVisible() && SB.isVisible() && AB.isVisible() && !BZ.isVisible();
        boolean marked = S.isMarked() && A.isMarked() && !(B.isMarked() && Z.isMarked()
                && SA.isMarked() && SB.isMarked()&& AB.isMarked() && BZ.isMarked());
        boolean hint = !(S.isHint() | A.isHint() | B.isHint() | Z.isHint() | SA.isHint() | SB.isHint()
                | AB.isHint() | BZ.isHint());
        boolean distance =  A.getCurrentDistance() == 2;
        return visible && marked && hint && distance;
    }

    @Test
    public void testResetNothingMarked() {
        testModel.reset();
        assertTrue(expectedGraphInitial());
    }

    @Test
    public void testResetEdgesMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.reset();
        assertTrue(expectedGraphInitial());
    }

    @Test
    public void testHighlightNothingMarked() {
        testModel.highlightShortestPathTo(A);
        assertTrue(checkNothingHighligthed());
    }

    private boolean checkNothingHighligthed() {
        return !SA.isHighlighted() && !SB.isHighlighted() && !AB.isHighlighted() && !BZ.isHighlighted();
    }

    @Test
    public void testHighlitedOneHop() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.highlightShortestPathTo(A);
        assertTrue(SA.isHighlighted());
        assertFalse(SB.isHighlighted() || AB.isHighlighted() || BZ.isHighlighted());
    }

    @Test
    public void testHighlitedOnSelectedOtherVertex() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.highlightShortestPathTo(B);
        assertTrue(checkNothingHighligthed());
    }

    @Test
    public void testHighlitedCompletePathMarked() throws ElementNotInGraphException, EdgeCompletesACircleException {
        testModel.markEdge(SA);
        testModel.markEdge(AB);
        testModel.markEdge(BZ);
        testModel.highlightShortestPathTo(Z);
        assertTrue(SA.isHighlighted() && AB.isHighlighted() && BZ.isHighlighted());
        assertFalse(SB.isHighlighted());
    }

    @Test
    public void testGetAllVertices() {
        Collection<GXVertex> vertices = testModel.getAllVertices();
        assertTrue(vertices.contains(S) && vertices.contains(A) &&
                vertices.contains(B) && vertices.contains(Z));
        assertEquals(4, vertices.size());
    }
}
