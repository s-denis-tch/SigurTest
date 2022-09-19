package com.sigurtest.bootstrap;

import com.sigurtest.model.Department;
import com.sigurtest.repositories.DepartmentRepository;
import com.sigurtest.repositories.EmployeeRepository;
import com.sigurtest.repositories.GuestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(value = 1)
public class Startup implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;

    public Startup(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository,
                             GuestRepository guestRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> departmentNames = Arrays.asList( "Учебный отдел",
                "Отдел охраны труда",
                "Отдел кадров",
                "Финансовый отдел",
                "Отдел маркетинга",
                "Бухгалтерия",
                "Отдел оценки рисков",
                "Производственный отдел",
                "Административно-хозяйственный отдел",
                "Отдел безопасности"
        );

        List<Department> departments = departmentNames.stream().map(Department::new).collect(Collectors.toList());
        departmentRepository.saveAll(departments);
        System.out.println("done");
    }
}
