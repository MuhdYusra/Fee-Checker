package com.example.feechecker;

public class FeeCalculator {

    public static final double ALLOWANCE_ECONOMY = 20.0;
    public static final double ALLOWANCE_BUSINESS = 40.0;
    public static final double SERVICE_TAX_RATE = 0.08;

    public static class Result {
        private final double baggageWeight;
        private final double allowance;
        private final double excessWeight;
        private final double excessCharge;
        private final double serviceTax;
        private final double totalPayable;

        public Result(double baggageWeight, double allowance, double excessWeight,
                      double excessCharge, double serviceTax, double totalPayable) {
            this.baggageWeight = baggageWeight;
            this.allowance = allowance;
            this.excessWeight = excessWeight;
            this.excessCharge = excessCharge;
            this.serviceTax = serviceTax;
            this.totalPayable = totalPayable;
        }

        public double getBaggageWeight() {
            return baggageWeight;
        }

        public double getAllowance() {
            return allowance;
        }

        public double getExcessWeight() {
            return excessWeight;
        }

        public double getExcessCharge() {
            return excessCharge;
        }

        public double getServiceTax() {
            return serviceTax;
        }

        public double getTotalPayable() {
            return totalPayable;
        }
    }

    public static Result calculate(double baggageWeight, boolean isBusinessClass, double ratePerKg) {
        double allowance = isBusinessClass ? ALLOWANCE_BUSINESS : ALLOWANCE_ECONOMY;
        double excessWeight = Math.max(0.0, baggageWeight - allowance);
        double excessCharge = excessWeight * ratePerKg;
        double serviceTax = excessCharge * SERVICE_TAX_RATE;
        double totalPayable = excessCharge + serviceTax;

        return new Result(baggageWeight, allowance, excessWeight, excessCharge, serviceTax, totalPayable);
    }
}