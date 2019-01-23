package com.ps.bitcoin.vo;

import java.util.Map;

public class BitcoinResponseVO {

    private Map priceMap;
    private Double maxValue;

    public Map getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map priceMap) {
        this.priceMap = priceMap;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
}
