package com.epam.sandbox.templategenerator;

import com.epam.sandbox.common.SuperLogic;
import com.epam.wilma.domain.stubconfig.dialog.response.template.TemplateGenerator;


public class TestTemplateGeneratorJared implements TemplateGenerator {

    private SuperLogic superLogic = new SuperLogic();

    @Override
    public byte[] generateTemplate() {
        if (superLogic.getResult()) {
            return null;
        } else {
            return new byte[]{};
        }
    }

}