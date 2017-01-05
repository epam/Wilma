package com.epam.wilma.message.search.web.controller;

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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for handling requests for the application home page.
 * @author Tamas_Bihari
 *
 */
@Controller
public class HomeController {

    /**
     * Serves requests which arrive to home and sends back the home page.
     * @return the name of the jsp to display as result
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

}
