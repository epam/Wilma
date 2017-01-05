package com.epam.wilma.message.search.lucene.index.scheduler;
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

import java.util.Date;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.wilma.message.search.lucene.configuration.LuceneConfigurationAccess;
import com.epam.wilma.message.search.lucene.configuration.PropertyDto;
import com.epam.wilma.message.search.lucene.index.scheduler.helper.CronTriggerFactory;
import com.epam.wilma.message.search.lucene.index.scheduler.helper.DateFactory;

/**
 * Unit test for {@link IndexTaskScheduler}.
 * @author Adam_Csaba_Kiraly
 *
 */
public class IndexTaskSchedulerTest {

    private static final String CRON_EXPRESSION = "* * * * * *";
    @Mock
    private ThreadPoolTaskScheduler taskScheduler;
    @Mock
    private LuceneConfigurationAccess configurationAccess;
    @Mock
    private CronTriggerFactory cronTriggerFactory;
    @Mock
    private DateFactory dateFactory;

    @Mock
    private ReindexerTask luceneReindexerTask;

    @InjectMocks
    private IndexTaskScheduler underTest;

    @Mock
    private PropertyDto properties;
    @Mock
    private CronTrigger cronTrigger;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartReindexScheduling() {
        //GIVEN
        given(configurationAccess.getProperties()).willReturn(properties);
        given(properties.getReindexTimer()).willReturn(CRON_EXPRESSION);
        given(cronTriggerFactory.create(CRON_EXPRESSION)).willReturn(cronTrigger);
        //WHEN
        underTest.startReindexScheduling();
        //THEN
        verify(taskScheduler).schedule(luceneReindexerTask, cronTrigger);
    }

    @Test
    public void testRunReindexOnDemand() {
        //GIVEN
        Date now = new Date();
        given(dateFactory.createNewDate()).willReturn(now);
        //WHEN
        underTest.runReindexOnDemand();
        //THEN
        verify(taskScheduler).schedule(luceneReindexerTask, now);
    }

}
