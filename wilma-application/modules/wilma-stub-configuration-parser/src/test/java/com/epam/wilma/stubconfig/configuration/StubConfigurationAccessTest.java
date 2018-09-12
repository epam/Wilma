package com.epam.wilma.stubconfig.configuration;
/*==========================================================================
Copyright since 2013, EPAM Systems

This file is part of Wilma.

Wilma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Wilma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.properties.PropertyHolder;
import com.epam.wilma.stubconfig.configuration.domain.PropertyDto;
import com.epam.wilma.stubconfig.dom.parser.node.helper.ClassNameMapper;

/**
 * Tests for {@link StubConfigurationAccess} class.
 * @author Tamas_Bihari, Tamas Kohegyi
 *
 */
public class StubConfigurationAccessTest {

    private static final int MAX_DEPTH = 10;

    @Mock
    private PropertyHolder propertyHolder;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private ClassNameMapper classNameMapper;

    @InjectMocks
    private StubConfigurationAccess underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadPropertiesShouldSetMaxDepth() {
        //GIVEN
        given(propertyHolder.getInt("stub.descriptor.max.depth")).willReturn(MAX_DEPTH);
        //WHEN
        underTest.loadProperties();
        //THEN
        PropertyDto actual = underTest.getProperties();
        assertEquals(actual.getMaxDepthOfTree(), MAX_DEPTH);
    }

    @Test
    public void testOnApplicationEventShouldSetTemplatesPath() {
        //GIVEN
        String templatePath = "templatePath";
        given(propertyHolder.get("stub.template.path")).willReturn(templatePath);
        //WHEN
        underTest.setProperties();
        //THEN
        verify(stubResourcePathProvider).setTemplatesPath(templatePath);
    }

    @Test
    public void testOnApplicationEventShouldSetResponseFormatterPath() {
        //GIVEN
        String responseFormatterPath = "responseFormatterPath";
        given(propertyHolder.get("stub.response.formatter.path")).willReturn(responseFormatterPath);
        //WHEN
        underTest.setProperties();
        //THEN
        verify(stubResourcePathProvider).setResponseFormatterPath(responseFormatterPath);
    }

    @Test
    public void testOnApplicationEventShouldSetConditionCheckerPath() {
        //GIVEN
        String conditionCheckerPath = "conditionCheckerPath";
        given(propertyHolder.get("stub.condition.checker.path")).willReturn(conditionCheckerPath);
        //WHEN
        underTest.setProperties();
        //THEN
        verify(stubResourcePathProvider).setConditionCheckerPath(conditionCheckerPath);
    }

    @Test
    public void testOnApplicationEventShouldSetInterceptorPath() {
        //GIVEN
        String interceptorPath = "interceptorPath";
        given(propertyHolder.get("stub.interceptor.path")).willReturn(interceptorPath);
        //WHEN
        underTest.setProperties();
        //THEN
        verify(stubResourcePathProvider).setInterceptorPath(interceptorPath);
    }

    @Test
    public void testOnApplicationEventShouldSetJarPath() {
        //GIVEN
        String jarPath = "jarPath";
        given(propertyHolder.get("stub.jar.path")).willReturn(jarPath);
        //WHEN
        underTest.setProperties();
        //THEN
        verify(stubResourcePathProvider).setJarPath(jarPath);
    }

    @Test
    public void testOnApplicationEventShouldSetSequenceHandlerPath() {
        //GIVEN
        String sequenceHandlerPath = "sequenceHandlerPath";
        given(propertyHolder.get("stub.sequence.handler.path")).willReturn(sequenceHandlerPath);
        //WHEN
        underTest.setProperties();
        //THEN
        verify(stubResourcePathProvider).setSequenceHandlerPath(sequenceHandlerPath);
    }

    @Test
    public void testOnApplicationEventShouldInitializeClassNameMapper() {
        //WHEN
        underTest.setProperties();
        //THEN
        verify(classNameMapper).initialize(new ArrayList<>());
    }
}
