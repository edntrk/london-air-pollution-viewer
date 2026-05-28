import javafx.scene.paint.Color;
/**
 * ColourMapper maps a pollution value to a JavaFX Color on a
 * green-amber-red scale, using WHO air quality guideline thresholds.
 *
 * Thresholds by pollutant (µg/m³):
 *
 *   NO2:   green  <  25   |  amber 25-40  |  red > 40
 *   PM10:  green  <  20   |  amber 20-35  |  red > 35
 *   PM2.5: green  <  10   |  amber 10-20  |  red > 20
 *
 * Within each band the colour is smoothly interpolated so that
 * transitions are gradual rather than hard steps.
 *
 * If the pollutant string is not recognised, generic low/mid/high
 * thresholds of 10 / 25 / 50 are used as a fallback.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class ColourMapper
{
    // Reusable colour constants
    private static final Color GREEN  = Color.rgb( 56, 168,  0);
    private static final Color AMBER  = Color.rgb(255, 165,  0);
    private static final Color RED    = Color.rgb(200,   0,  0);
 
    /**
     * Return the JavaFX Color for a given pollution value and pollutant type.
     * The colour is smoothly interpolated within the green→amber and amber→red bands.
     *
     * @param value     The pollution measurement (µg/m³)
     * @param pollutant The pollutant type: "no2", "pm10", or "pm2.5" (case-insensitive)
     * @return A JavaFX Color on the green-amber-red scale
     */
    public Color getColour(double value, String pollutant)
    {
        if (value < 0) {
            return GREEN;
        }

        double[] thresholds = getThresholds(pollutant);
        double low  = thresholds[0];
        double high = thresholds[1];

        if (value <= low) {
            return GREEN;
        } else if (value <= high) {
            double t = (value - low) / (high - low);
            return interpolate(GREEN, AMBER, t);
        } else {
            double t = Math.min(1.0, (value - high) / high);
            return interpolate(AMBER, RED, t);
        }
    }   
 
    /**
     * Return the lower and upper band thresholds for the given pollutant.
     * Below the lower threshold the colour is green; above the upper it is red.
     *
     * @param pollutant The pollutant type (case-insensitive)
     * @return A double array { lowerThreshold, upperThreshold }
     */
    private double[] getThresholds(String pollutant)
    {
        if (pollutant == null) {
            return new double[]{ 10.0, 25.0 };
        } else if (pollutant.toLowerCase().equals("no2")) {
            return new double[]{ 25.0, 40.0 };
        } else if (pollutant.toLowerCase().equals("pm10")) {
            return new double[]{ 20.0, 35.0 };
        } else if (pollutant.toLowerCase().equals("pm2.5")) {
            return new double[]{ 10.0, 20.0 };
        } else {
            return new double[]{ 10.0, 25.0 };
        }
    }
 
    /**
     * Linearly interpolate between two JavaFX Colors.
     *
     * @param from Start colour (t = 0)
     * @param to   End colour   (t = 1)
     * @param t    Interpolation factor in [0, 1]
     * @return The interpolated Color
     */
    private Color interpolate(Color from, Color to, double t)
    {
        double r = from.getRed()   + t * (to.getRed()   - from.getRed());
        double g = from.getGreen() + t * (to.getGreen() - from.getGreen());
        double b = from.getBlue()  + t * (to.getBlue()  - from.getBlue());
        return new Color(clamp(r), clamp(g), clamp(b), 1.0);
    }
 
    /**
     * Clamp a double value to the [0, 1] range required by JavaFX Color.
     *
     * @param v The value to clamp
     * @return The clamped value
     */
    private double clamp(double v)
    {
        return Math.max(0.0, Math.min(1.0, v));
    }
}