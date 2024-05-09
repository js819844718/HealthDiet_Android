package com.design.appproject.calc;
/**
 * @author zhangjiangshuai
 * @date 2024-03/22
 */
public class LeftParenthesesOperator extends Operator {
    LeftParenthesesOperator(Integer id, String value, Integer factor) {
        super(id, value, factor);
    }

    @Override
    public double operate(double a, double b) {
        throw new RuntimeException("this operator cannot do operate");
    }
}