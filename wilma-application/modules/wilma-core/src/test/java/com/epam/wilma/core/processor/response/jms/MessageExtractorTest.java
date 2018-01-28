package com.epam.wilma.core.processor.response.jms;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.epam.wilma.core.processor.WilmaEntityProcessorInterface;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;

/**
 * Unit test for {@link MessageExtractor}.
 *
 * @author Balazs_Berkes
 */
public class MessageExtractorTest {

    @Mock
    private WilmaEntityProcessorInterface fastInfosetDecompressorProcessor;
    @Mock
    private WilmaEntityProcessorInterface base64DecoderProcessor;

    @Mock
    private WilmaHttpEntity message;

    @InjectMocks
    private MessageExtractor underTest;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExtractShouldExtractContent() throws Exception {
        underTest.extract(message);

        verify(base64DecoderProcessor).process(message);
        verify(fastInfosetDecompressorProcessor).process(message);
    }

    @Test
    public void testExtractShouldDoNothingWhenExceptionIsThrown() throws Exception {
        doThrow(new ApplicationException("")).when(base64DecoderProcessor).process(message);

        underTest.extract(message);
    }
}
