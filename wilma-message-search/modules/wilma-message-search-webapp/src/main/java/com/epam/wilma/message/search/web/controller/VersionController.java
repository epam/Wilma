package com.epam.wilma.message.search.web.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epam.wilma.message.search.web.support.VersionTitleProvider;

/**
 * Controller for version information.
 * @author Adam_Csaba_Kiraly
 *
 */
@Controller
public class VersionController {

    @Autowired
    private VersionTitleProvider titleProvider;

    /**
     * Returns the build version of the application as a JSON response.
     * @return the JSON response containing the build version
     */
    @ResponseBody
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity<String> getVersion() {
        String messageSearchVersion = titleProvider.getVersionTitle();
        String jsonData = "{\"messageSearchVersion\":\"" + messageSearchVersion + "\"}";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonData, responseHeaders, HttpStatus.CREATED);
    }
}
