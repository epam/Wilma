package com.epam.gepard.common.helper;

/*==========================================================================
 Copyright 2004-2015 EPAM Systems

 This file is part of Gepard.

 Gepard is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Gepard is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Gepard.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================*/

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.epam.gepard.common.Environment;
import com.epam.gepard.common.TestClassExecutionData;
import com.epam.gepard.exception.ComplexGepardException;
import com.epam.gepard.generic.GenericListTestSuite;
import com.epam.gepard.util.FileUtil;

/**
 * Generates a file report in the case of test failure.
 * @author Zsolt Kiss Gere, Laszlo Toth, Tamas Godan, Tamas Kohegyi, Tibor Kovacs
 */
public class TestFailureReporter {
    private Environment environment;

    /**
     * Constructs a new instance of {@link TestFailureReporter}.
     * @param environment holds the properties of the application
     */
    public TestFailureReporter(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Generate the testlist-failure.txt file to help re-execution.
     */
    public void generateTestlistFailure() {
        Map<String, String> failedRows = collectFailedRows();
        //now build up the file
        String fileContent = concatenateRows(failedRows.values());
        writeContentToFile(fileContent);
    }

    private Map<String, String> collectFailedRows() {
        Iterator<String> i = GenericListTestSuite.getTestClassIds().iterator();
        Map<String, String> result = new LinkedHashMap<>();
        while (i.hasNext()) {
            TestClassExecutionData d = GenericListTestSuite.getTestClassExecutionData(i.next());
            if (d.getCountPassed() != d.getRunned()) {
                //this class should be in the list
                String row = d.getOriginalLine() + "\n";
                if (!result.containsKey(row)) {
                    result.put(row, row);
                }
            }
        }
        return result;
    }

    private String concatenateRows(final Collection<String> values) {
        Iterator<String> i = values.iterator();
        StringBuilder s = new StringBuilder();
        s.append("#Gepard generated testlist file about failures.\n");
        while (i.hasNext()) {
            s = s.append(i.next());
        }
        return s.toString();
    }

    private void writeContentToFile(final String content) {
        String path = environment.getProperty(Environment.GEPARD_TESTLIST_FAILURE_PATH);
        String fileName = environment.getProperty(Environment.GEPARD_TESTLIST_FAILURE_FILE);
        try {
            File f = new File(path + "/" + fileName);
            FileUtil fileUtil = new FileUtil();
            fileUtil.writeToFile(content, f);
        } catch (Exception e) {
            throw new ComplexGepardException("Error happened during the creation of list of failed test cases.", e, false, 0);
        }
    }
}
