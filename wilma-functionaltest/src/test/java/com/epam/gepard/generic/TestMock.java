package com.epam.gepard.generic;

import com.epam.gepard.annotations.TestClass;
import org.junit.Test;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

/**
 * Used for testing.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "TEST", name = "Self-Test Class")
public class TestMock implements GepardTestClass {

    private String one;
    private String two;

    /**
     * Just and empty and always passing test.
     */
    @Test
    public void emptyTest() {
    }
}
