package graphex2021.view;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;


public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue = 1;
    private final double zoomIntensity = 0.0008;
    private Node target;
    private Node zoomNode;

    public ZoomableScrollPane() {
        super();
    }

    public void init(Node target, Group group, GraphView view) {
        this.target = view;
        this.zoomNode = group;
        setScrollAction(view);
        setContent(target);

        setPannable(true);
        setFitToHeight(true); //center
        setFitToWidth(true); //center
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

        updateScale(scaleValue);
    }

    private void setScrollAction(Node outerNode) {
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });
    }

    private void updateScale(double newScaleValue) {

        Pane tar = (Pane) target;
        //the height of the scene the scrollpane is displayed in
        double sceneHeight = this.getScene().getHeight();
        double sceneWidth = this.getScene().getWidth();
        
        //the minimal size set for the graph
        double minHeight = tar.getMinHeight();
        double minWidth = tar.getMinWidth();
        target.setScaleX(newScaleValue);
        target.setScaleY(newScaleValue);


        if (tar.getScene().getWidth() >= tar.getBoundsInParent().getWidth()
                || tar.getScene().getHeight() - 40 - 28 >= tar.getBoundsInParent().getHeight()
                ) {
            target.setScaleX(scaleValue);
            target.setScaleY(scaleValue);
            return;
        }
        scaleValue = newScaleValue;

    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {

        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double newscaleValue = target.getScaleX() * zoomFactor;
        updateScale(newscaleValue);
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        // convert target coordinates to zoomTarget coordinates
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = target.getLocalToParentTransform()
                .deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        if (!(Math.abs(updatedInnerBounds.getWidth() - viewportBounds.getWidth()) < 0.1) && !(Math.abs(updatedInnerBounds.getHeight() - viewportBounds.getHeight()) < 0.1)) {
            this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
            this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        }
    }
}