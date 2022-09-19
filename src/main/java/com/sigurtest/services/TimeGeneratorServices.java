package com.sigurtest.services;

import com.sigurtest.controller.EmployeesMgr;
import com.sigurtest.controller.PassEmulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimeGeneratorServices {
    private static final Logger log = LoggerFactory.getLogger(TimeGeneratorServices.class);

    private final EmployeesMgr employeesMgr;
    private final PassEmulator passEmulator;

    private LocalDate dateStart = LocalDate.of(2022, Month.JANUARY, 1);
    private LocalDate dateEnd = LocalDate.of(2023, Month.JANUARY, 1);
    private LocalDate currentDate = dateStart;

    public TimeGeneratorServices(EmployeesMgr employeesMgr,
                                PassEmulator passEmulator) {
        this.employeesMgr = employeesMgr;
        this.passEmulator = passEmulator;
    }

    public AtomicBoolean getEnabled() {
        return enabled;
    }

    private AtomicBoolean enabled = new AtomicBoolean(true);

    @Scheduled(fixedRate = 1000)
    public void newDayHasCome() {
        if (currentDate.isBefore(dateEnd)) {
            employeesMgr.generateNewEmployee(currentDate, dateEnd);

            passEmulator.helloNewDay(currentDate);

            currentDate = currentDate.plusDays(1);
        } else {
            log.info("Менеджер закончил работу с персоналом {}", enabled.get());
            enabled.set(false);
        }
    }
}
