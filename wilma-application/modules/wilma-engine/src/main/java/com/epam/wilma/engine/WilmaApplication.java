package com.epam.wilma.engine;
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

import com.epam.wilma.engine.bootstrap.WilmaBootstrap;
import com.epam.wilma.engine.bootstrap.helper.ApplicationContextFactory;

/**
 * Starts the Wilma application.
 * @author Marton_Sereg
 *
 */
public final class WilmaApplication {

    public static String[] arguments; //NOSONAR

    private WilmaApplication() {
    }

    /**
     * The proxy can be started by running this method.
     * @param args The program needs the path of wilma.conf.properties to run.
     */
    public static void main(final String[] args) {
        arguments = args; //NOSONAR
        new WilmaBootstrap(ApplicationContextFactory.getInstance()).bootstrap();
    }

}
