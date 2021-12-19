package govtech.nphc.employeesalarymanagement.controller;

import govtech.nphc.employeesalarymanagement.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
public class EmployeeControllerTest {

    @Autowired
    EmployeeController employeeController;

    @Test
    public void testCreateEmployee_Success() {
        Employee employee = new Employee();
        employee.setId(5);
        employee.setLogin("1");
        employee.setName("1");
        employee.setSalary(BigDecimal.valueOf(1));
        employee.setStartDate(LocalDate.of(2000, 1, 1));

        Assertions.assertEquals(employeeController.createEmployee(employee).getBody().getMessage(),
                "Successfully created");
    }

    @Test
    public void testCreateEmployee_FailWithEmptyField() {
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setLogin("");
        employee1.setName("1");
        employee1.setSalary(BigDecimal.valueOf(1));
        employee1.setStartDate(LocalDate.of(2000, 01, 01));

        Assertions.assertEquals(employeeController.createEmployee(employee1).getBody().getMessage(),
                "Employee param has empty field");
    }

    @Test
    public void testCreateEmployee_FailWithInvalidData() {
        Employee employee2 = new Employee();
        employee2.setId(1);
        employee2.setLogin("1");
        employee2.setName("1");
        employee2.setSalary(BigDecimal.valueOf(-1));
        employee2.setStartDate(LocalDate.of(2000, 01, 01));

        Assertions.assertEquals(employeeController.createEmployee(employee2).getBody().getMessage(),
                "Invalid salary");
    }

    @Test
    public void testUpdateEmployee_Success() {
        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setLogin("1");
        savedEmployee.setName("1");
        savedEmployee.setSalary(BigDecimal.valueOf(1));
        savedEmployee.setStartDate(LocalDate.of(2000, 01, 01));

        Employee updateEmployee = new Employee();
        updateEmployee.setId(1L);
        updateEmployee.setLogin("2");

        employeeController.createEmployee(savedEmployee);


        Assertions.assertEquals(employeeController.updateEmployeeById(1, updateEmployee).getBody().getMessage(),
                "Successfully updated");
    }
}
