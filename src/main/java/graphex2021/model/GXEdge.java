package graphex2021.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

/**
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class GXEdge<E> implements Edge {

    @Override
    public Object element() {
        return null;
    }

    @Override
    public Vertex[] vertices() {
        return new Vertex[0];
    }
}
