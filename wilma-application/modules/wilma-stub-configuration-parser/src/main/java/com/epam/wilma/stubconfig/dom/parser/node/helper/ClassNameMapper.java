package com.epam.wilma.stubconfig.dom.parser.node.helper;

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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.bcel.classfile.JavaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.wilma.common.helper.FileUtils;
import com.epam.wilma.common.helper.JavaClassFactory;

/**
 * Class for storing simple class name and full class name pairs.
 * Thread safe.
 * @author Adam_Csaba_Kiraly
 *
 */
@Component
public class ClassNameMapper {
    private static final String DOT = "\\.";
    private static final String EXCEPTION = "Failed to process class file: ";

    private final Logger logger = LoggerFactory.getLogger(ClassNameMapper.class);
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private JavaClassFactory javaClassFactory;
    private final Map<String, String> simpleNameToFullName;

    /**
     * Constructs an empty ClassNameMapper object.
     */
    public ClassNameMapper() {
        simpleNameToFullName = new ConcurrentHashMap<String, String>();
    }

    /**
     * Initializes the simple class name, full class name pairs for class files that exist in Wilma at startup.
     * @param everyPathAsString list of paths where class files can be found
     */
    public void initialize(final List<String> everyPathAsString) {
        for (String resourcePath : everyPathAsString) {
            addClassNamesFromFolder(resourcePath);
        }
    }

    private void addClassNamesFromFolder(final String folderPath) {
        File folder = new File(folderPath);
        Collection<File> filesInFolder = fileUtils.listFiles(folder);
        for (File file : filesInFolder) {
            String fileName = file.getName();
            if (fileName.endsWith(".class")) {
                JavaClass javaClass;
                try {
                    javaClass = javaClassFactory.createJavaClass(file);
                    String simpleClassName = fileName.split(DOT)[0];
                    String fullClassName = javaClass.getClassName();
                    put(simpleClassName, fullClassName);
                } catch (Exception e) {
                    logger.info(EXCEPTION + file.getPath(), e);
                }
            }
        }
    }

    /**
     * Stores a full class name with the specified simple class as key.
     * @param simpleClassName key
     * @param fullClassName value
     */
    public void put(final String simpleClassName, final String fullClassName) {
        simpleNameToFullName.put(simpleClassName, fullClassName);
    }

    /**
     * Gets the full class name that belongs to the given simple class name.
     * @param simpleClassName key
     * @return full class name
     */
    public String get(final String simpleClassName) {
        String fullClassName = simpleNameToFullName.get(simpleClassName);
        if (fullClassName == null) {
            fullClassName = simpleClassName;
        }
        return fullClassName;
    }
}
