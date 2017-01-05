package com.epam.wilma.indexing.jms.helper;
/*==========================================================================
Copyright 2013-2017 EPAM Systems

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

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.indexing.domain.IndexMessage;

/**
 * Unit tests for the class {@link JmsIndexMessageCreator}.
 * @author Tunde_Kovacs
 *
 */
public class JmsIndexMessageCreatorTest {

    @Mock
    private IndexMessage indexMessage;
    @Mock
    private Session session;
    @Mock
    private ObjectMessage objectMessage;

    @InjectMocks
    private JmsIndexMessageCreator underTest;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateMessageShouldSetRequestOnMessage() throws JMSException {
        //GIVEN
        given(session.createObjectMessage()).willReturn(objectMessage);
        //WHEN
        underTest.createMessage(session);
        //THEN
        verify(objectMessage).setObject(indexMessage);
    }
}
