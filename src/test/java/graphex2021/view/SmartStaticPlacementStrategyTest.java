package graphex2021.view;

import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import graphex2021.model.GXPosition;
import graphex2021.model.GXVertex;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SmartStaticPlacementStrategyTest {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;
    private static final int MIN_Width = 1000;
    private static final int MIN_HEIGHT = 667;

    private SmartStaticPlacementStrategy strat;

    private ArrayList<SmartGraphVertex<String>> vertexList = new ArrayList<>();
    @Mock
    private SmartGraphVertexNode vertexA;

    @Mock
    private GXPosition position;
    @Mock
    private GXVertex gxVertexA;





    @Before
    public void setUp() {
        Random randomPos = new Random();
        strat = new SmartStaticPlacementStrategy();
        strat.setSizes(WIDTH, HEIGHT, MIN_Width, MIN_HEIGHT);
        //mocking Vertices and the pposition to be returned
        vertexList.add(vertexA);
        Mockito.when(position.getPosition()).thenReturn(
                new int[]{randomPos.nextInt(1001), randomPos.nextInt(1001)});
        Mockito.when(vertexA.getUnderlyingVertex()).thenReturn(Mockito.mock(GXVertex.class));
        gxVertexA = (GXVertex) vertexA.getUnderlyingVertex();
        Mockito.when(gxVertexA.getPosition()).thenReturn(position);

    }


    @After
    public void tearDown() {
        strat = null;
        vertexList = null;
        vertexA = null;
        gxVertexA = null;
        position = null;
    }

    @Test
    public void testPlaceSmallerMinSize() {
        final double width = 700;
        final double height = 200;
        ArgumentCaptor<Double>  doubleXArgumentCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double>  doubleYArgumentCaptor = ArgumentCaptor.forClass(Double.class);
        //Capturing the values put in the set Position by the strategy
        doNothing().when(vertexA).setPosition(doubleXArgumentCaptor.capture(), doubleYArgumentCaptor.capture());

        strat.place(width, height, null, vertexList);

        Assert.assertEquals(calcVertexMinXPos((GXVertex) vertexA.getUnderlyingVertex(), MIN_Width), doubleXArgumentCaptor.getValue(), 0.05);
        Assert.assertEquals(calcVertexMinYPos((GXVertex) vertexA.getUnderlyingVertex(), MIN_HEIGHT), doubleYArgumentCaptor.getValue(), 0.05);
    }

    private double calcVertexMinXPos(GXVertex vertex, double minWidth) {
        return (vertex.getPosition().getPosition()[0] / 1000.) * minWidth;
    }

    private double calcVertexMinYPos(GXVertex vertex, double minHeight) {
        return (vertex.getPosition().getPosition()[1] / 1000.) * minHeight;
    }

}