package junitAndIntegrationSprongBootTesting.demoTest.service.serviceImpl;

import junitAndIntegrationSprongBootTesting.demoTest.ResourceNotFoundException;
import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import junitAndIntegrationSprongBootTesting.demoTest.repository.EmployeeRepository;
import junitAndIntegrationSprongBootTesting.demoTest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository eRepository;

    public EmployeeServiceImpl(EmployeeRepository eRepository) {
        this.eRepository = eRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) throws Exception {

        Optional<Employee> savedEmployee1 = eRepository.findByEmail(employee.getEmail());
        if (savedEmployee1.isPresent()) {
            throw new ResourceNotFoundException("Employee with email :" + employee.getEmail() + " " + "has already existed!");
        }
      else {
            return eRepository.save(employee);
        }
    }

    @Override
    public Optional<Employee> updatedEmployee(Employee empl, long employeeId) throws Exception {
        return eRepository.findById(employeeId).map(employeeRecord -> {
            employeeRecord.setFirstName(empl.getFirstName());
            employeeRecord.setLastName(empl.getLastName());
            employeeRecord.setEmail(empl.getEmail());

            return eRepository.save(employeeRecord);
        });
    }

    @Override
    public Optional<Employee> getEmployeeById(long emplId) throws Exception {
        return Optional.ofNullable(eRepository.findById(emplId).orElseThrow(() -> new ResourceNotFoundException("Employee is not found!!!")));
    }

    @Override
    public List<Employee> findAllEmployees() throws Exception {
        return eRepository.findAll();
    }

    @Override
    public void deleteEmployee(long emplId) throws Exception {
        eRepository.deleteById(emplId);
    }

    @Override
    public List<Employee> findByFirstNameAndLastName(String firstName, String lastName) throws Exception {
        return eRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public List<Employee> findEmployeesByLastName(@Param("lastName") String lastName) throws Exception {
        return eRepository.findEmployeesByLastName(lastName);
    }

    @Override
    public Optional<Employee> findByEmailAndLastName(String email, String lastName) throws Exception {
        return eRepository.findByEmailAndLastName(email, lastName);
    }

    @Override
    public Optional<Employee> findEmployeesByEmailAndByFirstName(String email, String firstName) throws Exception {
        return eRepository.findEmployeesByEmailAndByFirstName(email, firstName);
    }
}

