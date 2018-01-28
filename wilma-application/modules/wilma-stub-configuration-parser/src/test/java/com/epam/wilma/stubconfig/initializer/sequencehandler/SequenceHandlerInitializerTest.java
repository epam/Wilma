package com.epam.wilma.stubconfig.initializer.sequencehandler;
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
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.domain.stubconfig.sequencehandler.DummySequenceHandler;
import com.epam.wilma.domain.stubconfig.sequencehandler.DummySequenceHandler2;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;

/**
 * Unit test for {@link SequenceHandlerInitializer}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class SequenceHandlerInitializerTest {

    private static final String PATH = "config/sequencehandlers";
    private static final String SEQUENCE_HANDLER_CLASS = "DummySequenceHandler";
    private List<SequenceHandler> sequenceHandlers;
    private SequenceHandler sequenceHandler;

    @Mock
    private ApplicationContext appContext;
    @Mock
    private ExternalInitializer externalInitializer;
    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemporaryStubResourceHolder stubResourceHolder;
    @InjectMocks
    private SequenceHandlerInitializer underTest;

    @BeforeMethod
    public void setUp() {
        underTest = Mockito.spy(new SequenceHandlerInitializer());
        MockitoAnnotations.initMocks(this);
        sequenceHandlers = new ArrayList<>();
        given(stubResourceHolder.getSequenceHandlers()).willReturn(sequenceHandlers);
    }

    @Test
    public void testGetSequenceHandlerShouldReturnSequenceHandler() {
        //GIVEN
        given(appContext.getBean(SEQUENCE_HANDLER_CLASS, SequenceHandler.class)).willThrow(new NoSuchBeanDefinitionException(""));
        sequenceHandler = new DummySequenceHandler();
        sequenceHandlers.add(sequenceHandler);
        //WHEN
        SequenceHandler actual = underTest.getExternalClassObject(SEQUENCE_HANDLER_CLASS);
        //THEN
        assertEquals(actual, sequenceHandler);
    }

    @Test
    public void testGetSequenceHandlerShouldReturnSequenceHandlerWhenTheClassIsManagedBySpring() {
        //GIVEN
        given(appContext.getBean(SEQUENCE_HANDLER_CLASS, SequenceHandler.class)).willReturn(sequenceHandler);
        //WHEN
        SequenceHandler actual = underTest.getExternalClassObject(SEQUENCE_HANDLER_CLASS);
        //THEN
        assertEquals(actual, sequenceHandler);
    }

    @Test
    public void testGetSequenceHandlerWhenInternalExistsShouldReturnSequenceHandler() {
        //GIVEN
        sequenceHandler = new DummySequenceHandler();
        sequenceHandlers.add(sequenceHandler);
        //WHEN
        SequenceHandler actual = underTest.getExternalClassObject(SEQUENCE_HANDLER_CLASS);
        //THEN
        assertEquals(actual, sequenceHandler);
    }

    @Test
    public void testGetSequenceHandlerWhenMoreInternalsExistShouldReturnSequenceHandler() {
        //GIVEN
        sequenceHandlers.clear();
        sequenceHandler = new DummySequenceHandler2();
        sequenceHandlers.add(sequenceHandler);
        sequenceHandler = new DummySequenceHandler();
        sequenceHandlers.add(sequenceHandler);
        //WHEN
        SequenceHandler actual = underTest.getExternalClassObject(SEQUENCE_HANDLER_CLASS);
        //THEN
        assertEquals(actual, sequenceHandler);
    }

    @Test
    public void testGetSequenceHandlerWhenExternalExistsShouldReturnSequenceHandler() {
        //GIVEN
        given(stubResourcePathProvider.getSequenceHandlerPathAsString()).willReturn(PATH);
        sequenceHandler = new DummySequenceHandler();
        given(externalInitializer.loadExternalClass(SEQUENCE_HANDLER_CLASS, PATH, SequenceHandler.class)).willReturn(sequenceHandler);
        //WHEN
        SequenceHandler actual = underTest.getExternalClassObject(SEQUENCE_HANDLER_CLASS);
        //THEN
        assertEquals(actual.getClass(), sequenceHandler.getClass());
    }

    @Test(expectedExceptions = DescriptorValidationFailedException.class)
    public void testGetSequenceHandlerWhenItDoesNotExistShouldThrowException() {
        //GIVEN
        given(stubResourcePathProvider.getSequenceHandlerPathAsString()).willReturn(PATH);
        given(externalInitializer.loadExternalClass(SEQUENCE_HANDLER_CLASS, PATH, SequenceHandler.class)).willThrow(
                new DescriptorValidationFailedException(SEQUENCE_HANDLER_CLASS));
        //WHEN
        underTest.getExternalClassObject(SEQUENCE_HANDLER_CLASS);
        //THEN it should throw exception
    }

}
