package graphex2021.view;

import graphex2021.model.GXGraph;
import graphex2021.model.GXVertex;
import graphex2021.model.Observer;
import graphex2021.model.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to generate a table displaying the distances from the starting vertex to each already discovered vertex.
 * These distances are calculated each step after marking an edge.
 * If a step is undone the corresponding row will be removed from the table.
 *
 *
 * @author Dominik
 * @version 1.0 29.01.2021
 */
public class GXTableView extends TableView<Map<String, String>> implements Observer {

    /**
     * Each step will be saved in Map, where the key is the vertex element and the entry is the current distance
     * from the starting vertex. These maps are corresponding to the steps.
     */
    private ObservableList<Map<String, String>> steps;
    private int stepCounter;
    private int prevMarked;


    /**
     * Creates a new {@link GXTableView} with the step counter 0 and empty list of rows.
     * The init method needs to be called before anything is actually added to the table.
     *
     */
    public GXTableView() {
        this.steps = FXCollections.observableArrayList();
        this.stepCounter = 0;
        this.prevMarked = 0;

    }


    /**
     * Will initialize the columns the table should have. First column will always be step count.
     * Then for each vertex passed to it a new column will be created named after the vertex element.
     *
     * @param vertices that should be columns of the table.
     */
    public void init(Collection<GXVertex> vertices) {
        TableColumn<Map<String, String>, String> stepColumn = new TableColumn<>("Schritt");
        stepColumn.setCellValueFactory(new MapValueFactory("Schritt"));
        this.getColumns().add(stepColumn);
        for (GXVertex vertex : vertices) {
            TableColumn<Map<String, String>, String> column =  new TableColumn<>(vertex.element());
            // The value of each cell in a column will be taken from a map. If the column name ("element") exists in the
            // map the corresponding value will be pulled and inserted into the cell.
            column.setCellValueFactory(new MapValueFactory(vertex.element()));
            column.setSortable(false);
            this.getColumns().add(column);
        }
        // The content of the table is now set to
        this.setItems(steps);
    }


    /**
     * Adds a row to the table. Will get the current distance from all the vertices in the passed {@link GXGraph}.
     * If a vertex in the table is not reachable the distance will be marked by a '-'
     *
     * @param graph that the row should be build from
     */
    private void addRow(GXGraph graph) {
        // The map that will represent the state for that row
        Map<String, String> row = new HashMap<>();
        row.put("Schritt", String.valueOf(stepCounter));
        stepCounter++;
        for (GXVertex vertex : graph.vertices()) {
            int currDist = vertex.getCurrentDistance();
            String entry;

            if (currDist > 0) {
                // If the distance is greater than zero the vertex is reachable
                // and the distance will be added to the row
                entry = String.valueOf(currDist);
            } else {
                // If the vertex is not reachable but in the current visible graph a '-' will be put in that place
                entry = "-";
            }
            row.put(vertex.element(), entry);
        }
        steps.add(row);
    }

    @Override
    public void doUpdate(Subject s) {
        final GXGraph graph = (GXGraph) s.getState();
        final int markedVertices = markedVertices(graph);
        if (markedVertices > prevMarked) {
            addRow(graph);
            this.prevMarked = markedVertices;
        } else if (markedVertices == 1) {
            reset();
        }
    }


    private int markedVertices(GXGraph graph) {
        int marked = 0;
        for (GXVertex vertex : graph.vertices()) {
            if (vertex.isMarked()) {
                marked++;
            }
        }
        return marked;
    }

    private void reset() {
        steps.remove(1, steps.size());
        stepCounter = 1;
        this.prevMarked = 1;
    }











}
