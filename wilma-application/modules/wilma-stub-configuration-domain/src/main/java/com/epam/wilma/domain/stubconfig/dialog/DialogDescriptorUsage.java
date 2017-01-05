package com.epam.wilma.domain.stubconfig.dialog;
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

/**
 * Defines the status of a {@link DialogDescriptor} in the stub configuration.
 * <ul>
 * <li> ALWAYS means this dialog descriptor is valid when Wilma is running </li>
 * <li> TIMEOUT means that when the dialog descriptor appears, after a timeout (specified in secs in validityvalue), the dialog descriptor will be disabled </li>
 * <li> HITCOUNT means that after this descriptor is applied N times (defined in validityvalue), the dialog descriptor will be disabled </li>
 * <li> DISABLED means that the dialog descriptor is not in use </li>
 * </ul>
 *
 * @author Tunde_Kovacs
 */
public enum DialogDescriptorUsage {
    ALWAYS,
    TIMEOUT,
    HITCOUNT,
    DISABLED;
}
