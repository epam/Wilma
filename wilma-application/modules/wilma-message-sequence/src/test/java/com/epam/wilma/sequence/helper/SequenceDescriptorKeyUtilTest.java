package com.epam.wilma.sequence.helper;
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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Provides unit tests for the class {@link SequenceDescriptorKeyUtil}.
 * @author Tibor_Kovacs
 *
 */
public class SequenceDescriptorKeyUtilTest {

    private SequenceDescriptorKeyUtil underTest = new SequenceDescriptorKeyUtil();

    @Test
    public void testCreateDescriptorKey() {
        //GIVEN
        String testGroupName = "TestTeam";
        String testDescriptorName = "TestDescriptor";
        //WHEN
        String result = underTest.createDescriptorKey(testGroupName, testDescriptorName);
        //THEN
        Assert.assertEquals(result, testGroupName + SequenceConstants.DESCRIPTOR_KEY_PART_SEPARATOR.getConstant() + testDescriptorName);
    }

}
