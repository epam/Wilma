package com.epam.wilma.message.search.lucene.index;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epam.wilma.message.search.domain.exception.SystemException;
import com.epam.wilma.message.search.lucene.helper.TermFactory;
import com.epam.wilma.message.search.lucene.index.helper.BufferedReaderFactory;
import com.epam.wilma.message.search.lucene.index.helper.DocumentFactory;
import com.epam.wilma.message.search.lucene.index.helper.FileInputStreamFactory;

/**
 * Class for adding files to Lucene index using {@link IndexWriter}.
 * @author Tamas_Bihari
 *
 */
@Component
public class FileIndexer {

    private final Logger logger = LoggerFactory.getLogger(FileIndexer.class);

    @Value("#{fieldName}")
    private String fieldName;
    @Autowired
    private IndexWriter indexWriter;
    @Autowired
    private DocumentFactory documentFactory;
    @Autowired
    private BufferedReaderFactory bufferedReaderFactory;
    @Autowired
    private TermFactory termFactory;
    @Autowired
    private FileInputStreamFactory fileInputStreamFactory;

    /**
     * Adds a file to index with {@link IndexWriter}.
     * @param file will be indexed by the function
     */
    public void indexFile(final File file) {
        FileInputStream fis = getInputStream(file);
        Document doc = documentFactory.createDocument();
        // Add the path of the file as a field named "path". Use a field that is indexed (i.e. searchable), but don't tokenize
        // the field into separate words and don't index term frequency or positional information:
        Field pathField = new StringField(fieldName, file.getAbsolutePath(), Field.Store.YES);
        doc.add(pathField);

        // Add the last modified date of the file a field named "modified".
        // Use a LongField that is indexed (i.e. efficiently filterable with NumericRangeFilter).
        doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));

        // Add the contents of the file to a field named "contents".
        // If that's not the case searching for special characters will fail.
        BufferedReader bufferedReader;
        try {
            bufferedReader = bufferedReaderFactory.createReader(fis);
            doc.add(new Field("contents", bufferedReader, TextField.TYPE_NOT_STORED));
            addDocument(file, doc);
            fis.close();
        } catch (IOException e) {
            logger.error(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    private FileInputStream getInputStream(final File file) {
        FileInputStream fis;
        try {
            fis = fileInputStreamFactory.createFileInputStream(file);
        } catch (FileNotFoundException fnfe) {
            throw new SystemException(fnfe.getMessage());
        }
        return fis;
    }

    private void addDocument(final File file, final Document doc) throws IOException {
        if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
            indexWriter.addDocument(doc);
        } else {
            Term term = termFactory.createTerm(fieldName, file.getPath());
            indexWriter.updateDocument(term, doc);
        }
    }

}
