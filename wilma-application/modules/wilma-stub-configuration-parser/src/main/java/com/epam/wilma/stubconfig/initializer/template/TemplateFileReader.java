package com.epam.wilma.stubconfig.initializer.template;
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
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileFactory;
import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.domain.stubconfig.StubResourcePathProvider;
import com.epam.wilma.domain.stubconfig.TemporaryStubResourceHolder;
import com.epam.wilma.domain.stubconfig.exception.DescriptorValidationFailedException;

/**
 * Class for reading template files from file system.
 * @author Tamas_Bihari
 *
 */
@Component
public class TemplateFileReader {

    @Autowired
    private FileFactory fileFactory;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private StubResourcePathProvider stubResourcePathProvider;
    @Autowired
    private TemporaryStubResourceHolder resourceHolder;

    /**
     * Read the specified template file from file system.
     * Throws StubConfigurationException if the template can not be read.
     * @param templatesName is the name of the template resource
     * @return with file as byte array
     */
    public byte[] readTemplate(final String templatesName) {
        byte[] result;
        Map<String, byte[]> templatesMap = resourceHolder.getTemplates();
        if (templatesMap.containsKey(templatesName)) {
            result = templatesMap.get(templatesName);
        } else {
            result = readTemplateFromFileSystem(templatesName);
            resourceHolder.addTemplate(templatesName, result);
        }
        return result;
    }

    private byte[] readTemplateFromFileSystem(final String templatesName) {
        String templatePath = (stubResourcePathProvider.getTemplatesPathAsString() + "/" + templatesName).replace("\\", "/");
        File templateFile = fileFactory.createFile(templatePath);
        byte[] result = null;
        try {
            result = fileUtils.getFileAsByteArray(templateFile);
        } catch (IOException e) {
            throw new DescriptorValidationFailedException("Validation of stub descriptor failed - Template file '" + templatePath + "' not found.", e);
        }
        return result;
    }
}
