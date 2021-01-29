package graphex2021.view;

import graphex2021.model.*;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Iterator;

public class GXTable implements Observer {
    private Stage tableStage;
    private Scene tableScene;
    private TableView gxTable;


    public GXTable() {
        tableStage = new Stage();
        tableStage.setTitle("Tabelle");
        gxTable = new TableView();
        VBox tableBox = new VBox(gxTable);
        tableScene = new Scene(tableBox);
        tableStage.setScene(tableScene);
        tableStage.hide();
    }

    @Override
    public void doUpdate(Subject s) {
        GXGraph visibleGraph = (GXGraph) s.getState();
        for (GXVertex v : visibleGraph.vertices()) {
            Iterator it = gxTable.getColumns().iterator();
            boolean inTable = false;
            while (it.hasNext()) {
                TableColumn col = (TableColumn) it.next();
                if (col.getText().equals(v.element())) {
                    inTable = true;
                }
            }
            if(!inTable) {
                TableColumn<GXVertex,Integer> vColumn = new TableColumn<>(v.element());
                gxTable.getColumns().add(vColumn);
            }
        }
    }


    public void initTable() {
        TableColumn<String, String> column1 = new TableColumn<>("Schritt");
        gxTable.getColumns().add(column1);

    }

    public void showTable() {
        tableStage.show();
    }
}

