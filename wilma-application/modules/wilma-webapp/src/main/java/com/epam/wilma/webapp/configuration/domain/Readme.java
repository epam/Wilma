package com.epam.wilma.webapp.configuration.domain;
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

/**
 * Configuration values for wilma readme.
 * @author Tunde_Kovacs
 *
 */
public class Readme {

    private final String url;
    private final String text;

    /**
     * Constructs a new instance with the given parameters.
     * @param url the url of the readme
     * @param text the text of the url
     */
    public Readme(final String url, final String text) {
        super();
        this.url = url;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

}
