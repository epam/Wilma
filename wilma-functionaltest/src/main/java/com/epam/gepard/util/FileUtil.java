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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.epam.gepard.AllTestRunner;

/**
 * Util class for manipulating Files and Folders.
 *
 * @author Laszlo Kishalmi, Tamas Kohegyi
 */
public class FileUtil {

    private static final int BUF_SIZE = 128 * 1024;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Copy streams.
     *
     * @param in  from where the file will be copied.
     * @param out to where the file will be copied.
     * @throws IOException in case of error.
     */
    public void copy(final InputStream in, final OutputStream out) throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        try {
            int read;
            while ((read = in.read(buf)) >= 0) {
                out.write(buf, 0, read);
            }
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
                LOG.debug("Copy: Closing InputStream was failed.");
            }
            try {
                out.close();
            } catch (IOException ignore) {
                LOG.debug("Copy: Closing OutputStream was failed.");
            }
        }
    }

    /**
     * Copy from stream to File.
     *
     * @param in  from where the file will be copied.
     * @param out to where the file will be copied.
     * @throws IOException in case of failure.
     */
    public void copy(final InputStream in, final File out) throws IOException {
        copy(in, new FileOutputStream(out));
    }

    /**
     * Copy from File to stream.
     *
     * @param in  from where the file will be copied.
     * @param out to where the file will be copied.
     * @throws IOException in case of failure.
     */
    public void copy(final File in, final File out) throws IOException {
        copy(new FileInputStream(in), new FileOutputStream(out));
    }

    /**
     * Write a string into File.
     *
     * @param st   to write.
     * @param file is the file to be filled.
     * @throws IOException in case of problem.
     */
    public void writeToFile(final String st, final File file) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        Writer w = new FileWriter(file);
        w.write(st);
        w.close();
    }

    /**
     * Write Document into File.
     *
     * @param doc  is the document to write.
     * @param file is the file to be filled.
     */
    public void writeToFile(final Document doc, final File file) {
        try { // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
            // Prepare the output file

            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            StreamResult result = new StreamResult(file);
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            xformer.transform(source, result);
        } catch (TransformerException ignore) {
            LOG.debug("WriteToFile from Document failed.");
        }
    }

    /**
     * Deletes all files and subdirectories under dir.
     *
     * @param dir to be deleted.
     * @return true if all deletions were successful. If a deletion fails, the method stops attempting to delete and returns false.
     */
    public boolean deleteDir(final File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        } // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * Create directory.
     *
     * @param dirPath Directory to create
     */
    public void createDirectory(final String dirPath) {
        String dir = formatPathName(dirPath);
        try {
            //Checking whether classpath exists or not
            FileProvider fileProvider = new FileProvider();
            File start = fileProvider.getFile(dir);
            if (!start.exists()) {
                //noinspection ResultOfMethodCallIgnored
                start.mkdirs();
            }
        } catch (Exception e) {
            AllTestRunner.CONSOLE_LOG.info("ERROR: Cannot create folder: " + dirPath, e);
            AllTestRunner.setExitCode(ExitCode.EXIT_CODE_CANNOT_CREATE_FOLDER);
        }
    }

    /**
     * Re-formats a path so that it contains only forward slashes,
     * and also removes double slashes.
     *
     * @param pathName is the path to be fixed.
     * @return with the corrected path.
     */
    public String formatPathName(final String pathName) {
        String path = pathName.replace('\\', '/');
        int index = 0;
        int pos;
        while ((pos = path.indexOf("//", index)) != -1) {
            path = path.substring(0, pos) + "/" + path.substring(pos + 2);
            index = pos;
        }
        return path;
    }

}
