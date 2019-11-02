package com.safe.gallery.calculator.activities;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;

import java.util.Iterator;

public class ExtendedDoubleEvaluator extends DoubleEvaluator {

    private static final Function ACOSD = new Function("acosd", 1);
    private static final Function ASIND = new Function("asind", 1);
    private static final Function ATAND = new Function("atand", 1);
    private static final Function CBRT = new Function("cbrt", 1);
    private static final Parameters PARAMS = DoubleEvaluator.getDefaultParameters();
    private static final Function SQRT = new Function("sqrt", 1);

    static {
        PARAMS.add(SQRT);
        PARAMS.add(CBRT);
        PARAMS.add(ASIND);
        PARAMS.add(ACOSD);
        PARAMS.add(ATAND);
    }

    public ExtendedDoubleEvaluator() {
        super(PARAMS);
    }

    public Double evaluate(Function function, Iterator<Double> it, Object obj) {
        return function == SQRT ? Double.valueOf(Math.sqrt(((Double) it.next()).doubleValue())) : function == CBRT ? Double.valueOf(Math.cbrt(((Double) it.next()).doubleValue())) : function == ASIND ? Double.valueOf(Math.toDegrees(Math.asin(((Double) it.next()).doubleValue()))) : function == ACOSD ? Double.valueOf(Math.toDegrees(Math.acos(((Double) it.next()).doubleValue()))) : function == ATAND ? Double.valueOf(Math.toDegrees(Math.atan(((Double) it.next()).doubleValue()))) : super.evaluate(function, it, obj);
    }
}
