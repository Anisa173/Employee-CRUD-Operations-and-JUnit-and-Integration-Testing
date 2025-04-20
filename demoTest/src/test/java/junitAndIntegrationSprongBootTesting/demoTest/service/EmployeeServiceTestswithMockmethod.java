package junitAndIntegrationSprongBootTesting.demoTest.service;

import junitAndIntegrationSprongBootTesting.demoTest.ResourceNotFoundException;
import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import junitAndIntegrationSprongBootTesting.demoTest.repository.EmployeeRepository;
import junitAndIntegrationSprongBootTesting.demoTest.service.serviceImpl.EmployeeServiceImpl;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class EmployeeServiceTestswithMockmethod {

    private EmployeeRepository emplRepo;

    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        emplRepo = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(emplRepo);

    }

    //JUnit Test case for saveEmployee method
    @DisplayName("JUnit Test case for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenAddEmployeeRecord_thenReturnSavedEmployeeRecord() throws Exception {
        //given - precondition or setUp
        employee = Employee.builder().firstName("Anisa").lastName("Cela").email("celaanisa07@gmail.com").build();

        given(emplRepo.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(emplRepo.save(employee)).willReturn(employee);
        //when - the operation to be done
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);
        //then - the result to be tested
//Assertions.
        assertThat(savedEmployee.getEmplId()).isNotNull();
    }

    //JUnit Test Case for saveEmployee which throws an exception
    @DisplayName("JUnit Test Case for saveEmployee which throws an exception")
    @Test
    public void givenEmployeeObjectwithExistedEmail_whenSaveEmployee_thenReturnInsertedEmployee() throws Exception {
        //given - precondition or setUp
        given(emplRepo.findByEmail(String.valueOf(employee.getEmplId()))).willReturn(Optional.of(employee));
        System.out.println(emplRepo);
        System.out.println(employeeService);
        //when - action to be executed
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then - results to be verified
        verify(emplRepo, never()).save(any(Employee.class));
        //assertThat(emplRepo.findByEmail(employee.getEmail())).isNotNull();

    }

    //JUnit Test to getAllEmployees
    @DisplayName("JUnit Test to getAllEmployees")
    @Test
    public void givenListOfEmployees_whenFindAll_thenReturnEmployeeResult() throws Exception {
        //given - preconditions or setUp
        Employee emp1 = Employee.builder().firstName("Ani").lastName("Anis").email("anoans01@outlook.com").build();
        Employee emp2 = Employee.builder().firstName("Ani1").lastName("Anis1").email("anuaans11@outlook.com").build();
        emplRepo.save(emp1);
        emplRepo.save(emp2);
        List<Employee> employeeList = List.of(emp1, emp2);

        given(emplRepo.findAll()).willReturn(employeeList);

        //when - operation to be executed
        List<Employee> employees = employeeService.findAllEmployees();
        System.out.println("Employees are :" + employees);
        //then - action to be verified

        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    //JUnit Test to getAllEmployees --- negative Scenario
    @DisplayName("JUnit Test to getAllEmployees")
    @Test
    public void givenListOfEmployees_whenFindAllEmpty_thenReturnEmptyEmployeeList() throws Exception {
        //given - preconditions or setUp
        Employee emp1 = Employee.builder().firstName("Ani").lastName("Anis").email("anoans01@outlook.com").build();
        Employee emp2 = Employee.builder().firstName("Ani1").lastName("Anis1").email("anuaans11@outlook.com").build();
        emplRepo.save(emp1);
        emplRepo.save(emp2);
       // List<Employee> employeeList = List.of(emp1, emp2);

        given(emplRepo.findAll()).willReturn(Collections.emptyList());

        //when - operation to be executed
        List<Employee> employees = employeeService.findAllEmployees();
        System.out.println(employees);
        //then - action to be verified

        assertThat(employees).isEmpty();
        assertThat(employees.size()).isEqualTo(0);
    }



    //JUnit test case for update Employee_Record data
    @DisplayName("JUnit test case for update Employee_Record data")
    @Test
    public void givenEmployObject_whenUpdateEmployeeRecordById_thenSaveUpdatedRecord() throws Exception {
        //given - preconditions or setUp
        Employee emp1 = Employee.builder().firstName("Ani").lastName("Anis").email("anoans01@outlook.com").build();
        emplRepo.save(emp1);
        Employee employee1 = emplRepo.findById(emp1.getEmplId()).get();
        employee1.setEmail("anis_ano01@gmail.com");
        //Employee updatedEmpl = emplRepo.save(employee1);
        given(emplRepo.save(employee1)).willReturn(employee1);

        //when  - the update operation to be executed
        Optional<Employee> emplResult = employeeService.updatedEmployee(employee1, employee1.getEmplId());

        //then  - the operation to be verified
        assertThat(emplResult.get().getEmail()).isEqualTo("anis_ano01@gmail.com");
    }

    //JUnit test case for fetching an Employee Record of data
    @DisplayName("JUnit test case for fetching an Employee Record of data")
    @Test
    public void givenEmployObject_whenGetEmployById_thenReturnEmployee() throws Exception {
        //given - preconditions or setUp
        Employee empl = Employee.builder().firstName("Anisa").lastName("Cela").email("celaanisa11@gmail.com").build();
        Employee employee1 = emplRepo.save(empl);

        given(emplRepo.findById(employee1.getEmplId())).willReturn(Optional.of(employee1));
        //when - the fetch operation is going to be executed
        Optional<Employee> employee2 = employeeService.getEmployeeById(employee1.getEmplId());

        //then - the result is going to be verified
        assertThat(employee1.getEmplId()).isNotNull();
        assertThat(employee1.getEmplId()).isGreaterThan(0);
        assertThat(employee1.getFirstName()).isEqualTo("Anisa");
        assertThat(employee1.getLastName()).isEqualTo("Cela");
        assertThat(employee1.getEmail()).isEqualTo("celaanisa11@gmail.com");
    }

    //JUnit Test case for DELETE an Employee Record
    @DisplayName("JUnit Test case for DELETE an Employee Record")
    @Test
    public void givenEmployeeObject_whenDeleteById_thenVerifyRemovedEmployee() throws Exception {
        //given - preconditions or setUp
        Employee empl = Employee.builder().firstName("Anisa").lastName("Cela").email("celaanisa11@gmail.com").build();
        Employee employee1 = emplRepo.save(empl);
        willDoNothing().given(emplRepo).deleteById(employee1.getEmplId());

        //when - the delete operation to be executed

        employeeService.deleteEmployee(employee1.getEmplId());

        //then - the action executed to be tested
        assertThat(employee1.getEmplId()).isEqualTo(0);

    }

    // List<Employee> findByFirstNameAndLastName
//JUnit Test case for fetching some Employee Records filtered by firstName and lastName
    @DisplayName("JUnit Test case for fetching some Employee Records filtered by firstName and lastName")
    @Test
    public void givenListOfEmployee_whenFindAllByFnameAndLname_thenReturnListofEmployee() throws Exception {
        //given - preconditions or setup
        Employee empl1 = Employee.builder().firstName("Astri").lastName("Astari").email("asastari01@gmail.com").build();
        emplRepo.save(empl1);
        Employee empl2 = Employee.builder().firstName("Astri").lastName("Astari").email("astaria12@gmail.com").build();
        emplRepo.save(empl2);
        Employee empl3 = Employee.builder().firstName("Astri").lastName("Astari").email("staria.as0123@gmail.com").build();
        emplRepo.save(empl3);
        Employee empl4 = Employee.builder().firstName("Arizona").lastName("Mandeli").email("ari.mand01@gmail.com").build();
        emplRepo.save(empl4);

        List<Employee> employeeList = List.of(empl1, empl2, empl3, empl4);
        given(emplRepo.findByFirstNameAndLastName("Astri", "Astari")).willReturn(employeeList.stream().toList());

        //when - the operation to be executed
        List<Employee> employeeList1 = employeeService.findByFirstNameAndLastName("Astri", "Astari");
        System.out.println(employeeList1);
        //then - the operation to be verified
        assertThat(employeeList1.size()).isGreaterThan(0);
        assertThat(employeeList1.size()).isEqualTo(3);
    }

    //JUnit tests case for findAll Records filtered by lastName
    @DisplayName("JUnit tests case for findAll Records filtered by lastName")
    @Test
    public void givenListOfEmployee_whenFindAllByLastName_thenReturnEmployeeRecords() throws Exception {
        //given - precondition or setUp
        Employee empl1 = Employee.builder().firstName("Astri").lastName("Astari").email("asastari01@gmail.com").build();
        emplRepo.save(empl1);
        Employee empl2 = Employee.builder().firstName("Arina").lastName("Astari").email("astaria12@gmail.com").build();
        emplRepo.save(empl2);
        Employee empl3 = Employee.builder().firstName("Aria").lastName("Astari").email("staria.as0123@gmail.com").build();
        emplRepo.save(empl3);
        Employee empl4 = Employee.builder().firstName("Arizona").lastName("Mandeli").email("ari.mand01@gmail.com").build();
        emplRepo.save(empl4);

        List<Employee> employeeList = List.of(empl1, empl2, empl3, empl4);
        given(emplRepo.findEmployeesByLastName("Astari")).willReturn(employeeList.stream().toList());
        //when - serviceOperation to be executed
        List<Employee> listOfEmployee = employeeService.findEmployeesByLastName("Astari");
        System.out.println(listOfEmployee);
        //then - resultOperation to be tested
        assertThat(listOfEmployee.size()).isGreaterThan(0);
        assertThat(listOfEmployee.size()).isEqualTo(3);
    }

    //JUnit Tests to get EmployeeRecord filtered by lastName and unique email
    @DisplayName("JUnit Tests to get EmployeeRecord filtered by lastName and unique email")
    @Test
    public void givenListOfEmployee_whenFindEmployByLastNameANDemail_thenReturnEmployeeRecord() throws Exception {
        //given - preconditions or setUp
        Employee empl1 = Employee.builder().firstName("Astri").lastName("Astari").email("asastari01@gmail.com").build();
        emplRepo.save(empl1);
        Employee empl2 = Employee.builder().firstName("Arina").lastName("Astari").email("astaria12@gmail.com").build();
        emplRepo.save(empl2);
        Employee empl3 = Employee.builder().firstName("Aria").lastName("Astari").email("staria.as013@gmail.com").build();
        emplRepo.save(empl3);
        Employee empl4 = Employee.builder().firstName("Arizona").lastName("Mandeli").email("ari.mand01@gmail.com").build();
        emplRepo.save(empl4);

        List<Employee> employeeList = List.of(empl1, empl2, empl3, empl4);
        given(emplRepo.findByEmailAndLastName("staria.as013@gmail.com", "Astari")).willReturn(employeeList.stream().findFirst());
        //when - serviceOperation to be executed
        Optional<Employee> empl = employeeService.findByEmailAndLastName("staria.as013@gmail.com", "Astari");
        System.out.println(empl);
        //then - operationResult to be verified
        assertThat(empl.isPresent()).isTrue();
        assertThat(empl.get().getEmplId()).isNotNull();
        assertThat(empl.get().getLastName()).isEqualTo("Astari");
        assertThat(empl.get().getEmail()).isEqualTo("staria.as013@gmail.com");
    }

    //JUnit Tests to get EmployeeRecord filtered by lastName and unique email
    @DisplayName("JUnit Tests to get EmployeeRecord filtered by firstName and unique email")
    @Test
    public void givenListOfEmployee_whenFindEmployByFirstNameANDemail_thenReturnEmployeeRecord() throws Exception {
        //given - preconditions or setUp
        Employee empl1 = Employee.builder().firstName("Astri").lastName("Astari").email("asastari01@gmail.com").build();
        emplRepo.save(empl1);
        Employee empl2 = Employee.builder().firstName("Arina").lastName("Astari").email("astaria12@gmail.com").build();
        emplRepo.save(empl2);
        Employee empl3 = Employee.builder().firstName("Aria").lastName("Astari").email("staria.as013@gmail.com").build();
        emplRepo.save(empl3);
        Employee empl4 = Employee.builder().firstName("Arizona").lastName("Mandeli").email("ari.mand01@gmail.com").build();
        emplRepo.save(empl4);

        List<Employee> employeeList = List.of(empl1, empl2, empl3, empl4);
        given(emplRepo.findByEmailAndLastName("staria.as013@gmail.com", "Aria")).willReturn(employeeList.stream().findFirst());
        //when - serviceOperation to be executed
        Optional<Employee> empl = employeeService.findByEmailAndLastName("staria.as013@gmail.com", "Aria");
        System.out.println(empl);
        //then - operationResult to be verified
        assertThat(empl.isPresent()).isTrue();
        assertThat(empl.get().getEmplId()).isNotNull();
        assertThat(empl.get().getFirstName()).isEqualTo("Astari");
        assertThat(empl.get().getEmail()).isEqualTo("staria.as013@gmail.com");
    }


}




