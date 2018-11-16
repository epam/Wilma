package com.epam.wilma.stubconfig.response.formatter;

import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.domain.stubconfig.parameter.ParameterList;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link SuppressGzipCompression}.
 *
 * @author Tamas_Kohegyi
 */
public class SuppressGzipCompressionTest {

    private static final String ANY_HEADER_VALUE = "value";

    @Mock
    private HttpServletResponse response;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList parameterMap;

    private SuppressGzipCompression underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new SuppressGzipCompression();
    }

    @Test
    public void testFormatTemplateWithoutExistingHeader() {
        //GIVEN
        //SetUp
        given(request.getHeader(SuppressGzipCompression.HEADER_KEY_ACCEPT_ENCODING)).willReturn(SuppressGzipCompression.HEADER_VALUE_GZIP);
        given(response.getHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING)).willReturn(null);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING, SuppressGzipCompression.HEADER_VALUE_GZIP);
    }

    @Test
    public void testFormatTemplateWithExistingHeader() {
        //GIVEN
        //SetUp
        given(request.getHeader(SuppressGzipCompression.HEADER_KEY_ACCEPT_ENCODING)).willReturn(SuppressGzipCompression.HEADER_VALUE_GZIP);
        given(response.getHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING)).willReturn(ANY_HEADER_VALUE);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader(SuppressGzipCompression.HEADER_KEY_SUPPRESS_ENCODING, ANY_HEADER_VALUE + "," + SuppressGzipCompression.HEADER_VALUE_GZIP);
    }

}
