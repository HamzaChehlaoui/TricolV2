package com.tricol.tricolV2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    public enum ValuationMethod {
        FIFO,
        CUMP
    }

    @Value("${app.stock.valuation-method:CUMP}")
    private String valuationMethod;

    public ValuationMethod getValuationMethod() {
        try {
            return ValuationMethod.valueOf(valuationMethod.toUpperCase());
        } catch (Exception ignored) {
            return ValuationMethod.CUMP;
        }
    }
}
