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
            double x = calcXFromRelative(width, vert.getPosition().getPosition()[0]);
            double y = calcYFromRelative(height, vert.getPosition().getPosition()[1]);

            vertex.setPosition(x, y);
        }
    }

    private double calcXFromRelative(double width, int x) {
        return (x / 1000.) * width;
    }

    private double calcYFromRelative(double height, int y) {
        return (y / 1000.) * height;
    }

}
