package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import graphex2021.model.Observer;
import graphex2021.model.Subject;


public class GraphView extends SmartGraphPanel<String, String> implements Observer {

    private GraphAdapter graph;

    public GraphView() {
        super(new GraphAdapter());
    }

    @Override
    public void doUpdate(Subject s) {
        s.getState();
    }
}
