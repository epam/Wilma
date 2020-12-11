package com.epam.wilma.message.search.engine.initalize;

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

import com.epam.wilma.message.search.configuration.ConfigurationAccessBase;
import com.epam.wilma.message.search.engine.bootstrap.StartUpMessageGenerator;
import com.epam.wilma.message.search.engine.properties.PropertyLoader;
import com.epam.wilma.message.search.lucene.index.scheduler.IndexTaskScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the class {@link ConfigurationInitializer}.
 *
 * @author Tunde_Kovacs
 */
public class ConfigurationInitializerTest {

    @Mock
    private PropertyLoader propertyLoader;
    @Mock
    private StartUpMessageGenerator startUpMessageGenerator;
    @Mock
    private JmsConnectionConfigurer jmsConnectionConfigurer;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private ConfigurationAccessBase configAccess;

    @InjectMocks
    private ConfigurationInitializer underTest;

    @Mock
    private IndexTaskScheduler indexTaskScheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(applicationContext.getBean("indexTaskScheduler", IndexTaskScheduler.class)).willReturn(indexTaskScheduler);
        Whitebox.setInternalState(underTest, "configurationAccesses", new ArrayList<ConfigurationAccessBase>());
    }

    @Test
    public final void testAfterPropertiesSetShouldLoadProperties() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(propertyLoader).loadProperties();
    }

    @Test
    public final void testAfterPropertiesCallStartUpMessage() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(startUpMessageGenerator).logStartUpMessage();
    }

    @Test
    public final void testAfterPropertiesCallJmsConnectionConfigurer() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(jmsConnectionConfigurer).setBrokerUrl();
    }

    @Test
    public final void testAfterPropertiesScheduleReindexing() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(indexTaskScheduler).startReindexScheduling();
    }

    @Test
    public final void testAfterPropertiesShouldBuildTheIndex() throws Exception {
        //GIVEN in setup
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(indexTaskScheduler).runReindexOnDemand();
    }

    @Test
    public final void testAfterPropertiesShouldCallCOnfigurationAccessLoadProperties() throws Exception {
        //GIVEN
        List<ConfigurationAccessBase> configAccesses = new ArrayList<>();
        configAccesses.add(configAccess);
        Whitebox.setInternalState(underTest, "configurationAccesses", configAccesses);
        //WHEN
        underTest.afterPropertiesSet();
        //THEN
        verify(configAccess).loadProperties();
    }
}
