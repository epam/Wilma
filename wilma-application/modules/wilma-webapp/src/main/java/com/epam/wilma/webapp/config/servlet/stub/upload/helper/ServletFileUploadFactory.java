package com.epam.wilma.webapp.config.servlet.stub.upload.helper;
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

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;

/**
 * Factory for {@link ServletFileUpload} class.
 * @author Tamas_Bihari
 *
 */
@Component
public class ServletFileUploadFactory {

    /**
     * Creates a new {@link ServletFileUpload} instance.
     * @return with the new instance.
     */
    public ServletFileUpload createInstance() {
        FileItemFactory factory = new DiskFileItemFactory();
        return new ServletFileUpload(factory);
    }
}
