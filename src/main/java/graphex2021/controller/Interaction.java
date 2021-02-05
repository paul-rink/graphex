package graphex2021.controller;

public enum Interaction {
    EDGE_SELECTION("Kante markieren", "Doppelklick auf Kante"),
    VERTEX_DISTANCE_SHOW("Distanz anzeigen", "Hovern über einen markierten Knoten"),
    POTENTIAL_DISTANCE_SHOW("Potentielle Distanz anzeigen", "Hovern über unmarkierte Kante"),
    PATH_TO_VERTEX_SHOW("Pfad zu einem Knoten einblenden", "Linksklick auf einen Knoten"),
    VERTEX_ID_SHOW("Knoten-ID anzeigen", "Hovern über unmarkierten Knoten");

    private String action;
    private String text;

    /**
     * Creates a new interaction category.
     * @param action is the action that can be performed
     * @param text is how the action can be performed
     */
    Interaction(String action, String text) {
        this.action = action;
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public String getText() {
        return text;
    }
}
