package com.epam.wilma.service.unit.request;

/**
 * Created by tkohegyi on 2016. 01. 27..
 */
public class RequestConditionBuilder {

    RequestConditionBuilder and(RequestConditionBase conditionA, RequestConditionBase conditionB) {
        return this;
    }

    RequestConditionBuilder or(RequestConditionBase conditionA, RequestConditionBase conditionB) {
        return this;
    }

    RequestConditionBuilder not(RequestConditionBase condition) {
        return this;
    }

    RequestConditionBuilder condition() {
        return this;
    }
}
