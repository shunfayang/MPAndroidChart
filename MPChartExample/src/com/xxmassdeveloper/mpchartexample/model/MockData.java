package com.xxmassdeveloper.mpchartexample.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangshunfa on 07/25/2018
 **/
public class MockData implements Serializable {
    private List<Long> array;
}

class Item implements Serializable {
    private long time = 0;
    private long btcPrice = 0;
    private long usdPrice = 0;
    private long cap = 0;
    private long volume = 0;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(long btcPrice) {
        this.btcPrice = btcPrice;
    }

    public long getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(long usdPrice) {
        this.usdPrice = usdPrice;
    }

    public long getCap() {
        return cap;
    }

    public void setCap(long cap) {
        this.cap = cap;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
