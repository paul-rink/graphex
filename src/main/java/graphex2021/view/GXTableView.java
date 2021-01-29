package graphex2021.view;

import graphex2021.model.GXGraph;
import graphex2021.model.GXVertex;
import graphex2021.model.Observer;
import graphex2021.model.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GXTableView extends TableView<Map<String, String>> implements Observer {
    ObservableList<Map<String, String>> step;
    TableView<Map> table;

    public GXTableView(GXGraph graph) {
         this.table = new TableView<>();
         this.step = FXCollections.<Map<String, String>>observableArrayList();
         create(graph);
    }



    public void create(GXGraph graph) {
        for (GXVertex vertex : graph.vertices()) {
            TableColumn<Map, String> column =  new TableColumn<>(vertex.element());
            column.setCellValueFactory(new MapValueFactory<>(vertex.element()));
            this.table.getColumns().add(column);
        }
    }

    public void addRow(GXGraph graph) {
        Map<String, String> item = new HashMap<>();
        for (GXVertex vertex : graph.vertices()) {
            item.put(vertex.element(), String.valueOf(vertex.getCurrentDistance()));
        }
        step.add(item);

    }

    @Override
    public void doUpdate(Subject s) {
        addRow((GXGraph) s.getState());

    }









}
