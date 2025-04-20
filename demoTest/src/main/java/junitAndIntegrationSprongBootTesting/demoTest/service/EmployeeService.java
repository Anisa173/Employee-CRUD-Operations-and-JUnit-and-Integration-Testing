package junitAndIntegrationSprongBootTesting.demoTest.service;

import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    public Employee saveEmployee(Employee employee) throws Exception;

    public Optional<Employee> updatedEmployee(Employee empl, long employeeId) throws Exception;

    public Optional<Employee> getEmployeeById(long emplId) throws Exception;

    public List<Employee> findAllEmployees() throws Exception;

    void deleteEmployee(long emplId) throws Exception;

    public List<Employee> findByFirstNameAndLastName(String firstName, String lastName) throws Exception;

    public List<Employee> findEmployeesByLastName(@Param("lastName") String lastName) throws Exception;

    public Optional<Employee> findByEmailAndLastName(String email, String lastName) throws Exception;
    public Optional<Employee> findEmployeesByEmailAndByFirstName(@Param("email") String email , @Param("firstName") String firstName) throws Exception;
}



