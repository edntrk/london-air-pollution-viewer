import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
/**
 * WelcomePanel is the first panel shown when the application starts.
 * Displays the application title, short description and usage instructions
 * to guide the user.
 *
 * @author Begum Zeytun
 * @version 1.0
 */
public class WelcomePanel extends VBox
{
    /**
     * Construct the WelcomePanel and build all its UI elements.
     */
    public WelcomePanel()
    {
        super(20); // 20px spacing between child elements
        setPadding(new Insets(60, 80, 60, 80));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #1a1a2e;"); 
        
        // add all sections to the panel in order
        getChildren().addAll(
        buildTitle(),
        buildSubtitle(),
        buildDivider(),
        buildDescription(),
        buildDivider(),
        buildInstructionsTitle(),
        buildInstructions()
        );
    }

    /**
     * Build the main title label.
     *
     * @return A styled Label with the application title.
     */
    private Label buildTitle()
    {
        Label title = new Label("London Air Pollution Viewer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setStyle("-fx-text-fill: #e0e0e0;");
        title.setTextAlignment(TextAlignment.CENTER);
        return title;
    }
    
    /**
     * Builds the subtitle label showing the data source and coverage.
     * 
     * @return A styled subtitle Label.
     */
    private Label buildSubtitle()
    {
        Label subtitle = new Label("DEFRA UK Air Pollution Data  |  2018 – 2023  |  NO\u2082 \u00b7 PM10 \u00b7 PM2.5  |  Multiple Regions");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
        subtitle.setStyle("-fx-text-fill: #a0a0c0;");
        subtitle.setTextAlignment(TextAlignment.CENTER);
        return subtitle;
    }
    
    /**
     * Builds a horizontal divider line.
     * 
     * @return A styled Label used as a visual divider.
     */
    private Label buildDivider()
    {
        Label divider = new Label();
        divider.setMaxWidth(Double.MAX_VALUE);
        divider.setMinHeight(1);
        divider.setMaxHeight(1);
        divider.setStyle("-fx-background-color: #3a3a5c;");
        return divider;
    }
    
    /**
     * Build the description paragraph explaining the purpose of the application.
     * 
     * @return A styled description Label.
     */
    private Label buildDescription()
    {
        Label desc = new Label(
            "This application visualises air pollution data for UK cities collected by " +
            "DEFRA (the UK Department for Environment, Food and Rural Affairs).\n\n" +
            "Three pollutants are covered: Nitrogen Dioxide (NO\u2082), and Particulate Matter " +
            "(PM10 and PM2.5). Data is available for every year from 2018 to 2023, mapped " +
            "to 1 \u00d7 1 km grid cells.\n\n" +
            "Regions available: London, Manchester, Birmingham, Leeds, and Bristol."
            );
            desc.setFont(Font.font("Arial", 14));
            desc.setStyle("-fx-text-fill: #cccccc;");
            desc.setWrapText(true);
            desc.setTextAlignment(TextAlignment.LEFT);
            desc.setMaxWidth(700);
            return desc;
    }
    
    /**
     * Builds the "How to use" section title.
     * 
     * @return A styled Label for the instructions heading.
     */
    private Label buildInstructionsTitle()
    {
        Label title = new Label("How to Use");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #9090d0;"); 
        return title;
    }
    
    /**
     * Builds the step-by-step usage instructions.
     * 
     * @return A styled Label containing the instruction text.
     */
    private Label buildInstructions()
    {
        Label instructions = new Label(
            "1.  Map View  --  Select a pollutant, year, and region using the controls at the top.\n" +
            "    Colour-coded markers appear on the map at each measurement location.\n" +
            "    Green = low pollution, Amber = moderate, Red = high.\n\n" +
            "2.  Pollution Filter  --  Use the threshold slider to show only highly polluted\n" +
            "    areas. Set to 0 to show all data points.\n\n" +
            "3.  Hover  over any marker to see a quick tooltip with the grid code,\n" +
            "    coordinates, and pollution value.\n\n" +
            "4.  Click  a marker to see its full details in the info bar below the map\n" +
            "    and to highlight the corresponding row in the Grid Data panel.\n\n" +
            "5.  Statistics Panel  --  View average, maximum, minimum, median, and\n" +
            "    standard deviation. Choose from 7 chart types including bar charts.\n\n" +
            "6.  Grid Data Panel  --  Browse or search individual grid-cell records.\n\n" +
            "7.  Comparison Panel  --  Pick two years and compare pollution statistics\n" +
            "    side by side with percentage change indicators and a grouped bar chart."
            );
            instructions.setFont(Font.font("Courier New", 13));
            instructions.setStyle("-fx-text-fill: #b0b0b0;");
            instructions.setWrapText(true);
            instructions.setMaxWidth(700);
            return instructions;
    }
}