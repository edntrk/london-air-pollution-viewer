import java.util.List;

/**
 * MainController wires all UI panels of the Air Pollution Viewer
 * together and coordinates their responses to user interactions.
 *
 * Responsibilities:
 *   - Listens to ControlsBar for pollutant / year / region / threshold changes.
 *   - Refreshes MapPanel, StatsPanel, GridPanel, and ComparisonPanel.
 *   - Uses GridSelector to translate map clicks into the nearest DataPoint.
 *   - Updates ToolTipOverlay and GridPanel when a DataPoint is selected.
 *   - Manages region switching and threshold filtering.
 *
 * @author Elif Deniz Turkmen
 * @version 2.0
 */
public class MainController
{
    private ControlsBar     controlsBar;
    private MapPanel        mapPanel;
    private StatsPanel      statsPanel;
    private GridPanel       gridPanel;
    private ComparisonPanel comparisonPanel;
    private ToolTipOverlay  toolTipOverlay;

    private PollutionFilter filter;

    private GridSelector gridSelector;

    private String currentPollutant;
    private String currentYear;

    /**
     * Construct a MainController and immediately wire all panels together.
     *
     * @param controlsBar     The top bar providing pollutant / year / region controls.
     * @param mapPanel        The map panel that displays pollution markers.
     * @param statsPanel      The statistics panel showing charts and summaries.
     * @param gridPanel       The grid data panel with the searchable table.
     * @param comparisonPanel The year-to-year comparison panel.
     * @param toolTipOverlay  The info bar shown at the bottom of the map tab.
     * @param filter          The PollutionFilter used to retrieve filtered data.
     */
    public MainController(
            ControlsBar     controlsBar,
            MapPanel        mapPanel,
            StatsPanel      statsPanel,
            GridPanel       gridPanel,
            ComparisonPanel comparisonPanel,
            ToolTipOverlay  toolTipOverlay,
            PollutionFilter filter)
    {
        this.controlsBar     = controlsBar;
        this.mapPanel        = mapPanel;
        this.statsPanel      = statsPanel;
        this.gridPanel       = gridPanel;
        this.comparisonPanel = comparisonPanel;
        this.toolTipOverlay  = toolTipOverlay;
        this.filter          = filter;

        // Read initial selection from the controls bar
        this.currentPollutant = controlsBar.getSelectedPollutant();
        this.currentYear      = controlsBar.getSelectedYear();
        
        // Set the initial region
        String initialRegion = controlsBar.getSelectedRegion();
        filter.setRegion(initialRegion);
        int[] bounds = filter.getRegionBounds(initialRegion);
        mapPanel.setRegionBounds(bounds[0], bounds[1], bounds[2], bounds[3], initialRegion);

        // Set up click-to-nearest-DataPoint on the map
        this.gridSelector = new GridSelector(mapPanel);

        wireControls();
        wireMapClicks();
        refreshAll();
    }

    /**
     * Return the currently selected pollutant.
     *
     * @return The pollutant type string, e.g. "no2".
     */
    public String getCurrentPollutant()
    {
        return currentPollutant;
    }

    /**
     * Return the currently selected year.
     *
     * @return The year string, e.g. "2023".
     */
    public String getCurrentYear()
    {
        return currentYear;
    }

    /**
     * Programmatically change the selection and refresh all panels.
     * Useful for testing or external navigation.
     *
     * @param pollutant The pollutant type to select.
     * @param year      The year to select.
     */
    public void setSelection(String pollutant, String year)
    {
        currentPollutant = pollutant;
        currentYear      = year;
        refreshAll();
    }

    /**
     * Show or hide the ToolTipOverlay.
     * Called by MainApp when the user switches tabs.
     *
     * @param visible true to show the overlay, false to hide it.
     */
    public void setOverlayVisible(boolean visible)
    {
        toolTipOverlay.setVisible(visible);
        toolTipOverlay.setManaged(visible);
    }

    /**
     * Register a SelectionListener on the ControlsBar so that every
     * pollutant / year / region / threshold change triggers a full refresh.
     */
    private void wireControls()
    {
        controlsBar.setListener((pollutant, year) ->
        {
            currentPollutant = pollutant;
            currentYear      = year;
            
            // Update the region in the filter
            String region = controlsBar.getSelectedRegion();
            filter.setRegion(region);
            
            // Update map bounds and region name for the map panel
            int[] bounds = filter.getRegionBounds(region);
            mapPanel.setRegionBounds(bounds[0], bounds[1], bounds[2], bounds[3], region);
            
            toolTipOverlay.reset();
            refreshAll();
        });
    }

    /**
     * Attach a mouse-click handler to the MapPanel.
     *
     * When the user clicks on the map, GridSelector finds the nearest
     * DataPoint. If one is found within the snap radius, the
     * ToolTipOverlay is updated and the corresponding row in GridPanel
     * is highlighted.
     */
    private void wireMapClicks()
    {
        mapPanel.setOnMouseClicked(event ->
        {
            DataPoint dp = gridSelector.findNearest(event.getX(), event.getY());
            if (dp != null)
            {
                toolTipOverlay.show(dp, currentPollutant, currentYear);
                gridPanel.highlight(dp);
            }
        });
    }

    /**
     * Refresh all panels for the current pollutant and year selection.
     */
    private void refreshAll()
    {
        refreshMap();
        refreshStats();
        refreshGrid();
        refreshComparison();
    }

    /**
     * Reload the MapPanel with data filtered to the current region
     * and pollution threshold.
     */
    private void refreshMap()
    {
        double threshold = controlsBar.getThreshold();
        List<DataPoint> points = filter.getFilteredDataWithThreshold(
            currentPollutant, currentYear, threshold);
        mapPanel.refresh(points, currentPollutant, currentYear);
    }

    /**
     * Reload the StatsPanel for the current selection.
     */
    private void refreshStats()
    {
        statsPanel.refresh(currentPollutant, currentYear);
    }

    /**
     * Reload the GridPanel table with filtered data, applying the
     * pollution threshold filter.
     */
    private void refreshGrid()
    {
        double threshold = controlsBar.getThreshold();
        List<DataPoint> points = filter.getFilteredDataWithThreshold(
            currentPollutant, currentYear, threshold);
        gridPanel.refresh(points);
    }
    
    /**
     * Refresh the ComparisonPanel with the current pollutant.
     */
    private void refreshComparison()
    {
        comparisonPanel.refresh(currentPollutant, currentYear);
    }
}
