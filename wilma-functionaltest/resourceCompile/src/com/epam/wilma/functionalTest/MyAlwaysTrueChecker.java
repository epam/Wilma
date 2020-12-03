package com.epam.wilma.functionalTest;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.dialog.condition.checker.ConditionChecker;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.springframework.stereotype.Component;

/**
 * A bare simple condition checker that fires on ever call...
 */
@Component
public class MyAlwaysTrueChecker implements ConditionChecker {
    @Override
    public boolean checkCondition(WilmaHttpRequest request, ParameterList parameters) {
        return true; //this is the way how to fire it
    }
}
