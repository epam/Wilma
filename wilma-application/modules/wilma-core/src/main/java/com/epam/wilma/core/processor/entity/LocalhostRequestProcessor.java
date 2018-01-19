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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.domain.exception.ApplicationException;
import com.epam.wilma.domain.http.WilmaHttpEntity;
import com.epam.wilma.domain.http.WilmaHttpRequest;
import com.epam.wilma.router.LocalhostRequestRouter;
import com.epam.wilma.router.helper.LocalhostRequestChecker;

/**
 * Reroutes requests which are directed at localhost address, to the Wilma Servlet which will answer it.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class LocalhostRequestProcessor extends ProcessorBase {

    @Autowired
    private LocalhostRequestChecker localhostRequestChecker;
    @Autowired
    private LocalhostRequestRouter localhostRequestRouter;

    @Override
    public void process(final WilmaHttpEntity entity) throws ApplicationException {
        WilmaHttpRequest request = (WilmaHttpRequest) entity;
        /**
         * If this method was called then localhost blocking is turned on,
         * so we only need to check if the request targets localhost in order to determine if it needs to be rerouted.
         */
        boolean requestNeedsRerouting = localhostRequestChecker.checkIfRequestTargetsLocalhost(request);
        if (requestNeedsRerouting) {
            localhostRequestRouter.reroute(request);
            request.setRerouted(true);
        }
    }

}
