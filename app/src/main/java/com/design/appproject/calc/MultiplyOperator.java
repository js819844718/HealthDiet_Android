package com.design.appproject.calc;


/**
 * @author zhangjiangshuai
 * @date 2024-03/23
 */
public class MultiplyOperator extends Operator {
    MultiplyOperator(Integer id, String value, Integer factor) {
        super(id, value, factor);
    }

    @Override
    public double operate(double a, double b) {
        return a * b;
    }
}