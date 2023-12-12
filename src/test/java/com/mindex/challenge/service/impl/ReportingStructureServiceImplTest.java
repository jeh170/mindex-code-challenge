package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ReportingStructureServiceImplTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
  @Autowired
  private ReportingStructureService reportingStructureService;

  @MockBean
  private EmployeeRepository employeeRepository;

  @Test
  public void testWithNoReports() {
    Employee employee1 = new Employee();
    employee1.setEmployeeId("1");
    employee1.setFirstName("John");
    employee1.setLastName("Doe");
    employee1.setDepartment("Engineering");
    employee1.setPosition("Developer");

    Employee employee2 = new Employee();
    employee2.setEmployeeId("2");
    employee2.setFirstName("Jane");
    employee2.setLastName("Doe");
    employee2.setDepartment("Engineering");
    employee2.setPosition("Developer");

    Mockito.when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

    ReportingStructure reportingStructure = reportingStructureService.calculateReportingStructureForEmployee("2");

    assertThat(reportingStructure.getNumberOfReports()).isEqualTo(0);
  }

  @Test
  public void testWithOnlyDirectReports() {
    Employee employee1 = new Employee();
    employee1.setEmployeeId("1");
    employee1.setFirstName("John");
    employee1.setLastName("Doe");
    employee1.setDepartment("Engineering");
    employee1.setPosition("Developer");

    Employee employee2 = new Employee();
    employee2.setEmployeeId("2");
    employee2.setFirstName("Jane");
    employee2.setLastName("Doe");
    employee2.setDepartment("Engineering");
    employee2.setPosition("Developer");

    Employee manager1 = new Employee();
    manager1.setEmployeeId("3");
    manager1.setFirstName("Joseph");
    manager1.setLastName("Petrovich");
    manager1.setDepartment("Engineering");
    manager1.setPosition("Manager");
    manager1.setDirectReports(createDirectReportList(employee1, employee2));

    Mockito.when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2, manager1));

    ReportingStructure reportingStructure = reportingStructureService.calculateReportingStructureForEmployee("3");

    assertThat(reportingStructure.getNumberOfReports()).isEqualTo(2);
  }

  @Test
  public void testWithChainOfReports() {
    Employee employee1 = new Employee();
    employee1.setEmployeeId("1");
    employee1.setFirstName("John");
    employee1.setLastName("Doe");
    employee1.setDepartment("Engineering");
    employee1.setPosition("Developer");

    Employee employee2 = new Employee();
    employee2.setEmployeeId("2");
    employee2.setFirstName("Jane");
    employee2.setLastName("Doe");
    employee2.setDepartment("Engineering");
    employee2.setPosition("Developer");

    Employee manager1 = new Employee();
    manager1.setEmployeeId("3");
    manager1.setFirstName("Joseph");
    manager1.setLastName("Petrovich");
    manager1.setDepartment("Engineering");
    manager1.setPosition("Manager");
    manager1.setDirectReports(createDirectReportList(employee1, employee2));

    Employee vp1 = new Employee();
    vp1.setEmployeeId("4");
    vp1.setFirstName("Big");
    vp1.setLastName("Boss");
    vp1.setDepartment("Engineering");
    vp1.setPosition("Vice President");
    vp1.setDirectReports(createDirectReportList(manager1));

    Mockito.when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2, manager1, vp1));

    ReportingStructure reportingStructure = reportingStructureService.calculateReportingStructureForEmployee("4");

    assertThat(reportingStructure.getNumberOfReports()).isEqualTo(3);
  }

  // Used to fill in the direct reports list accurately to how they come out of the repository
  private List<Employee> createDirectReportList(Employee ...employees) {
    return Arrays.stream(employees).map(employee -> {
      final Employee employeeIdOnly = new Employee();
      employeeIdOnly.setEmployeeId(employee.getEmployeeId());

      return employeeIdOnly;
    }).collect(Collectors.toList());
  }
}