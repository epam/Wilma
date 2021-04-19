package com.epam.gepard.common.threads.handler;

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

import com.epam.gepard.AllTestRunner;
import com.epam.gepard.util.ExitCode;

/**
 * Handler for the kill command.
 * @author Adam_Csaba_Kiraly
 */
public class KillCommandHandler {

    /**
     * Handles the specified command, killing Gepard itself.
     */
    public void handleCommand() {
        AllTestRunner.exitFromGepard(ExitCode.EXIT_CODE_BRUTE_FORCE);
    }
}
