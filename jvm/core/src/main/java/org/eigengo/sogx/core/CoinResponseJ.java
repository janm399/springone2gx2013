package org.eigengo.sogx.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoinResponseJ {
    private final boolean succeeded;
    private final List<CoinJ> coins;

    @JsonCreator
    public CoinResponseJ(@JsonProperty("succeeded") boolean succeeded, @JsonProperty(value = "coins", required = false) List<CoinJ> coins) {
        this.succeeded = succeeded;
        this.coins = coins;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public List<CoinJ> getCoins() {
        return coins;
    }
}
