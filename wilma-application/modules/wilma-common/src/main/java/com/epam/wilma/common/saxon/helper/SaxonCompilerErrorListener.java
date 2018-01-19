package com.epam.wilma.common.saxon.helper;
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

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Class for handling Saxon XML transformation errors. Writes warnings to log and throws errors.
 * @author Tamas_Bihari
 *
 */
@Component
public class SaxonCompilerErrorListener implements ErrorListener {
    private final Logger logger = LoggerFactory.getLogger(SaxonCompilerErrorListener.class);

    @Override
    public void warning(final TransformerException exception) throws TransformerException {
        logger.warn("Warning at XSLT transformation: " + exception.getMessageAndLocation());
    }

    @Override
    public void error(final TransformerException exception) throws TransformerException {
        throw exception;
    }

    @Override
    public void fatalError(final TransformerException exception) throws TransformerException {
        throw exception;
    }

}
