package com.design.appproject.calc;

/**
 * @author zhangjiangshuai
 * @date 2024-04/02
 */
public class RightParenthesesOperator extends Operator {
    RightParenthesesOperator(Integer id, String value, Integer factor) {
        super(id, value, factor);
    }

    @Override
    public double operate(double a, double b) {
        throw new RuntimeException("this operator cannot do operate");
    }
}