package com.college.leavemanagement.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeptSection {
    CSBS,
    AIDS_A,
    AIDS_B,
    AIDS,
    CSE,
    IT,
    ECE,
    EEE,
    MECH,
    CIVIL,
    AIML,
    CYBER,
    DATA_SCIENCE,
    ROBOTICS,
    AUTOMOBILE,
    BIOTECH,
    BIOMEDICAL,
    AEROSPACE,
    CHEMICAL,
    MECHATRONICS,
    AGRICULTURE,
    OTHER;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static DeptSection fromString(String key) {
        if (key == null || key.isBlank()) return OTHER;
        String upper = key.trim().toUpperCase().replace("-", "_").replace(" ", "_");
        for (DeptSection b : DeptSection.values()) {
            if (b.name().equalsIgnoreCase(upper)) {
                return b;
            }
        }
        return OTHER;
    }
}
