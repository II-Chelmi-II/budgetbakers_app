package org.michel.model;

public class Currency {
    private final int currency_id;
    private final String name;
    private final String code;

    public Currency(int currency_id, String name, String code) {
        this.currency_id = currency_id;
        this.name = name;
        this.code = code;
    }

    public int getCurrency_id() {
        return currency_id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
