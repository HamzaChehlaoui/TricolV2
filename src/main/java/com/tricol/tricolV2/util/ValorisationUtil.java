package com.tricol.tricolV2.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValorisationUtil {

    public static BigDecimal calculateCump(BigDecimal currentQty,
                                           BigDecimal currentAvgCost,
                                           BigDecimal incomingQty,
                                           BigDecimal incomingUnitCost) {
        BigDecimal safeCurrentQty = currentQty == null ? BigDecimal.ZERO : currentQty;
        BigDecimal safeCurrentAvg = currentAvgCost == null ? BigDecimal.ZERO : currentAvgCost;
        BigDecimal safeIncomingQty = incomingQty == null ? BigDecimal.ZERO : incomingQty;
        BigDecimal safeIncomingCost = incomingUnitCost == null ? BigDecimal.ZERO : incomingUnitCost;

        if (safeIncomingQty.compareTo(BigDecimal.ZERO) <= 0) {
            return safeCurrentAvg;
        }

        BigDecimal totalCost = safeCurrentAvg.multiply(safeCurrentQty)
                .add(safeIncomingCost.multiply(safeIncomingQty));
        BigDecimal totalQty = safeCurrentQty.add(safeIncomingQty);
        if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return totalCost.divide(totalQty, 4, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP);
    }
}
