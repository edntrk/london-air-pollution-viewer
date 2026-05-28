import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Unit tests for the StatisticsCalc class.
 * 
 * Tests cover all public methods including:
 * average, max, min, maxDataPoint, minDataPoint, countValid,
 * standardDeviation, median, yearlyAverageTrend, yearlyMaxTrend,
 * yearlyMinTrend.
 * 
 * Each method is tested with normal data, empty lists, lists with
 * only invalid values, single element lists, and null inputs.
 * 
 * @author Emre Gokdemir
 * @version 1.0
 */
public class StatisticsCalcTest
{
    private List<DataPoint> normalList;
    private List<DataPoint> singleItemList;
    private List<DataPoint> emptyList;
    private List<DataPoint> allInvalidList;
    private List<DataPoint> mixedList;
    
    /**
     * Set up test data before each test.
     * Creates several lists of DataPoints with different characteristics.
     */
    @BeforeEach
    public void setUp()
    {
        normalList = new ArrayList<>();
        normalList.add(new DataPoint(1, 100, 200, 10.0));
        normalList.add(new DataPoint(2, 101, 201, 20.0));
        normalList.add(new DataPoint(3, 102, 202, 30.0));
        normalList.add(new DataPoint(4, 103, 203, 40.0));
        normalList.add(new DataPoint(5, 104, 204, 50.0));
        
        singleItemList = new ArrayList<>();
        singleItemList.add(new DataPoint(1, 100, 200, 25.0));
        
        emptyList = new ArrayList<>();
        
        allInvalidList = new ArrayList<>();
        allInvalidList.add(new DataPoint(1, 100, 200, -1.0));
        allInvalidList.add(new DataPoint(2, 101, 201, -1.0));
        allInvalidList.add(new DataPoint(3, 102, 202, -1.0));
        
        mixedList = new ArrayList<>();
        mixedList.add(new DataPoint(1, 100, 200, 10.0));
        mixedList.add(new DataPoint(2, 101, 201, -1.0));   // invalid
        mixedList.add(new DataPoint(3, 102, 202, 30.0));
        mixedList.add(new DataPoint(4, 103, 203, -1.0));   // invalid
        mixedList.add(new DataPoint(5, 104, 204, 50.0));
    }
    
    @Test
    public void testAverageNormalList()
    {
        assertEquals(30.0, StatisticsCalc.average(normalList), 0.001);
    }
    
    @Test
    public void testAverageSingleItem()
    {
        assertEquals(25.0, StatisticsCalc.average(singleItemList), 0.001);
    }
    
    @Test
    public void testAverageEmptyList()
    {
        assertEquals(-1.0, StatisticsCalc.average(emptyList), 0.001);
    }
    
    @Test
    public void testAverageAllInvalid()
    {
        assertEquals(-1.0, StatisticsCalc.average(allInvalidList), 0.001);
    }
    
    @Test
    public void testAverageMixedList()
    {
        assertEquals(30.0, StatisticsCalc.average(mixedList), 0.001);
    }
    
    @Test
    public void testAverageNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.average(null);
        });
    }
    
    @Test
    public void testMaxNormalList()
    {
        assertEquals(50.0, StatisticsCalc.max(normalList), 0.001);
    }
    
    @Test
    public void testMaxSingleItem()
    {
        assertEquals(25.0, StatisticsCalc.max(singleItemList), 0.001);
    }
    
    @Test
    public void testMaxEmptyList()
    {
        assertEquals(-1.0, StatisticsCalc.max(emptyList), 0.001);
    }
    
    @Test
    public void testMaxAllInvalid()
    {
        assertEquals(-1.0, StatisticsCalc.max(allInvalidList), 0.001);
    }
    
    @Test
    public void testMaxMixedList()
    {
        assertEquals(50.0, StatisticsCalc.max(mixedList), 0.001);
    }
    
    @Test
    public void testMaxNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.max(null);
        });
    }
    
    @Test
    public void testMinNormalList()
    {
        assertEquals(10.0, StatisticsCalc.min(normalList), 0.001);
    }
    
    @Test
    public void testMinSingleItem()
    {
        assertEquals(25.0, StatisticsCalc.min(singleItemList), 0.001);
    }
    
    @Test
    public void testMinEmptyList()
    {
        assertEquals(-1.0, StatisticsCalc.min(emptyList), 0.001);
    }
    
    @Test
    public void testMinAllInvalid()
    {
        assertEquals(-1.0, StatisticsCalc.min(allInvalidList), 0.001);
    }
    
    @Test
    public void testMinMixedList()
    {
        assertEquals(10.0, StatisticsCalc.min(mixedList), 0.001);
    }
    
    @Test
    public void testMinNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.min(null);
        });
    }
    
    @Test
    public void testMaxDataPointNormalList()
    {
        DataPoint result = StatisticsCalc.maxDataPoint(normalList);
        assertNotNull(result);
        assertEquals(50.0, result.value(), 0.001);
        assertEquals(5, result.gridCode());
    }
    
    @Test
    public void testMaxDataPointSingleItem()
    {
        DataPoint result = StatisticsCalc.maxDataPoint(singleItemList);
        assertNotNull(result);
        assertEquals(25.0, result.value(), 0.001);
    }
    
    @Test
    public void testMaxDataPointEmptyList()
    {
        assertNull(StatisticsCalc.maxDataPoint(emptyList));
    }
    
    @Test
    public void testMaxDataPointAllInvalid()
    {
        assertNull(StatisticsCalc.maxDataPoint(allInvalidList));
    }
    
    @Test
    public void testMaxDataPointNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.maxDataPoint(null);
        });
    }
    
    @Test
    public void testMinDataPointNormalList()
    {
        DataPoint result = StatisticsCalc.minDataPoint(normalList);
        assertNotNull(result);
        assertEquals(10.0, result.value(), 0.001);
        assertEquals(1, result.gridCode());
    }
    
    @Test
    public void testMinDataPointSingleItem()
    {
        DataPoint result = StatisticsCalc.minDataPoint(singleItemList);
        assertNotNull(result);
        assertEquals(25.0, result.value(), 0.001);
    }
    
    @Test
    public void testMinDataPointEmptyList()
    {
        assertNull(StatisticsCalc.minDataPoint(emptyList));
    }
    
    @Test
    public void testMinDataPointAllInvalid()
    {
        assertNull(StatisticsCalc.minDataPoint(allInvalidList));
    }
    
    @Test
    public void testMinDataPointNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.minDataPoint(null);
        });
    }
    
    @Test
    public void testCountValidNormalList()
    {
        assertEquals(5, StatisticsCalc.countValid(normalList));
    }
    
    @Test
    public void testCountValidEmptyList()
    {
        assertEquals(0, StatisticsCalc.countValid(emptyList));
    }
    
    @Test
    public void testCountValidAllInvalid()
    {
        assertEquals(0, StatisticsCalc.countValid(allInvalidList));
    }
    
    @Test
    public void testCountValidMixedList()
    {
        assertEquals(3, StatisticsCalc.countValid(mixedList));
    }
    
    @Test
    public void testCountValidNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.countValid(null);
        });
    }
    
    @Test
    public void testStdDevNormalList()
    {
        assertEquals(14.142, StatisticsCalc.standardDeviation(normalList), 0.01);
    }
    
    @Test
    public void testStdDevSingleItem()
    {
        assertEquals(-1.0, StatisticsCalc.standardDeviation(singleItemList), 0.001);
    }
    
    @Test
    public void testStdDevEmptyList()
    {
        assertEquals(-1.0, StatisticsCalc.standardDeviation(emptyList), 0.001);
    }
    
    @Test
    public void testStdDevAllInvalid()
    {
        assertEquals(-1.0, StatisticsCalc.standardDeviation(allInvalidList), 0.001);
    }
    
    @Test
    public void testStdDevIdenticalValues()
    {
        List<DataPoint> sameValues = new ArrayList<>();
        sameValues.add(new DataPoint(1, 100, 200, 15.0));
        sameValues.add(new DataPoint(2, 101, 201, 15.0));
        sameValues.add(new DataPoint(3, 102, 202, 15.0));
        assertEquals(0.0, StatisticsCalc.standardDeviation(sameValues), 0.001);
    }
    
    @Test
    public void testStdDevNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.standardDeviation(null);
        });
    }
    
    @Test
    public void testMedianOddCount()
    {
        assertEquals(30.0, StatisticsCalc.median(normalList), 0.001);
    }
    
    @Test
    public void testMedianEvenCount()
    {
        List<DataPoint> evenList = new ArrayList<>();
        evenList.add(new DataPoint(1, 100, 200, 10.0));
        evenList.add(new DataPoint(2, 101, 201, 20.0));
        evenList.add(new DataPoint(3, 102, 202, 30.0));
        evenList.add(new DataPoint(4, 103, 203, 40.0));
        assertEquals(25.0, StatisticsCalc.median(evenList), 0.001);
    }
    
    @Test
    public void testMedianSingleItem()
    {
        assertEquals(25.0, StatisticsCalc.median(singleItemList), 0.001);
    }
    
    @Test
    public void testMedianEmptyList()
    {
        assertEquals(-1.0, StatisticsCalc.median(emptyList), 0.001);
    }
    
    @Test
    public void testMedianAllInvalid()
    {
        assertEquals(-1.0, StatisticsCalc.median(allInvalidList), 0.001);
    }
    
    @Test
    public void testMedianMixedList()
    {
        assertEquals(30.0, StatisticsCalc.median(mixedList), 0.001);
    }
    
    @Test
    public void testMedianUnsortedValues()
    {
        List<DataPoint> unsorted = new ArrayList<>();
        unsorted.add(new DataPoint(1, 100, 200, 50.0));
        unsorted.add(new DataPoint(2, 101, 201, 10.0));
        unsorted.add(new DataPoint(3, 102, 202, 40.0));
        unsorted.add(new DataPoint(4, 103, 203, 20.0));
        unsorted.add(new DataPoint(5, 104, 204, 30.0));
        assertEquals(30.0, StatisticsCalc.median(unsorted), 0.001);
    }
    
    @Test
    public void testMedianNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.median(null);
        });
    }
    
    @Test
    public void testYearlyAverageTrend()
    {
        HashMap<String, List<DataPoint>> yearlyData = new HashMap<>();
        
        List<DataPoint> year2020 = new ArrayList<>();
        year2020.add(new DataPoint(1, 100, 200, 10.0));
        year2020.add(new DataPoint(2, 101, 201, 20.0));
        
        List<DataPoint> year2021 = new ArrayList<>();
        year2021.add(new DataPoint(3, 102, 202, 30.0));
        year2021.add(new DataPoint(4, 103, 203, 40.0));
        
        yearlyData.put("2020", year2020);
        yearlyData.put("2021", year2021);
        
        Map<String, Double> trend = StatisticsCalc.yearlyAverageTrend(yearlyData);
        
        assertEquals(2, trend.size());
        assertEquals(15.0, trend.get("2020"), 0.001);  // (10+20)/2
        assertEquals(35.0, trend.get("2021"), 0.001);  // (30+40)/2
    }
    
    @Test
    public void testYearlyAverageTrendEmpty()
    {
        HashMap<String, List<DataPoint>> yearlyData = new HashMap<>();
        Map<String, Double> trend = StatisticsCalc.yearlyAverageTrend(yearlyData);
        assertTrue(trend.isEmpty());
    }
    
    @Test
    public void testYearlyAverageTrendNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.yearlyAverageTrend(null);
        });
    }
    
    @Test
    public void testYearlyMaxTrend()
    {
        HashMap<String, List<DataPoint>> yearlyData = new HashMap<>();
        
        List<DataPoint> year2020 = new ArrayList<>();
        year2020.add(new DataPoint(1, 100, 200, 10.0));
        year2020.add(new DataPoint(2, 101, 201, 20.0));
        
        List<DataPoint> year2021 = new ArrayList<>();
        year2021.add(new DataPoint(3, 102, 202, 30.0));
        year2021.add(new DataPoint(4, 103, 203, 40.0));
        
        yearlyData.put("2020", year2020);
        yearlyData.put("2021", year2021);
        
        Map<String, Double> trend = StatisticsCalc.yearlyMaxTrend(yearlyData);
        
        assertEquals(20.0, trend.get("2020"), 0.001);
        assertEquals(40.0, trend.get("2021"), 0.001);
    }
    
    @Test
    public void testYearlyMaxTrendNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.yearlyMaxTrend(null);
        });
    }
    
    @Test
    public void testYearlyMinTrend()
    {
        HashMap<String, List<DataPoint>> yearlyData = new HashMap<>();
        
        List<DataPoint> year2020 = new ArrayList<>();
        year2020.add(new DataPoint(1, 100, 200, 10.0));
        year2020.add(new DataPoint(2, 101, 201, 20.0));
        
        List<DataPoint> year2021 = new ArrayList<>();
        year2021.add(new DataPoint(3, 102, 202, 30.0));
        year2021.add(new DataPoint(4, 103, 203, 40.0));
        
        yearlyData.put("2020", year2020);
        yearlyData.put("2021", year2021);
        
        Map<String, Double> trend = StatisticsCalc.yearlyMinTrend(yearlyData);
        
        assertEquals(10.0, trend.get("2020"), 0.001);
        assertEquals(30.0, trend.get("2021"), 0.001);
    }
    
    @Test
    public void testYearlyMinTrendNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsCalc.yearlyMinTrend(null);
        });
    }
    
    @Test
    public void testZeroValueIsValid()
    {
        List<DataPoint> zeroList = new ArrayList<>();
        zeroList.add(new DataPoint(1, 100, 200, 0.0));
        zeroList.add(new DataPoint(2, 101, 201, 10.0));
        
        // 0.0 is >= 0, so it should be counted as valid
        assertEquals(2, StatisticsCalc.countValid(zeroList));
        assertEquals(5.0, StatisticsCalc.average(zeroList), 0.001);
        assertEquals(0.0, StatisticsCalc.min(zeroList), 0.001);
        assertEquals(10.0, StatisticsCalc.max(zeroList), 0.001);
    }
    
    @Test
    public void testStdDevTwoItems()
    {
        List<DataPoint> twoItems = new ArrayList<>();
        twoItems.add(new DataPoint(1, 100, 200, 10.0));
        twoItems.add(new DataPoint(2, 101, 201, 20.0));
        
        // Mean = 15, variance = ((25 + 25) / 2) = 25, std dev = 5.0
        assertEquals(5.0, StatisticsCalc.standardDeviation(twoItems), 0.001);
    }
    
    @Test
    public void testMedianTwoItems()
    {
        List<DataPoint> twoItems = new ArrayList<>();
        twoItems.add(new DataPoint(1, 100, 200, 10.0));
        twoItems.add(new DataPoint(2, 101, 201, 20.0));
        
        // Median of two values = (10 + 20) / 2 = 15.0
        assertEquals(15.0, StatisticsCalc.median(twoItems), 0.001);
    }
}
