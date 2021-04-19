package com.epam.gepard.util;

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to hold general purpose help routines.
 */
public class Util {

    private static final String VERSION_NOT_FOUND = "unknown (no manifest file)";
    private PackageProvider packageProvider;

    /**
     * This is the constructor of the general purpose util class for Gepard.
     */
    public Util() {
        packageProvider = new PackageProvider();
    }

    // =================================  Exception helper methods.  ================================

    /**
     * Method getStackTrace : returns the stack trace of a throwable object.
     * @param t the throwable object.
     * @return String the stack trace of the throwable object.
     */
    public static String getStackTrace(final Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        return stringWriter.toString();
    }

    /**
     * Escape text so that it can be viewed in an HTML page.
     * @param text is the text that should be escaped
     * @return with a html safe text
     */
    public String escapeHTML(final String text) {
        if (text == null) {
            return "<null>";
        }
        StringBuilder sb = new StringBuilder(text.length() * 2);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
            case '\"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            default:
                sb.append(c);
            }
        }
        //with escaped string we return
        return sb.toString();
    }

    /**
     * Returns the title and version of Gepard tool.
     * @return the version title if the manifest is found
     */
    public String getGepardVersion() {
        Package pack = packageProvider.getPackageOfObject(this);
        String implementationTitle = pack.getImplementationTitle();
        if (implementationTitle != null) {
            return implementationTitle;
        }
        return VERSION_NOT_FOUND;
    }

    /**
     * Makes the given text red and bold in HTML.
     * @param text the given text
     * @return a red and bold text in HTML format
     */
    public String alertText(final String text) {
        return String.format("<font color=\"#AA0000\"><b>%s</b></font>", text);
    }

    /**
     * Parse the matching data (1st group in regexp) from the text.
     *
     * @param text containing the data
     * @param regexp to lookup
     * @return with the matching data
     */
    public String parseData(String text, String regexp) {
        String retval = null;

        if (text != null) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                retval = matcher.group(1);
            }
        }

        return retval;
    }

}
