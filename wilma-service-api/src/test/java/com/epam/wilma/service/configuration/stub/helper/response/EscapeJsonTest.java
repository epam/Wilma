package com.epam.wilma.service.configuration.stub.helper.response;
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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for EscapeJson class.
 *
 * @author Tamas_Kohegyi
 */
public class EscapeJsonTest {

    private EscapeJson escapeJson = new EscapeJson();

    @Test
    public void testEscapingNullString() {
        String revisedText = escapeJson.escapeJson(null);
        Assert.assertNull(revisedText);
    }

    @Test
    public void testEscapingEmptyString() {
        String text = "";
        String revisedText = escapeJson.escapeJson(text);
        Assert.assertNotNull(revisedText);
        Assert.assertEquals(revisedText, text);
    }

    @Test
    public void testEscapingNonProblematicString() {
        String text = "alma";
        String revisedText = escapeJson.escapeJson(text);
        Assert.assertNotNull(revisedText);
        Assert.assertEquals(revisedText, text);
    }

    @Test
    public void testEscapingProblematicString() {
        String text = "alma<>\"\'&";
        String revisedText = escapeJson.escapeJson(text);
        Assert.assertNotNull(revisedText);
        Assert.assertEquals(revisedText, "alma<>\\\"\'&");
    }

}
