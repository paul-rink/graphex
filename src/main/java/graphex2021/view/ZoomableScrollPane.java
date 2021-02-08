package graphex2021.view;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue = 1;
    private final double zoomIntensity = 0.002;
    private Node target;
    private Node zoomNode;

    public ZoomableScrollPane() {
        super();
    }

    public void init(Node target, Group group, GraphView view) {
        //Pane graphView = (Pane) pane.getChildren().get(0);
        this.target = view;
        this.zoomNode = group;
        setScrollAction(view);
        //pane.getChildren().add(zoomNode);
        setContent(target);

        setPannable(true);
        setFitToHeight(true); //center
        setFitToWidth(true); //center
        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setHbarPolicy(ScrollBarPolicy.ALWAYS);

        //target.setOnMouseEntered(e -> System.out.println("Vbox"));
        //group.setOnMouseEntered(e -> System.out.println("group"));
        //view.setOnMouseEntered(e -> System.out.println("view"));

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
        double sceneHeight = this.getScene().getHeight();
        System.out.println("Scene Height: "+ sceneHeight);
        double sceneWidth = this.getScene().getWidth();
        System.out.println("Scene Width: "+ sceneWidth);
        double currentHeight = tar.heightProperty().doubleValue();
        System.out.println("current Height: "+ currentHeight);
        double currentWidth = tar.widthProperty().doubleValue();
        System.out.println("current Width: " + currentWidth);
        double newHeight = currentHeight*newScaleValue;
        System.out.println("new Height: " + newHeight);
        double newWidth = currentWidth*newScaleValue;
        System.out.println("new Width: "+ newWidth);
        double minHeight =tar.getMinHeight();
        double minWidth = tar.getMinWidth();
        if (newHeight < sceneHeight && minHeight < sceneHeight) {
            System.out.println("case 1");
            double factor = currentHeight/sceneHeight;
            target.setScaleX(factor);
            target.setScaleY(factor);
            scaleValue = 1;
        } else if(newWidth < sceneWidth && minWidth < sceneWidth) {
            System.out.println("case 2");
            double factor = currentWidth / sceneWidth;
            target.setScaleX(factor);
            target.setScaleY(factor);
            scaleValue = 1;
        } else {
            if(newWidth > minWidth && newHeight > minHeight) {
                System.out.println("case 3");
                target.setScaleX(newScaleValue);
                target.setScaleY(newScaleValue);
                scaleValue = newScaleValue;
            }

        }
    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        // calculate pixel offsets from [0, 1] range
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double newscaleValue = scaleValue * zoomFactor;
        updateScale(newscaleValue);
        this.layout(); // refresh ScrollPane scroll positions & target bounds

        // convert target coordinates to zoomTarget coordinates
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        // calculate adjustment of scroll position (pixels)
        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}