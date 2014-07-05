package com.tcswidget.data;

import java.util.HashMap;
import java.util.Map;

/**
 * User: antosha
 * Date: 7/5/14
 */
public class DataManager {
    private static class InstanceHolder {
        static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private DataManager(){}

    private Map<String, String> balances = new HashMap<String, String>();

    public synchronized void updateBalance(String card, String balance) {
        balances.put(card, balance);
    }

    public synchronized String getBalance(String card) {
        return balances.get(card);
    }
}
