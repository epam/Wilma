package com.epam.wilma.stubconfig.initializer.template;
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

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;
import com.epam.wilma.stubconfig.initializer.support.ExternalInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Provides unit tests for the class {@link TemplateGeneratorInitializer}.
 *
 * @author Tamas_Bihari
 */
public class TemplateGeneratorInitializerTest {
    private static final String CLASS = "CLASS_NAME";

    @Mock
    private StubResourcePathProvider stubResourcePathProvider;
    @Mock
    private TemplateGenerator templateGenerator;
    @Mock
    private ExternalInitializer externalInitializer;

    @InjectMocks
    private TemplateGeneratorInitializer underTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTemplateGeneratorShouldReturnWithTemplateGenerator() {
        //GIVEN
        given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn(CLASS);
        given(externalInitializer.loadExternalClass(CLASS, CLASS, TemplateGenerator.class)).willReturn(templateGenerator);
        //WHEN
        TemplateGenerator actual = underTest.getTemplateGenerator(CLASS);
        //THEN
        assertEquals(actual, templateGenerator);
    }

    @Test
    public void testGetTemplateGeneratorShouldThrowExceptionWhenExternalClassCanNotLoad() {
        Assertions.assertThrows(DescriptorValidationFailedException.class, () -> {
            //GIVEN
            given(stubResourcePathProvider.getTemplatesPathAsString()).willReturn(CLASS);
            given(externalInitializer.loadExternalClass(CLASS, CLASS, TemplateGenerator.class)).willThrow(
                    new DescriptorValidationFailedException(CLASS));
            //WHEN
            underTest.getTemplateGenerator(CLASS);
            //THEN
        });
    }
}
