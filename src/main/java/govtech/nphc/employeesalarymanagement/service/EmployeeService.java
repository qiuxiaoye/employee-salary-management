package govtech.nphc.employeesalarymanagement.service;

import govtech.nphc.employeesalarymanagement.message.ResponseMessage;
import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static govtech.nphc.employeesalarymanagement.utils.Utils.isContainLogin;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * CURD Create service for Employee.
     * @param employee Employee entity.
     */
    public void create(Employee employee) {
        try
        {
            if (employeeRepository.findById(employee.getId()).isPresent()) {
                throw new RuntimeException("Employee ID already exists");
            }

            if (isContainLogin(employeeRepository.findAll(), employee.getLogin())) {
                throw new RuntimeException("Employee login not unique");
            }

            if (employee.getSalary().compareTo(BigDecimal.valueOf(0)) == -1) {
                throw new RuntimeException("Invalid salary");
            }

            if (employee.getStartDate().isAfter(LocalDate.now())) {
                throw new RuntimeException("Invalid date");
            }

            employeeRepository.save(new Employee(
                    employee.getId(),
                    employee.getName(),
                    employee.getLogin(),
                    employee.getSalary(),
                    employee.getStartDate()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Delete Employee by id.
     * @param id long
     */
    public void deleteById(long id) {
        try {
            if(employeeRepository.findById(id).isPresent()) {
                employeeRepository.deleteById(id);
            }
            throw new RuntimeException("No such employee");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get Employee by id.
     * @param id Long
     * @return Employee
     */
    public Employee getEmployeeById(long id) {
        try {
            Optional<Employee> employeeData = employeeRepository.findById(id);
            if (employeeData.isPresent()) {
                return employeeData.get();
            }
            throw new RuntimeException("no such employee");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Update employee by id.
     * @param id Long
     * @param employee Employee infomation needs to update.
     */
    public void updateEmployeeById(long id, Employee employee) {
        try {
            Optional<Employee> employeeData = employeeRepository.findById(id);
            if (employeeData.isPresent()) {
                Employee emp = employeeData.get();

                if (employee.getLogin() != null) {
                    if (isContainLogin(employeeRepository.findAll(), employee.getLogin())) {
                        throw new RuntimeException("Employee login not unique");
                    }
                    emp.setLogin(employee.getLogin());
                }
                if (employee.getName() != null) {
                    emp.setName(employee.getName());
                }
                if (employee.getSalary() != null) {
                    if (employee.getSalary().compareTo(BigDecimal.valueOf(0)) == -1) {
                        throw new RuntimeException("Invalid salary");
                    }
                    emp.setSalary(employee.getSalary());
                }
                if (employee.getStartDate() != null) {
                    if (employee.getStartDate().isAfter(LocalDate.now())) {
                        throw new RuntimeException("Invalid date");
                    }
                    emp.setStartDate(employee.getStartDate());
                }
                employeeRepository.save(emp);
            } else {
                throw new RuntimeException("No such employee");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int updateAll(List<Employee> updateEmployee) {
        int counter = 0; // count total updates
        try {
            if (updateEmployee == null) {
                throw new RuntimeException("No update records.");
            }
            for (Employee employee : updateEmployee) {
                updateEmployeeById(employee.getId(), employee);
                counter ++;
            }
            return counter;
        } catch (Exception e) {
            throw new RuntimeException("fail to update: " + e.getMessage());
        }
    }
}
