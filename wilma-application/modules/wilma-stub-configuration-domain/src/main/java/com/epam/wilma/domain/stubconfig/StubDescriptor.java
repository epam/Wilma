package com.epam.wilma.domain.stubconfig;
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

import java.util.Iterator;
import java.util.List;

import com.epam.wilma.domain.stubconfig.dialog.DialogDescriptor;
import com.epam.wilma.domain.stubconfig.interceptor.InterceptorDescriptor;
import com.epam.wilma.domain.stubconfig.sequence.SequenceDescriptor;

/**
 * The stub configuration contains several {@link DialogDescriptor}s that describe a request-response pair
 * - with what kind of response the stub should answer in case of a specific request.
 * It also contains interceptors that can be configured for the stub and cass process
 * requests and responses.
 * @author Marton_Sereg
 * @author Tunde_Kovacs
 * @author Balazs_Berkes
 */
public class StubDescriptor {

    private final StubDescriptorAttributes attributes;
    private final List<DialogDescriptor> dialogDescriptors;
    private final List<InterceptorDescriptor> interceptorDescriptors;
    private final List<SequenceDescriptor> sequenceDescriptors;

    /**
     * Constructs a new instance of {@link StubDescriptor}.
     * @param attributes includes the groupname attribute of stub configuration
     * @param dialogDescriptors the list of dialog descriptors defined in the stub configuration
     * @param interceptorDescriptors the list of request and response interceptors defined in the stub configuration
     * @param sequenceDescriptors the list of sequence descriptors defined in the stub configuration
     */
    public StubDescriptor(final StubDescriptorAttributes attributes, final List<DialogDescriptor> dialogDescriptors,
            final List<InterceptorDescriptor> interceptorDescriptors, final List<SequenceDescriptor> sequenceDescriptors) {
        super();
        this.attributes = attributes;
        this.dialogDescriptors = dialogDescriptors;
        this.interceptorDescriptors = interceptorDescriptors;
        this.sequenceDescriptors = sequenceDescriptors;
    }

    public List<SequenceDescriptor> getSequenceDescriptors() {
        return sequenceDescriptors;
    }

    public List<DialogDescriptor> getDialogDescriptors() {
        return dialogDescriptors;
    }

    /**
     * Adds a new dialog descriptor to the stub descriptor.
     * @param dialogDescriptor the new dialog descriptor that will be added
     * to the end of the list
     */
    public void addDialogDescriptor(final DialogDescriptor dialogDescriptor) {
        dialogDescriptors.add(dialogDescriptor);
    }

    /**
     * Removes a dialog descriptor from the stub configuration.
     * @param name the name of the dialog descriptor that will be removed from
     * the stub descriptor
     * @return true if removal was successful, false otherwise
     */
    public boolean removeDialogDescriptorWithName(final String name) {
        Iterator<DialogDescriptor> iterator = dialogDescriptors.iterator();
        boolean removed = false;
        while (iterator.hasNext() && !removed) {
            DialogDescriptor dialogDescriptor = iterator.next();
            if (dialogDescriptor.getAttributes().getName().equals(name)) {
                dialogDescriptors.remove(dialogDescriptor);
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Retrieves a dialog descriptor with a given name from the stub descriptor.
     * @param name the name of the dialog descriptor to be found
     * @return the dialog descriptor matching the <tt>name</tt>. Null if no dialog
     * descriptor with the given name was found.
     */
    public DialogDescriptor getDialogDescriptorWithName(final String name) {
        DialogDescriptor result = null;
        Iterator<DialogDescriptor> iterator = dialogDescriptors.iterator();
        boolean found = false;
        while (iterator.hasNext() && !found) {
            DialogDescriptor dialogDescriptor = iterator.next();
            if (dialogDescriptor.getAttributes().getName().equals(name)) {
                result = dialogDescriptor;
                found = true;
            }
        }
        return result;
    }

    public List<InterceptorDescriptor> getInterceptorDescriptors() {
        return interceptorDescriptors;
    }

    public StubDescriptorAttributes getAttributes() {
        return attributes;
    }

}
