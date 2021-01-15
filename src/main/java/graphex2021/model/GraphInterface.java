package graphex2021.model;


import com.brunomnsilva.smartgraph.graph.Graph;

/**
 *
 * TODO why boolean return values? Generally check Javadocs
 *
 * @param <V> the element stored in the vertices
 * @param <E> the element stored in the edges
 * @author Dominik
 * @version 1.0
 */
public interface GraphInterface<V, E> extends Graph<V, E> {

    /**
     *
     *
     * @param vertex
     * @return true if everything worked
     */
    boolean setEdgeVisible(GXVertex vertex);

    boolean setVertexVisible(GXEdge edge);

    boolean updateDistance();

    void mark(GXEdge edge, GXVertex vertex);

    GXEdge blockCircles(GXVertex vertex);

    GXVertex getStartingVertex();

    GXVertex getEndingVertex();

    void unmarkVertex(GXVertex vertex);

    void setVertexInvisible(GXVertex vertex, GXEdge edge);


}
