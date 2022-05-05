package com.company.collections.changeAPI.information.find;

import com.company.collections.changeAPI.information.ChangeInformation;

public abstract class FindBase extends ChangeInformation<Object> {

    // ====================================
    //               FIELDS
    // ====================================

    protected final Object[] toFind;

    // ====================================
    //             CONSTRUCTOR
    // ====================================

    public FindBase(
            final Object[] toFind
    ) {
        this.toFind = toFind;
    }

    // ====================================
    //             ACCESSORS
    // ====================================

    public Object[] getToFind() {
        return toFind;
    }
}
