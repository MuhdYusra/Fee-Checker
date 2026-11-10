package com.example.feechecker;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FeeCalculatorTest {

    private static final double DELTA = 0.001;

    @Test
    public void testSampleCase1_EconomyOverweight() {
        // Baggage weight: 25, Economy, Rate: 15
        FeeCalculator.Result result = FeeCalculator.calculate(25.0, false, 15.0);

        assertEquals(5.0, result.getExcessWeight(), DELTA);
        assertEquals(75.00, result.getExcessCharge(), DELTA);
        assertEquals(6.00, result.getServiceTax(), DELTA);
        assertEquals(81.00, result.getTotalPayable(), DELTA);
    }

    @Test
    public void testSampleCase2_EconomyUnderWeight() {
        // Baggage weight: 18, Economy, Rate: 15
        FeeCalculator.Result result = FeeCalculator.calculate(18.0, false, 15.0);

        assertEquals(0.0, result.getExcessWeight(), DELTA);
        assertEquals(0.00, result.getExcessCharge(), DELTA);
        assertEquals(0.00, result.getServiceTax(), DELTA);
        assertEquals(0.00, result.getTotalPayable(), DELTA);
    }

    @Test
    public void testSampleCase3_BusinessOverweight() {
        // Baggage weight: 55, Business, Rate: 12
        FeeCalculator.Result result = FeeCalculator.calculate(55.0, true, 12.0);

        assertEquals(15.0, result.getExcessWeight(), DELTA);
        assertEquals(180.00, result.getExcessCharge(), DELTA);
        assertEquals(14.40, result.getServiceTax(), DELTA);
        assertEquals(194.40, result.getTotalPayable(), DELTA);
    }

    @Test
    public void testSampleCase4_BusinessDecimalOverweight() {
        // Baggage weight: 42.5, Business, Rate: 12
        FeeCalculator.Result result = FeeCalculator.calculate(42.5, true, 12.0);

        assertEquals(2.5, result.getExcessWeight(), DELTA);
        assertEquals(30.00, result.getExcessCharge(), DELTA);
        assertEquals(2.40, result.getServiceTax(), DELTA);
        assertEquals(32.40, result.getTotalPayable(), DELTA);
    }
}