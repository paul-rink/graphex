package graphex2021.model;

import java.util.LinkedList;

/**
 * This interface provides methods required by the {@link DisplayModel} to get the displayed {@link GXGraph} ready for
 * the algorithm and to match the algorithm {@link Step}s with user inputs.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public interface Algorithm {
    /**
     * This method is <b>crucial</b> for the program to be executed correctly. By calling this method, the algorithm
     * should return a ordered collection of {@link Step}s that indicate the correct step-by-step execution of the
     * algorithm. You should care for a unique interpretation of the collection.
     *
     * @param g is the {@link GXGraph} the algorithm is applied at.
     * @return a unique ordered collection of {@link Step}s, indicating the algorithm's step-by-step execution.
     */
    LinkedList<Step> getSequence(GXGraph g);

    /**
     * Determines, whether the {@link GXGraph} should be fully revealed at the beginning or not. Choosing
     * {@code true} means, that every {@link GXVertex} and {@link GXEdge} should be visible to the user on
     * initialization. {@code False} means the opposite: every {@link GXVertex} and {@link GXEdge} sould be invisible
     * to the user at beginning. Further exceptions have to be defined by {@link #hasStartingVertex()} or
     * {@link #hasEndingVertex()}.
     *
     * @return {@code true} if the {@link GXGraph} is totally visible at the beginning, {@code false} if not.
     */
    boolean isRevealed();

    /**
     * Choose {@code true} if the algorithm needs a Start-{@link GXVertex}. This will implicate that this
     * {@link GXVertex} and its {@link GXEdge}s will be visible at the beginning.
     * @return {@code true} if the algorithm needs a Start-{@link GXVertex}, {@code false} if not.
     */
    boolean hasStartingVertex();

    /**
     * Choose {@code true} if the algorithm needs an End-{@link GXVertex}. This will implicate that this
     * {@link GXVertex} will be visible at the beginning.
     * @return {@code true} if the algorithm needs an End-{@link GXVertex}, {@code false} if not.
     */
    boolean hasEndingVertex();

    /**
     * Returns whether distance is correct.
     *
     * @param goal is the goal vertex
     * @param distance is the distance to the goal vertex you want to check.
     * @return {@code true} if distance is correct, {@code false} else.
     */
    boolean isCorrectDistance(GXVertex goal, int distance);
}
