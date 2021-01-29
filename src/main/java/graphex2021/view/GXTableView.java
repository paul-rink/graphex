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

public class GXTableView extends TableView<Map<String, String>> implements Observer {
    private ObservableList<Map<String, String>> step;
    private TableView<Map> table;
    private int stepCounter;

    public GXTableView() {
         this.table = new TableView<>();
         this.step = FXCollections.observableArrayList();
         this.stepCounter = 0;
    }



    public void init(Collection<GXVertex> vertices) {
        TableColumn<Map<String, String>, String> stepColumn = new TableColumn<>("Schritt");
        stepColumn.setCellValueFactory(new MapValueFactory("Schritt"));
        this.getColumns().add(stepColumn);
        for (GXVertex vertex : vertices) {
            TableColumn<Map<String, String>, String> column =  new TableColumn<>(vertex.element());
            column.setCellValueFactory(new MapValueFactory(vertex.element()));
            this.getColumns().add(column);
        }
    }

    public void addRow(GXGraph graph) {
        Map<String, String> item = new HashMap<>();
        item.put("Schritt", String.valueOf(stepCounter));
        stepCounter++;
        for (GXVertex vertex : graph.vertices()) {
            int currDist = vertex.getCurrentDistance();
            String entry;
            if (currDist > 0) {
                entry = String.valueOf(currDist);
            } else {
                entry = "-";
            }
            item.put(vertex.element(), entry);
        }
        step.add(item);
        this.getItems().add(item);

    }

    @Override
    public void doUpdate(Subject s) {
        addRow((GXGraph) s.getState());

    }












}
