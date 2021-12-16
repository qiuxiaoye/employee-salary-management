package govtech.nphc.employeesalarymanagement.model;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {

    private long id;
    private String name;
    private String login;
    private BigDecimal salary;
    private Date startDate;

    public Employee(){}

    public Employee(long id, String name, String login, BigDecimal salary, Date startDate) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.salary = salary;
        this.startDate = startDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }
}
