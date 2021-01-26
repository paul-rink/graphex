package graphex2021.view;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import graphex2021.model.GXGraph;
import graphex2021.model.Observer;
import graphex2021.model.Subject;


public class GraphView extends SmartGraphPanel<String, String> implements Observer {


    public GraphView() {
        super(new GraphAdapter() , new SmartGraphProperties(), new SmartCircularSortedPlacementStrategy());
    }

    @Override
    public void doUpdate(Subject s) {
        GXGraph visible = (GXGraph) s.getState();
        GraphAdapter underlyinigGraph = (GraphAdapter) super.theGraph;
        underlyinigGraph.setGXGraph(visible);

    }
}
