package com.epam.wilma.stubconfig.dom.transformer;
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

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.epam.wilma.domain.stubconfig.exception.DocumentTransformationException;

/**
 * Class for transforming a {@link Document} to byte array.
 * @author Tamas_Bihari
 *
 */
@Component
public class DomBasedDocumentTransformer {

    /**
     * Transforms a {@link Document} to byte array.
     * @param document will be transformed
     * @return with the document as byte array
     * @throws DocumentTransformationException is thrown when {@link Document} can not be transformed
     */
    public byte[] transform(final Document document) throws DocumentTransformationException {
        ByteArrayOutputStream outputStream;
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            outputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new DocumentTransformationException("The actually used stub configuration XML can not be transformed. ", e);
        }
        return outputStream.toByteArray();
    }

    /**
     * Transforms a {@link Document} into a new File.
     * @param document will be transformed
     * @param path will be the path of the new file
     * @param actualStatus is the is the actual status of this stub descriptor
     * @throws TransformerException is thrown when unrecoverable error occurs during the course of the transformation
     */
    public void transformToFile(final Document document, final String path, final boolean actualStatus) throws TransformerException {
        Element root = document.getDocumentElement();
        root.setAttribute("active", String.valueOf(actualStatus));
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(path);
        transformer.transform(source, result);
    }
}
