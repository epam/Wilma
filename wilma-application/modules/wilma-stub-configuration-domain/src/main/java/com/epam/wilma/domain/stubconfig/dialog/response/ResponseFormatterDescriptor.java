package com.epam.wilma.domain.stubconfig.dialog.response;
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

import com.epam.wilma.domain.stubconfig.parameter.ParameterList;

/**
 * Holds response formatter details.
 * @author Tunde_Kovacs, Tamas_Kohegyo
 *
 */
public class ResponseFormatterDescriptor {

    private final ResponseFormatter responseFormatter;
    private final ParameterList params;

    /**
     * Constructs a new instance of template formatter.
     * @param responseFormatter the type of response formatter
     * @param params the parameters of the <tt>responseFormatter</tt>
     */
    public ResponseFormatterDescriptor(final ResponseFormatter responseFormatter, final ParameterList params) {
        super();
        this.responseFormatter = responseFormatter;
        this.params = params;
    }

    public ResponseFormatter getResponseFormatter() {
        return responseFormatter;
    }

    /**
     * Returns the {@link ResponseFormatter}'s parameters.
     * @return the cloned map
     */
    public ParameterList getParams() {
        return params;
    }

}
