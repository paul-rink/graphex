package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;

import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import graphex2021.model.GXVertex;


import java.util.Collection;


public class SmartStaticPlacementStrategy implements SmartPlacementStrategy {

    private double startWidth;
    private double startHeight;
    private double minHeight;
    private double minWidth;
    private double correction;

    public SmartStaticPlacementStrategy() {

    }

    @Override
    public <V, E> void place(double width, double height, Graph<V, E> theGraph, Collection<?
            extends SmartGraphVertex<V>> vertices) {
        double startingRatio = this.startWidth / (this.startHeight);
        double currentRatio = width / height;

        /*
        If the factor is 1 the ratio of the sides is still kept correct.
        If it is less than 1 the window it means the window is wider in relation relation to its height than before
        ==> the height needs to be stretched by correction factor
        (E.g. starts out at 1000:1000 == Stretched to 2000:1000 ==> currentRatio = 2,
        startingscale = 1 ==> correction factor = 0.5
        Analog for greater than 1
         */

        double correctionFactor = currentRatio / startingRatio;
        this.correction = correctionFactor;

        if (width <= minWidth && height <= minHeight) {
            for (SmartGraphVertex<V> vertex : vertices) {

                GXVertex vert = (GXVertex) vertex.getUnderlyingVertex();
                double x = calcFromRelative(minWidth, vert.getPosition().getPosition()[0]);
                double y = calcFromRelative(minHeight, vert.getPosition().getPosition()[1]);
                vertex.setPosition(x, y);
            }

        } else {
            if (correctionFactor >= 1) {
                //Relatively wider than high
                for (SmartGraphVertex<V> vertex : vertices) {

                    GXVertex vert = (GXVertex) vertex.getUnderlyingVertex();
                    double x = calcFromRelative(width, vert.getPosition().getPosition()[0]);
                    double y = calcFromRelative(height, vert.getPosition().getPosition()[1]) * correctionFactor;

                    vertex.setPosition(x, y);
                }

            } else if (correctionFactor < 1) {
                for (SmartGraphVertex<V> vertex : vertices) {

                    GXVertex vert = (GXVertex) vertex.getUnderlyingVertex();

                    // if the height has relatively grown more than the width the correctionFactor is less than 1
                    // ==> the x coordinates need to be stretched. The factor would be
                    // (startingHeight / startingWidth) / ( currentHeight/ currentWidth) == (1 / correctionFactor)
                    double x = calcFromRelative(width, vert.getPosition().getPosition()[0]) * (1 / correctionFactor);
                    double y = calcFromRelative(height, vert.getPosition().getPosition()[1]);

                    vertex.setPosition(x, y);
                }
            }
        }
    }

    public void setSizes(double width, double height, double minWidth, double minHeight) {
        this.startWidth = width;
        this.startHeight = height;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    private double calcFromRelative(double size, int x) {
        return (x / 1000.) * size;
    }

    public double getCorrection() {
        return correction;
    }

}
