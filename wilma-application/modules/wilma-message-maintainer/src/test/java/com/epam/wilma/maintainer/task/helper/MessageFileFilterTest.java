package com.epam.wilma.maintainer.task.helper;
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
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for MessageFileFilter.
 * @author Tamas_Bihari
 *
 */
public class MessageFileFilterTest {

    @InjectMocks
    private MessageFileFilter underTest;

    @Mock
    private File file;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAcceptShouldReturnFalseWhenParameterIsNotFile() {
        //GIVEN
        given(file.isFile()).willReturn(false);
        //WHEN
        boolean result = underTest.accept(file);
        //THEN
        verify(file).isFile();
        assertFalse(result);
    }

    @Test
    public void testAcceptShouldReturnFalseWhenFileNameDoesNotMatchWithRegExp() {
        //GIVEN
        String example = "20130701132107.0025red.txt";
        given(file.isFile()).willReturn(true);
        given(file.getName()).willReturn(example);
        //WHEN
        boolean result = underTest.accept(file);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testAcceptShouldReturnTrueWhenParameterFileAndNameMatchWithRegExp() {
        //GIVEN
        String fileName = "20130701132107.0025req.txt";
        given(file.isFile()).willReturn(true);
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.accept(file);
        //THEN
        assertTrue(result);
    }

    @Test
    public void testIsMatchWithRegExpShouldReturnFalseWhenSuffixDoesNotMatch() {
        //GIVEN
        String fileName = "20130701132107.0025asd.txt";
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.isMatchWithRegExp(file);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testIsMatchWithRegExpShouldReturnFalseWhenTimeStampDoesNotMatch() {
        //GIVEN
        String fileName = "2013070113210766.0025req.txt";
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.isMatchWithRegExp(file);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testIsMatchWithRegExpShouldReturnFalseWhenIDDoesNotMatch() {
        //GIVEN
        String fileName = "20130701132107.00325req.txt";
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.isMatchWithRegExp(file);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testIsMatchWithRegExpShouldReturnFalseWhenExtensionDoesNotMatch() {
        //GIVEN
        String fileName = "20130701132107.0025req.csv";
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.isMatchWithRegExp(file);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testIsMatchWithRegExpShouldReturnFalseWhenDotIsMissing() {
        //GIVEN
        String fileName = "201307011321070025req.csv";
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.isMatchWithRegExp(file);
        //THEN
        assertFalse(result);
    }

    @Test
    public void testIsMatchWithRegExpShouldReturnTrueWhenNameDoesMatch() {
        //GIVEN
        String fileName = "20130701132107.0025resp.txt";
        given(file.getName()).willReturn(fileName);
        //WHEN
        boolean result = underTest.isMatchWithRegExp(file);
        //THEN
        assertTrue(result);
    }
}
