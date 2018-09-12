package com.epam.wilma.domain.stubconfig;
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

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Provides the path of the stub configuration resources.
 * @author Tunde_Kovacs
 * @author Tamas Kohegyi
 */
@Component
public class StubResourcePathProvider {

    private String templatesPath;
    private String conditionCheckerPath;
    private String responseFormattersPath;
    private String interceptorPath;
    private String jarPath;
    private String cachePath;
    private String sequenceHandlerPath;

    public String getTemplatesPathAsString() {
        return templatesPath;
    }

    public Path getTemplatesPath() {
        return FileSystems.getDefault().getPath(templatesPath);
    }

    public String getConditionCheckerPathAsString() {
        return conditionCheckerPath;
    }

    public Path getConditionCheckerPath() {
        return FileSystems.getDefault().getPath(conditionCheckerPath);
    }

    public String getResponseFormattersPathAsString() {
        return responseFormattersPath;
    }

    public Path getResponseFormatterPath() {
        return FileSystems.getDefault().getPath(responseFormattersPath);
    }

    public String getInterceptorPathAsString() {
        return interceptorPath;
    }

    public Path getInterceptorPath() {
        return FileSystems.getDefault().getPath(interceptorPath);
    }

    public Path getJarPath() {
        return FileSystems.getDefault().getPath(jarPath);
    }

    public String getJarPathAsString() {
        return jarPath;
    }

    public String getCachePath() {
        return cachePath;
    }

    public Path getSequenceHandlerPath() {
        return FileSystems.getDefault().getPath(sequenceHandlerPath);
    }

    public String getSequenceHandlerPathAsString() {
        return sequenceHandlerPath;
    }

    /**
     * Returns every stub resource path as a list of Strings.
     * @return the stub resource paths
     */
    public List<String> getEveryPathAsString() {
        List<String> result = new ArrayList<>();
        result.add(getConditionCheckerPathAsString());
        result.add(getInterceptorPathAsString());
        result.add(getResponseFormattersPathAsString());
        result.add(getTemplatesPathAsString());
        result.add(getJarPathAsString());
        result.add(getSequenceHandlerPathAsString());
        return result;
    }

    public void setTemplatesPath(final String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public void setConditionCheckerPath(final String conditionCheckerPath) {
        this.conditionCheckerPath = conditionCheckerPath;
    }

    public void setResponseFormatterPath(final String responseFormattersPath) {
        this.responseFormattersPath = responseFormattersPath;
    }

    public void setInterceptorPath(final String interceptorPath) {
        this.interceptorPath = interceptorPath;
    }

    public void setJarPath(final String jarPath) {
        this.jarPath = jarPath;
    }

    public void setSequenceHandlerPath(final String sequenceHandlerPath) {
        this.sequenceHandlerPath = sequenceHandlerPath;
    }

    public void setCachePath(final String cachePath) {
        this.cachePath = cachePath;
    }
}
