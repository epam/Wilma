package com.epam.wilma.mitmProxy.proxy.helper;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.DecompressingEntity;

public class BrotliDecompressingEntity extends DecompressingEntity {

    public BrotliDecompressingEntity(final HttpEntity entity) {
            super(entity, BrotliInputStreamFactory.getInstance());
        }

    }
