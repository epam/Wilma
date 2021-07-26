package com.epam.wilma.mitmProxy.proxy.helper;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.DecompressingEntity;

/**
 * This class is originated from project: https://github.com/tkohegyi/mitmJavaProxy
 * @author Tamas_Kohegyi
 */

public class BrotliDecompressingEntity extends DecompressingEntity {

    public BrotliDecompressingEntity(final HttpEntity entity) {
            super(entity, BrotliInputStreamFactory.getInstance());
        }

}
