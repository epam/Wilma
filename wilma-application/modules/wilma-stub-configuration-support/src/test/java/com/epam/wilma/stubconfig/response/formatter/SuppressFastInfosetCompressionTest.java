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
 * Tests for {@link SuppressFastInfosetCompression}.
 *
 * @author Tamas_Kohegyi
 */
public class SuppressFastInfosetCompressionTest {

    private static final String ANY_HEADER_VALUE = "value";

    @Mock
    private HttpServletResponse response;
    @Mock
    private WilmaHttpRequest request;
    @Mock
    private ParameterList parameterMap;

    private SuppressFastInfosetCompression underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new SuppressFastInfosetCompression();
    }

    @Test
    public void testFormatTemplateWithoutExistingHeader() {
        //GIVEN
        //SetUp
        given(request.getHeader(SuppressFastInfosetCompression.ACCEPT_HEADER_KEY)).willReturn(SuppressFastInfosetCompression.ACCEPT_VALUE_FASTINFOSET);
        given(response.getHeader(SuppressFastInfosetCompression.HEADER_KEY_SUPPRESS_ENCODING)).willReturn(null);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader(SuppressFastInfosetCompression.HEADER_KEY_SUPPRESS_ENCODING, SuppressFastInfosetCompression.FASTINFOSET);
    }

    @Test
    public void testFormatTemplateWithExistingHeader() {
        //GIVEN
        //SetUp
        given(request.getHeader(SuppressFastInfosetCompression.ACCEPT_HEADER_KEY)).willReturn(SuppressFastInfosetCompression.ACCEPT_VALUE_FASTINFOSET);
        given(response.getHeader(SuppressFastInfosetCompression.HEADER_KEY_SUPPRESS_ENCODING)).willReturn(ANY_HEADER_VALUE);
        //WHEN
        underTest.formatResponse(request, response, null, parameterMap);
        //THEN
        verify(response).addHeader(SuppressFastInfosetCompression.HEADER_KEY_SUPPRESS_ENCODING, ANY_HEADER_VALUE + "," + SuppressFastInfosetCompression.FASTINFOSET);
    }

}
