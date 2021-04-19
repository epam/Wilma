package com.epam.gepard.datadriven.feeders;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for LabelBasedDataFeeder and related classes.
 * Created by tkohegyi
 */
public class LabelBasedDataFeederTest {
    private static final int ERROR_FEEDER_RELATION_LABEL_TYPE_IS_MISSING = -1;
    private static final int ERROR_FEEDER_RELATION_EXCEPTION = -3;
    private static final int ERROR_FEEDER_RELATION_WRONG_FORMAT = -4;
    private static final int ERROR_FEEDER_RELATION_WRONG_ROW_NUMBER = -5;
    private static final int ERROR_FEEDER_DUPLICATED = -9;
    private static final int ERROR_FEEDER_FILE_CONTENT_ERROR = -20;
    private LabelBasedDataFeeder underTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        underTest = new LabelBasedDataFeeder();
    }

    @Test
    public void testInitWithNullParameter() {
        //given
        String className = this.getClass().getCanonicalName();
        // and the parameter is null
        //when
        int returnValue = underTest.init(className, null, null);
        //then
        assertEquals(ERROR_FEEDER_RELATION_WRONG_FORMAT, returnValue);
    }

    @Test
    public void testInitWithBadParameterBegin() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_RELATION_WRONG_FORMAT, returnValue);
    }

    @Test
    public void testInitWithBadParameterEnd() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_RELATION_WRONG_FORMAT, returnValue);
    }

    @Test
    public void testInitWithDefaultEmptyFeeder() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(0, returnValue); //this should be OK
    }

    @Test
    public void testInitWithDefaultDefaultFeeder() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[DEFAULT]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(0, returnValue); //this should be ok
    }

    @Test
    public void testInitWithBadRowNo() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[:x]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_RELATION_WRONG_ROW_NUMBER, returnValue);
    }

    @Test
    public void testInitWithBadLabelType() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[:::BADTYPE]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_RELATION_LABEL_TYPE_IS_MISSING, returnValue);
    }

    @Test
    public void testInitWithDuplicatedDefaultFeeders() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[DEFAULT]+[DEFAULT]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_DUPLICATED, returnValue);
    }

    @Test
    public void testInitWithDuplicatedDefaultFeeders2() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[]+[DEFAULT]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_DUPLICATED, returnValue);
    }

    @Test
    public void testInitWithDuplicatedDefaultFeeders3() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[]+[]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_DUPLICATED, returnValue);
    }

    @Test
    public void testInitWithFeederRelation() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[com/epam/gepard/datadriven/feeders/a.txt]+[com/epam/gepard/datadriven/feeders/b.txt]x[com/epam/gepard/datadriven/feeders/c.txt]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(0, returnValue); //this should be ok
    }

    @Test
    public void testInitWithBadFeederRelation() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[com/epam/gepard/datadriven/feeders/a.txt].[com/epam/gepard/datadriven/feeders/b.txt]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_RELATION_EXCEPTION, returnValue);
    }

    @Test
    public void testInitWithBadFeederContent() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[com/epam/gepard/datadriven/feeders/badcontent.txt]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(ERROR_FEEDER_FILE_CONTENT_ERROR, returnValue);
    }

    @Test
    public void testInitWithCsvFeeder() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[com/epam/gepard/datadriven/feeders/csv.csv]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(0, returnValue); //this should be ok
    }

    @Test
    public void testInitUseRandomSeleaction() {
        //given
        String className = this.getClass().getCanonicalName();
        String parameter = "[:::RANDOM]";
        //when
        int returnValue = underTest.init(className, parameter, null);
        //then
        assertEquals(0, returnValue); //this should be ok

    }

}
