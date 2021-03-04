package graphex2021.controller;

import graphex2021.model.algo.Dijkstra;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;

/**
 * Dialog that shows information to a specific algorithm.
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 07.02.2021
 */
public class InfoDialog extends Dialog<Void> {

    InfoDialog(AlgorithmName algo) {
        setTitle("Information");
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        setHeight(600);

        switch (algo) {

            case DIJKSTRA:
                Label label = new Label("HINWEIS");
                setHeaderText("Dijkstra-Algorithmus");
                setContentText("Der Algorithmus von Dijkstra (nach seinem Erfinder Edsger W. Dijkstra) ist ein " +
                        "Algorithmus aus der Klasse der Greedy-Algorithmen und löst das Problem der kürzesten Pfade für einen " +
                        "gegebenen Startknoten. Er berechnet somit einen kürzesten Pfad zwischen dem gegebenen Startknoten " +
                        "und einem der (oder allen) übrigen Knoten in einem kantengewichteten Graphen (sofern dieser keine " +
                        "Negativkanten enthält).\n" +
                        "\n" +
                        "Für unzusammenhängende ungerichtete Graphen ist der Abstand zu denjenigen Knoten unendlich, zu " +
                        "denen kein Pfad vom Startknoten aus existiert. Dasselbe gilt auch für gerichtete nicht stark " +
                        "zusammenhängende Graphen. Dabei wird der Abstand synonym auch als Entfernung, Kosten oder Gewicht " +
                        "bezeichnet. \n (Wikipedia) \n \n HINWEISE: \n Wähle bei unentschiedenen Gewichten immer den Knoten " +
                        "bzw. die Kante mit der niedrigeren ID. \n Wenn der Zielknoten nicht erreichbar ist, verwende "
                        + Dijkstra.INFINITY_DIST + " als kürzeste Distanz.");

                break;

            default:
        }
    }

}
