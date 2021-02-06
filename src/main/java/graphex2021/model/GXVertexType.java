package graphex2021.model;

/**
 * Attribute that contains whether a vertex is the start or the finish vertex. If it is neither can be null.
 */
public enum GXVertexType {

    /**
     * If vertex is starting Vertex
     */
    STARTING("startingVertex"),
    /**
     * If vertex is ending Vertex
     */
    ENDING("endingVertex"),
    /**
     * If vert is normal Vertex
     */
    NORMAL("vertex");

    private String type;

    private GXVertexType(String type) {
        this.type = type;
    }

    public String toString() {
        return this.type;
    }

    public void setType(String s) {
        this.type = s;
    }
}
