package com.epam.wilma.maintainer.task.helper;
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

import java.io.File;
import java.io.FileFilter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * File filter that returns true for request and response files.
 * @author Marton_Sereg
 *
 */
@Component
@Qualifier("message")
public class MessageFileFilter implements FileFilter {

    private static final String FILE_NAME_EXP = "[0-9]{14}.[0-9]{4}re(q|sp)(FI)?.txt";

    @Override
    public boolean accept(final File file) {
        return file.isFile() && isMatchWithRegExp(file);
    }

    boolean isMatchWithRegExp(final File file) {
        return file.getName().matches(FILE_NAME_EXP);
    }
}
