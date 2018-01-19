package com.epam.wilma.sequence.maintainer;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.sequence.SequenceManager;

/**
 * This task is scheduled as defined in wilma.conf.properties.
 * This task starts cleaning process of {@link SequenceManager}.
 * @author Tibor_Kovacs
 *
 */
@Component
public class SequenceCleanerTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(SequenceCleanerTask.class);
    @Autowired
    private SequenceManager manager;

    @Override
    public void run() {
        manager.cleanUpDescriptors();
        logger.debug("Clean process has ran.");
    }

}
