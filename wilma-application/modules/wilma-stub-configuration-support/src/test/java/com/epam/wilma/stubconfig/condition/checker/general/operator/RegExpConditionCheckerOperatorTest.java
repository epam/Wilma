package com.epam.wilma.stubconfig.condition.checker.general.operator;

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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.stubconfig.condition.checker.general.operator.helper.RegExpPatternFactory;
import com.epam.wilma.domain.stubconfig.exception.RegularExpressionEvaluationException;

/**
 * Tests for {@link RegExpConditionCheckerOperator}.
 * @author Tamas_Bihari
 *
 */
public class RegExpConditionCheckerOperatorTest {

    private static final String EX_REG_EXP = "A[a-zA-Z]{8}ary";
    private static final String EX_TARGET = "TARGET_STRING_Aqwertzuoary";
    @Mock
    private RegExpPatternFactory patternFactory;

    @InjectMocks
    private RegExpConditionCheckerOperator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckTargetShouldReturnFalseWhenRegExpIsEmptyString() {
        //GIVEN in setUp
        //WHEN
        boolean actual = underTest.checkTarget("", EX_TARGET);
        //THEN
        assertFalse(actual);
    }

    @Test(expectedExceptions = RegularExpressionEvaluationException.class)
    public void testCheckTargetShouldThrowExceptionWhenRegExpCompilationFailed() {
        //GIVEN
        given(patternFactory.createPattern(EX_REG_EXP)).willThrow(new PatternSyntaxException("", "", 0));
        //WHEN
        underTest.checkTarget(EX_REG_EXP, EX_TARGET);
        //THEN exception is thrown
    }

    @Test
    public void testCheckTargetShouldReturnFalseWhenRegExpDoesNotMatch() {
        //GIVEN
        String regExp = "asdf[a-z]";
        given(patternFactory.createPattern(regExp)).willReturn(Pattern.compile(regExp));
        //WHEN
        boolean actual = underTest.checkTarget(regExp, EX_TARGET);
        //THEN
        assertFalse(actual);
    }

    @Test
    public void testCheckTargetShouldReturnTrueWhenRegExpMatches() {
        //GIVEN
        given(patternFactory.createPattern(EX_REG_EXP)).willReturn(Pattern.compile(EX_REG_EXP));
        //WHEN
        boolean actual = underTest.checkTarget(EX_REG_EXP, EX_TARGET);
        //THEN
        assertTrue(actual);
    }

}
