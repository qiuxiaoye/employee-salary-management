package govtech.nphc.employeesalarymanagement.service;

import govtech.nphc.employeesalarymanagement.helper.CSVHelper;
import govtech.nphc.employeesalarymanagement.model.Employee;
import govtech.nphc.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    EmployeeRepository repository;

    public void save(MultipartFile file) {
        try {
            List<Employee> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Employee> getAllTutorials() {
        return repository.findAll();
    }
}
