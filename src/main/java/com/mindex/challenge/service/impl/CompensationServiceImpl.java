package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {
    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation readByEmployeeId(String employeeID) {
        final Compensation compensation = compensationRepository.findByEmployeeId(employeeID);

        final Employee hydratedEmployee = employeeRepository.findByEmployeeId(employeeID);

        compensation.setEmployee(hydratedEmployee);

        return compensation;
    }

    @Override
    public Compensation create(Compensation compensation) {
        return compensationRepository.insert(compensation);
    }
}
