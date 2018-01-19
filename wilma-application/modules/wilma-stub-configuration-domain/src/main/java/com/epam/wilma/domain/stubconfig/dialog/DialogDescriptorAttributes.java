package com.epam.wilma.domain.stubconfig.dialog;
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

/**
 * Represents the attributes needed by a {@link DialogDescriptor}.
 * @author Tunde_Kovacs
 *
 */
public class DialogDescriptorAttributes {

    private final String name;
    private DialogDescriptorUsage usage;
    private long hitcount;
    private long timeout;
    private String comment;

    /**
     * Constructs a new instance of dialog descriptor attributes.
     * @param name the unique name of the dialog descriptor
     * @param usage the type of {@link DialogDescriptorUsage}
     */
    public DialogDescriptorAttributes(final String name, final DialogDescriptorUsage usage) {
        super();
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public DialogDescriptorUsage getUsage() {
        return usage;
    }

    public void setUsage(final DialogDescriptorUsage usage) {
        this.usage = usage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public long getHitcount() {
        return hitcount;
    }

    public void setHitcount(final long hitcount) {
        this.hitcount = hitcount;
    }

    /**
     * Decreases the hit count of the dialog descriptor with one.
     * @return the decreased validity value
     */
    public long decreaseHitcount() {
        return --hitcount;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

}
