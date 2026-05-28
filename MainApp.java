import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainApp is the JavaFX entry point of the Air Pollution Viewer.
 *
 * Its sole responsibility is to:
 * Construct all panels and the data layer.
 * Assemble them into the top-level BorderPane layout.
 * Hand control to MainController, which wires the panels together.
 * Configure and show the primary Stage.
 *
 * Now includes a Comparison tab for year-to-year analysis.
 *
 * @author Elif Deniz Turkmen
 * @version 2.0
 */
public class MainApp extends Application
{
    /**
     * JavaFX entry point. Builds all panels, assembles the layout,
     * creates the MainController, and shows the window.
     *
     * @param primaryStage The primary window provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage primaryStage)
    {
        DataManager     dataManager = new DataManager();
        PollutionFilter filter      = new PollutionFilter(dataManager);

        ControlsBar     controlsBar     = new ControlsBar();
        WelcomePanel    welcomePanel    = new WelcomePanel();
        MapPanel        mapPanel        = new MapPanel();
        StatsPanel      statsPanel      = new StatsPanel(filter);
        GridPanel       gridPanel       = new GridPanel();
        ComparisonPanel comparisonPanel = new ComparisonPanel(filter);
        ToolTipOverlay  toolTipOverlay  = new ToolTipOverlay();

        TabPane tabPane = buildTabPane(
                welcomePanel, mapPanel, statsPanel, gridPanel,
                comparisonPanel, toolTipOverlay);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #12122a;");
        root.setTop(controlsBar);
        root.setCenter(tabPane);
        root.setBottom(toolTipOverlay);

        MainController controller = new MainController(
                controlsBar, mapPanel, statsPanel, gridPanel,
                comparisonPanel, toolTipOverlay, filter);

        // Show overlay only on the Map tab (index 1)
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldIdx, newIdx) ->
                    controller.setOverlayVisible(newIdx.intValue() == 1));

        Scene scene = new Scene(root, 1280, 820);
        primaryStage.setTitle("UK Air Pollution Viewer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(620);
        primaryStage.show();

        Platform.runLater(() -> controller.setSelection(
                controlsBar.getSelectedPollutant(),
                controlsBar.getSelectedYear()));
    }

    /**
     * Build the TabPane containing the five application tabs.
     */
    private TabPane buildTabPane(
            WelcomePanel    welcomePanel,
            MapPanel        mapPanel,
            StatsPanel      statsPanel,
            GridPanel       gridPanel,
            ComparisonPanel comparisonPanel,
            ToolTipOverlay  toolTipOverlay)
    {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: #12122a; -fx-tab-min-width: 100;");

        tabPane.getTabs().addAll(
            buildTab("Welcome",    welcomePanel),
            buildTab("Map",        mapPanel),
            buildTab("Statistics", statsPanel),
            buildTab("Grid Data",  gridPanel),
            buildTab("Comparison", comparisonPanel)
        );

        return tabPane;
    }

    /**
     * Create a styled, non-closable Tab with the given title and content.
     */
    private Tab buildTab(String title, javafx.scene.Node content)
    {
        Tab tab = new Tab(title, content);
        tab.setStyle("-fx-background-color: #1a1a2e; -fx-text-fill: #cccccc;");
        return tab;
    }

    /**
     * Application entry point.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
