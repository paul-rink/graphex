package graphex2021.model;

/**
 * This class is for saving each step of an algorithm or a modification on a {@link GXGraph}. Therefore it holds
 * references to a {@link GXVertex} and/or {@link GXEdge} that was selected/modified at a specific time.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public class Step {
    private final GXVertex selectedVertex;
    private final GXEdge selectedEdge;

    /**
     * Constructor for a new {@link Step}. Saves a selected/modified {@link GXEdge} and/or {@link GXVertex} of a
     * specific step. You can set one {@code null} if only a edge or a vertex has to be stored.
     *
     * @param selectedVertex is the selected/modified {@link GXVertex}
     * @param selectedEdge is the selected/modified {@link GXEdge}
     */
    Step(GXVertex selectedVertex, GXEdge selectedEdge) {
        this.selectedVertex = selectedVertex;
        this.selectedEdge = selectedEdge;
    }

    /**
     * Returns the {@link GXVertex} that was selected or modified in this step.
     * @return selected {@link GXVertex} or {@code null} if no vertex was selected in this step.
     */
    public GXVertex getSelectedVertex() {
        return selectedVertex;
    }

    /**
     * Returns the {@link GXEdge} that was selected or modified in this step.
     * @return selected {@link GXEdge} or {@code null} if no edge was selected in this step.
     */
    public GXEdge getSelectedEdge() {
        return selectedEdge;
    }

}
