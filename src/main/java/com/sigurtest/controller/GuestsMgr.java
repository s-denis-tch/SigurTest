package com.sigurtest.controller;

import com.sigurtest.model.Employee;
import com.sigurtest.model.Guest;
import com.sigurtest.repositories.DepartmentRepository;
import com.sigurtest.repositories.EmployeeRepository;
import com.sigurtest.repositories.GuestRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GuestsMgr {

    private static final int SIX_MONTHS = 6;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;
    private final PassEmulator passEmulator;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public GuestsMgr(DepartmentRepository departmentRepository,
                     EmployeeRepository employeeRepository,
                     GuestRepository guestRepository,
                     PassEmulator passEmulator) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.guestRepository = guestRepository;
        this.passEmulator = passEmulator;
    }

    public Employee createOrNotGuest(Employee employee, LocalDate currentDate) {
        if (random.nextInt(0, 100) < 50) {
            Guest guest = new Guest(employee, notLaterSixMonths(employee.getHireTime()));
            byte[] card = passEmulator.generateCardCode();
            guest.setCard(card);
            employee.setGuest(guest);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        if (savedEmployee.getGuest() != null) {
            generateVisitLogMessage(savedEmployee, currentDate);
        }
        return savedEmployee;
    }

    private void generateVisitLogMessage(Employee employee, LocalDate currentDate) {
        String departmentName = employee.getDepartment().getName();
        Guest guest = employee.getGuest();
        LocalDate visitDate = guest.getVisitDate();
        long daysBeforeVisit = ChronoUnit.DAYS.between(currentDate, visitDate);

        String message = String.format("?????????? %d ?????????????????? ?????????????? ???????????????????? %d. " +
                        "??????????: %s. ????????: %s. ???? ?????????????? ????????????????: %d ????????.",
                guest.getId(), employee.getId(), departmentName, visitDate, daysBeforeVisit);
        System.out.println(message);
    }

    private static LocalDate notLaterSixMonths(LocalDate startInclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = startInclusive.plusMonths(SIX_MONTHS).toEpochDay();

        long randomDay = ThreadLocalRandom.current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    public void cancelVisitsOfFiredEmployees(List<Employee> firedEmployees) {
        firedEmployees.forEach(emp -> {
            if (emp.getGuest() != null) {
                Guest guest = emp.getGuest();
                String departmentName = emp.getDepartment().getName();
                LocalDate visitDate = guest.getVisitDate();
                LocalDate firedTime = emp.getFiredTime();
                String message = String.format("?????????????? ?????????? %d ?? ?????????????????????? %d ????????????????. ??????????: %s. ???????? ??????????????: %s" +
                        ", ???????? ???????????????????? ????????????????????: %s", guest.getId(), emp.getId(), departmentName, visitDate, firedTime);
                System.err.println(message);
            }
        });
    }
}