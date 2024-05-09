package com.design.appproject.calc;

/**
 * @author zhangjiangshuai
 * @date 2024-03/29
 */
public abstract class Operator {
    private Integer id;
    private String value;
    private Integer factor;

    Operator(Integer id, String value, Integer factor) {
        this.id = id;
        this.value = value;
        this.factor = factor;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Integer getFactor() {
        return factor;
    }

    public abstract double operate(double a, double b);
}