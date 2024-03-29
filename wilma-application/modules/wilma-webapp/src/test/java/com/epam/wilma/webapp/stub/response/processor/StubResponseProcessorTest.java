package com.epam.wilma.webapp.stub.response.processor;
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

import com.epam.wilma.webapp.stub.response.processor.gzip.GzipCompressorProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link StubResponseProcessor}.
 *
 * @author Tamas_Bihari
 */
public class StubResponseProcessorTest {
    @Mock
    private GzipCompressorProcessor processorMock;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;

    private byte[] responseBody;
    private StubResponseProcessor underTest;
    private List<ResponseProcessor> processors;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new StubResponseProcessor();
        responseBody = "SIMPLE_RESPONSE".getBytes();
        processors = new ArrayList<>();
        processors.add(processorMock);
        underTest.setResponseProcessors(processors);
    }

    @Test
    public void testProcessResponseShouldReturnWithResponseBodyWhenProcessorListIsEmpty() {
        //GIVEN
        processors.clear();
        //WHEN
        byte[] actual = underTest.processResponse(req, resp, responseBody);
        //THEN
        assertEquals(actual, responseBody);
    }

    @Test
    public void testProcessResponseShouldCallProcessorWhenProcessorListIsNotEmpty() {
        //GIVEN
        given(processorMock.process(req, resp, responseBody)).willReturn(responseBody);
        //WHEN
        byte[] actual = underTest.processResponse(req, resp, responseBody);
        //THEN
        verify(processorMock).process(req, resp, responseBody);
        assertEquals(actual, responseBody);
    }

    @Test
    public void testProcessResponseShouldCallTwiceProcessorWhenProcessorListContainsTwoProcessor() {
        //GIVEN
        processors.add(processorMock);
        given(processorMock.process(req, resp, responseBody)).willReturn(responseBody);
        //WHEN
        byte[] actual = underTest.processResponse(req, resp, responseBody);
        //THEN
        verify(processorMock, times(2)).process(req, resp, responseBody);
        assertEquals(actual, responseBody);
    }

}
