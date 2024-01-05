package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.mindex.challenge.assertions.DataAssertions.assertCompensationEquivalence;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    @Autowired
    private CompensationServiceImpl compensationService;

    @Autowired
    private CompensationRepository compensationRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    final private LocalDate TODAY = LocalDate.of(2023, 12, 19);

    @Before
    public void clearDb() {
        compensationRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void testCreateMethod() {
        // Given a Compensation with employee and date
        Employee employee = new Employee();

        employee.setEmployeeId("1");

        final Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployee(employee);
        expectedCompensation.setSalary("$100,000");
        expectedCompensation.setEffectiveDate(TODAY);

        compensationService.create(expectedCompensation);

        assertNotNull(compensationRepository.findByEmployeeId("1"));
        assertCompensationEquivalence(expectedCompensation, compensationRepository.findByEmployeeId("1"));
    }

    @Test
    public void testReadMethod() {
        // Given an Employee with employee data is in the database
        Employee storedEmployee = new Employee();
        storedEmployee.setEmployeeId("1");
        storedEmployee.setFirstName("James");
        storedEmployee.setLastName("Hantz");
        storedEmployee.setDepartment("Engineering");
        storedEmployee.setPosition("Software Engineer");

        when(employeeRepository.findByEmployeeId("1")).thenReturn(storedEmployee);

        // Expect that the employee will be hydrated as part of the service call
        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployee(storedEmployee);
        expectedCompensation.setSalary("$100,000");
        expectedCompensation.setEffectiveDate(TODAY);

        // Given a Compensation for the employee is in the database
        Employee compensationEmployee = new Employee();

        compensationEmployee.setEmployeeId("1");

        final Compensation storedCompensation = new Compensation();
        storedCompensation.setEmployee(compensationEmployee);
        storedCompensation.setSalary("$100,000");
        storedCompensation.setEffectiveDate(TODAY);

        compensationRepository.insert(storedCompensation);

        Compensation actualCompensation = compensationService.readByEmployeeId("1");

        assertCompensationEquivalence(actualCompensation, expectedCompensation);
    }
}
