package com.epam.wilma.message.search.lucene.index.helper;
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

public class FileWrapper {
    private final File file;
    private final boolean isDirectory;
    private final boolean exists;
    private final String[] list;
    private final boolean canRead;
    private final String absolutePath;
    private final long lastModified;


    public FileWrapper(String path) {
        this.file = new File(path);
        this.isDirectory = this.file.isDirectory();
        this.exists = this.file.exists();
        this.list = this.file.list();
        this.canRead = this.file.canRead();
        this.absolutePath = this.file.getAbsolutePath();
        this.lastModified = this.file.lastModified();
    }

    public FileWrapper(File file, String child) {
        this.file = new File(file, child);
        this.isDirectory = this.file.isDirectory();
        this.exists = this.file.exists();
        this.list = this.file.list();
        this.canRead = this.file.canRead();
        this.absolutePath = this.file.getAbsolutePath();
        this.lastModified = this.file.lastModified();
    }

    public boolean canRead() {
        return canRead;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean exists() {
        return exists;
    }

    public String[] list() {
        return list;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public File getFile() {
        return file;
    }

    public long lastModified() {
        return lastModified;
    }
}
