// Copyright (c) 2026 just.meh.apps@gmail.com
// SPDX-License-Identifier: MIT

package just.meh.apps.utils;

public enum Scale {
    INTEGER(0),
    CURRENCY(2),
    PERCENTAGE(4),
    DEFAULT(32);

    private final int value;

    Scale(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
