package govtech.nphc.employeesalarymanagement.service;

import govtech.nphc.employeesalarymanagement.helper.CSVHelper;
import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static govtech.nphc.employeesalarymanagement.utils.Utils.isContainLogin;
import static govtech.nphc.employeesalarymanagement.utils.Utils.isEmptyOrWhiteSpace;

@Service
public class CSVService {
    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> save(MultipartFile file) {
        try {
            List<Employee> employees = CSVHelper.csvToEmployees(file.getInputStream());
            List<Employee> updateEmployees = new ArrayList<>();
            for (Employee employee : employees) {
                // Check all Employee elements are filled not null/ whitespace.
                if (employee.getId() == 0
                        || isEmptyOrWhiteSpace(employee.getName())
                        || isEmptyOrWhiteSpace(employee.getLogin())
                        || employee.getSalary() == null
                        || employee.getStartDate() == null) {
                    throw new RuntimeException("Invalid data input in CSV file.");
                }

                // duplicated id
                for (int i = 0; i < employees.size(); i++) {
                    for (int j = i + 1; j < employees.size(); j++) {
                        if (employees.get(i).getId() == employees.get(j).getId()){
                            throw new RuntimeException("Duplicated id in the list");
                        }
                    }
                }

                // Check illegal input.
                if (isContainLogin(employeeRepository.findAll(), employee.getLogin())) {
                    throw new RuntimeException("Employee login not unique");
                }

                if (employee.getSalary().compareTo(BigDecimal.valueOf(0)) == -1) {
                    throw new RuntimeException("Invalid salary");
                }

                if (employee.getStartDate().isAfter(LocalDate.now())) {
                    throw new RuntimeException("Invalid date");
                }

                // id exist, run update
                if (employeeRepository.findById(employee.getId()).isPresent()) {
                    updateEmployees.add(employee);
                }
            }
            employeeRepository.saveAll(employees);
            return updateEmployees;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }


    public int updateAll(List<Employee> updateEmployee) {
        int counter = 0; // count total updates
        try {
            if (updateEmployee == null) {
                throw new RuntimeException("No update records.");
            }
            for (Employee employee : updateEmployee) {
                if (update(employee)) counter ++;
            }
            return counter;
        } catch (Exception e) {
            throw new RuntimeException("fail to update: " + e.getMessage());
        }
    }

    public boolean update(Employee employee) {
        try {
            int result = 0;
            Optional<Employee> employeeData = employeeRepository.findById(employee.getId());
            if (employeeData.isPresent()) {
                Employee emp = employeeData.get();

                if (!emp.getLogin().equals(employee.getLogin())) {
                    throw new RuntimeException("Employee login not unique");
                }
                emp.setLogin(employee.getLogin());

                emp.setName(employee.getName());

                if (employee.getSalary().compareTo(BigDecimal.valueOf(0)) == -1) {
                    throw new RuntimeException("Invalid salary");
                }
                emp.setSalary(employee.getSalary());

                if (employee.getStartDate().isAfter(LocalDate.now())) {
                    throw new RuntimeException("Invalid date");
                }
                emp.setStartDate(employee.getStartDate());

                employeeRepository.save(emp);
                return true;
            }

        } catch (Exception e) {
                throw new RuntimeException("No such employee" );
        }
        return false;
    }


}
