package com.college.leavemanagement.entity;

import com.college.leavemanagement.enums.DeptSection;
import com.college.leavemanagement.enums.Year;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class HodDepartmentAssignment {

    @Enumerated(EnumType.STRING)
    private DeptSection deptSection;

    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(name = "assigned_year")
    private Year year;

    public HodDepartmentAssignment() {}

    public HodDepartmentAssignment(DeptSection deptSection, Year year) {
        this.deptSection = deptSection;
        this.year = year;
    }

    public static HodDepartmentAssignmentBuilder builder() {
        return new HodDepartmentAssignmentBuilder();
    }

    public DeptSection getDeptSection() { return deptSection; }
    public void setDeptSection(DeptSection deptSection) { this.deptSection = deptSection; }
    public void setDeptSection(String deptSection) { this.deptSection = DeptSection.fromString(deptSection); }

    public Year getYear() { return year; }
    public void setYear(Year year) { this.year = year; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HodDepartmentAssignment that = (HodDepartmentAssignment) o;
        return deptSection == that.deptSection && year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptSection, year);
    }

    public static class HodDepartmentAssignmentBuilder {
        private DeptSection deptSection;
        private Year year;

        HodDepartmentAssignmentBuilder() {}

        public HodDepartmentAssignmentBuilder deptSection(DeptSection deptSection) { this.deptSection = deptSection; return this; }
        public HodDepartmentAssignmentBuilder year(Year year) { this.year = year; return this; }

        public HodDepartmentAssignment build() {
            return new HodDepartmentAssignment(deptSection, year);
        }
    }
}
