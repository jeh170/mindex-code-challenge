package com.mindex.challenge.data;


import java.time.LocalDate;

public class Compensation {
    private Employee employee;
    private String salary;
    private LocalDate effectiveLocalDate;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveLocalDate;
    }

    public void setEffectiveDate(LocalDate effectiveLocalDate) {
        this.effectiveLocalDate = effectiveLocalDate;
    }
}
