package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation readByEmployeeId(String employeeID);

    Compensation create(Compensation compensation);
}
