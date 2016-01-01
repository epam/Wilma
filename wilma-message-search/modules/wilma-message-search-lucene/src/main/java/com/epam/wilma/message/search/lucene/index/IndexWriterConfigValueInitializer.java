package com.epam.wilma.message.search.lucene.index;

/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Initializes {@link IndexWriterConfig} values.
 * @author Tamas_Bihari
 *
 */
public class IndexWriterConfigValueInitializer {
    private static final Double WRITER_BUFFER_SIZE = 1500.0;

    private final IndexWriterConfig config;

    /**
     * Creates a {@link IndexWriterConfigValueInitializer} and initializes the given config values.
     * @param config is the {@link IndexWriterConfig} instance
     */
    @Autowired
    public IndexWriterConfigValueInitializer(final IndexWriterConfig config) {
        this.config = config;
        initConfigValues();
    }

    private void initConfigValues() {
        config.setOpenMode(OpenMode.CREATE);
        // Optional: for better indexing performance, increase the max heap size to the JVM (eg add -Xmxm or -Xmx1g)
        config.setRAMBufferSizeMB(WRITER_BUFFER_SIZE);
    }
}
