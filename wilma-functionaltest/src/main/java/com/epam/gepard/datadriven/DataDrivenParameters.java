package com.epam.gepard.datadriven;

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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class holds the data driven parameters for a test case.
 */
public class DataDrivenParameters {
    private String[] parameters; //the loaded parameters
    private String[] parameterNames; //names of the parameter columns
    private Map<String, String> paramMap;

    /**
     * Initiates a new parameter array for a data driven TC execution.
     *
     * @param parameters is the array of parameters
     * @param parameterEnum is the names of the parameters (i.e. column names)
     */
    @SuppressWarnings("rawtypes")
    public DataDrivenParameters(final String[] parameters, final Class<? extends Enum> parameterEnum) {
        this.parameters = parameters;
        String[] parameterNames;
        if (parameterEnum != null) {
            Enum[] e = parameterEnum.getEnumConstants();
            parameterNames = new String[e.length];
            for (Enum enumName : e) {
                parameterNames[enumName.ordinal()] = enumName.toString();
            }
        } else {
            parameterNames = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameterNames[i] = "PAR" + i;
            }
        }
        setParameterNames(parameterNames);
    }

    /**
     * Initiates a new parameter array for a data driven TC execution.
     *
     * @param parameters is the array of parameters
     */
    public DataDrivenParameters(final String[] parameters) {
        this.parameters = parameters;
        setParameterNames(null);
    }

    /**
     * Initiates a new parameter array for a data driven TC execution.
     *
     * @param parameters is the array of parameters
     * @param parameterNames is the names of the parameters (i.e. column names)
     */
    public DataDrivenParameters(final String[] parameters, final String[] parameterNames) {
        this.parameters = parameters;
        setParameterNames(parameterNames);
    }

    /**
     * Initiates a new parameter array for a data driven TC execution.
     *
     * @param data is the original data driver parameter, to be cloned
     */
    public DataDrivenParameters(final DataDrivenParameters data) {
        this.parameters = data.parameters;
        setParameterNames(data.parameterNames);
    }

    /**
     * Sets the names for the parameters. In case no name is given, but parameters we have,
     * the generated name will be PARx (x = 0,1,2 ...)
     * @param parameterNames is the names to be used for the parameters.
     */
    public void setParameterNames(final String[] parameterNames) {
        if (parameterNames == null) {
            //let the parameter names generated...
            if (parameters == null) {
                this.parameterNames = null;
                this.paramMap = null;
                return;
            }
            //we have parameters, so we can name them
            nameTheParameters();
        } else {
            //need to set up valid names
            this.parameterNames = parameterNames;
        }
        //now we have the local parameterNames, finally generate the map
        validateValues();
        generateMap();
    }

    private void nameTheParameters() {
        String[] s = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            s[i] = "PAR" + Integer.toString(i);
        }
        this.parameterNames = s;
    }

    private void validateValues() {
        if (this.parameterNames == null) {
            throw new IllegalArgumentException("Names of the parameters is empty.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter array is empty.");
        }
        if (parameters.length != this.parameterNames.length) {
            throw new IllegalArgumentException("Parameter Name array and parameters have different size.");
        }
    }

    private void generateMap() {
        paramMap = new LinkedHashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            paramMap.put(this.parameterNames[i], parameters[i]);
        }
    }

    /**
     * Get the value of a data parameter. Do NOT use it, use the same method with Enum parameter.
     * @param parameterName is the name of the parameter
     * @return with the value
     */
    public String getTestParameter(final String parameterName) {
        if (paramMap == null) {
            throw new IllegalArgumentException("No parameter map is created, unable to lookup the parameter.");
        }
        return paramMap.get(parameterName);
    }

    /**
     * Gets the name of the Nth parameter.
     * @param ordinal is the N.
     * @return with the name of the specified parameter.
     */
    public String getParameterName(final int ordinal) {
        if (paramMap == null) {
            throw new IllegalArgumentException("No parameter map is created, unable to lookup the parameter.");
        }
        if (paramMap.size() < ordinal) {
            throw new IllegalArgumentException("Ordinal value is too high.");
        }
        Iterator i = paramMap.keySet().iterator();
        int counter = ordinal + 1;
        String name = null;
        while (counter > 0) {
            name = i.next().toString();
            counter--;
        }
        return name;
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
}
