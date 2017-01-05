package com.epam.wilma.maintainer.domain;
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

import org.springframework.stereotype.Component;

/**
 * Holds the count of deleted log files from the last iteration.
 * @author Tibor_Kovacs
 *
 */
@Component
public class DeletedFileProvider {
    private int deletedFilesCount;

    public int getDeletedFilesCount() {
        return deletedFilesCount;
    }

    public void setDeletedFilesCount(final int deletedFilesCount) {
        this.deletedFilesCount = deletedFilesCount;
    }
}
