package com.epam.wilma.core.processor.entity;
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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.Router;

/**
 * Processes a {@link WilmaHttpRequest} by rerouting it based on a route logic.
 * If an incoming request contains a wilma bypass string, then the request will not
 * be rerouted.
 * @author Tunde_Kovacs
 *
 */
@Component
public class RouterProcessor extends ProcessorBase {

    private static final String BYPASS_VALUE = "WilmaBypass=true";
    @Autowired
    private Router router;

    @Override
    public void process(final WilmaHttpEntity entity) throws ApplicationException {
        WilmaHttpRequest request = (WilmaHttpRequest) entity;
        boolean bypassWilma = isByPassPresentInHeader(request.getHeaders());
        if (!bypassWilma && !request.isRerouted()) {
            router.reroute(request);
        }
    }

    private boolean isByPassPresentInHeader(final Map<String, String> headers) {
        Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
        boolean found = false;
        while (iterator.hasNext() && !found) {
            Entry<String, String> entry = iterator.next();
            if (entry.getKey().contains(BYPASS_VALUE) || entry.getValue().contains(BYPASS_VALUE)) {
                found = true;
            }
        }
        return found;
    }
}
