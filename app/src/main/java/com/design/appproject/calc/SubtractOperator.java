package com.design.appproject.calc;

/**
 * @author zhangjiangshuai
 * @date 2024-04/26
 */
public class SubtractOperator extends Operator {
    SubtractOperator(Integer id, String value, Integer factor) {
        super(id, value, factor);
    }

    @Override
    public double operate(double a, double b) {
        return a - b;
    }
}