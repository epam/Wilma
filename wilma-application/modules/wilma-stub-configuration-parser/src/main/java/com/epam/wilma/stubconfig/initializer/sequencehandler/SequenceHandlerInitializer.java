package com.epam.wilma.stubconfig.initializer.sequencehandler;
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.sequence.SequenceHandler;
import com.epam.wilma.stubconfig.initializer.CommonClassInitializer;

/**
 * Loads an external sequence handler referenced in the StubConfig.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class SequenceHandlerInitializer extends CommonClassInitializer<SequenceHandler> {

    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private TemporaryStubResourceHolder stubResourceHolder;

    @Override
    protected String getPathOfExternalClasses() {
        return stubResourcePathProvider.getSequenceHandlerPathAsString();
    }

    @Override
    protected List<SequenceHandler> getExternalClassObjects() {
        return stubResourceHolder.getSequenceHandlers();
    }

    @Override
    protected void addExternalClassObject(final SequenceHandler externalClassObject) {
        stubResourceHolder.addSequenceHandler(externalClassObject);
    }

    @Override
    protected Class<SequenceHandler> getExternalClassType() {
        return SequenceHandler.class;
    }

}
