package com.design.appproject.calc;

/**
 * @author zhangjiangshuai
 * @date 2024-03/21
 */
public class AddOperator extends Operator {
    AddOperator(Integer id, String value, Integer factor) {
        super(id, value, factor);
    }

    @Override
    public double operate(double a, double b) {
        return a + b;
    }
}