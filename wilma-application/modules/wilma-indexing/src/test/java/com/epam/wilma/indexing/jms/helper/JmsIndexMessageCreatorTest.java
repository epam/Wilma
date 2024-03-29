package com.epam.wilma.indexing.jms.helper;
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

import com.epam.wilma.indexing.domain.IndexMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link JmsIndexMessageCreator}.
 *
 * @author Tunde_Kovacs
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
