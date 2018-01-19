package com.epam.wilma.webapp.config.servlet.helper;
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

import static org.testng.Assert.assertEquals;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.webapp.configuration.domain.MaintainerProperties;

/**
 * Test class for {@link MaintainerPropertiesJsonBuilder}.
 * @author Marton_Sereg
 *
 */
public class MaintainerPropertiesJsonBuilderTest {

    @InjectMocks
    private MaintainerPropertiesJsonBuilder underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void testBuildMaintainerPropertiesJsonShouldCreateProperJson() {
        //GIVEN
        MaintainerProperties maintainerProperties = new MaintainerProperties("cron", "method", 10, "10S");
        String expected = "{\"cronExpression\":\"cron\",\"maintainerMethod\":\"method\",\"fileLimit\":10,\"timeLimit\":\"10S\"}";
        //WHEN
        String actual = underTest.buildMaintainerPropertiesJson(maintainerProperties);
        //THEN
        assertEquals(actual, expected);

    }

}
