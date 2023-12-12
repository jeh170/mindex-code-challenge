package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure calculateReportingStructureForEmployee(String employeeId) {
        final Map<String, Employee> employeesById = employeeRepository
                .findAll().stream().collect(Collectors.toMap(Employee::getEmployeeId, Function.identity()));

        final Employee employee = employeesById.get(employeeId);

        final Employee employeeWithHydratedReports = hydrateReports(employee, employeesById);

        final int numberOfReports = calculateNumberOfReports(employeeWithHydratedReports);

        return new ReportingStructure(employeeWithHydratedReports, numberOfReports);
    }

    private Employee hydrateReports(Employee employee, Map<String, Employee> employeesById) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return employee;
        }

        final List<Employee> hydratedReports = employee.getDirectReports().stream()
                .map(report -> {
                    final Employee hydratedReport = employeesById.get(report.getEmployeeId());
                    return hydrateReports(hydratedReport, employeesById);
                }).collect(Collectors.toList());

        employee.setDirectReports(hydratedReports);

        return employee;
    }

    private int calculateNumberOfReports(Employee employee) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return 0;
        }

        return employee
                .getDirectReports()
                .stream()
                .reduce(
                        0,
                        (numberOfReports, report) -> numberOfReports + 1 + calculateNumberOfReports(report),
                        Integer::sum
                );
    }
}
