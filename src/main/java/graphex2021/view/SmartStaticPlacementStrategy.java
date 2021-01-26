package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import graphex2021.model.GXVertex;

import java.util.Collection;

public class SmartStaticPlacementStrategy implements SmartPlacementStrategy {

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Collection<?
            extends SmartGraphVertex<V>> vertices) {

        for (SmartGraphVertex<V> vertex : vertices) {

            GXVertex vert = (GXVertex) vertex.getUnderlyingVertex();
            double x = vert.getPosition().getPosition()[0];
            double y = vert.getPosition().getPosition()[1];

            vertex.setPosition(x, y);
        }


    }
}
