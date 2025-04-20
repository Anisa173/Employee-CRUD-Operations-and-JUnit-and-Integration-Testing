package junitAndIntegrationSprongBootTesting.demoTest.repository;

import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository eRepository;
    private Employee employee1;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("Anisa")
                .firstName("Cela")
                .email("celaanisa07@gmail.com")
                .build();
    }

    //JUnit test for save Employee Records
    @DisplayName("JUnit Test for adding new Employee Records into db")
    @Test
    public void givenEmployeeObject_whenAddEmployeeRecord_thenReturnSavedEmployeeRecord() {
        //given - precondition or setUp
        //  Employee employee1 = Employee.builder()
        //          .firstName("Anisa")
        //        .firstName("Cela")
        //      .email("celaanisa07@gmail.com")
        //    .build();
        //when - the action that then we are going to test
        Employee savedEmployee = eRepository.save(employee1);

        //then - verify the excepted Result with the actualResult---verify the result
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getEmplId()).isGreaterThan(0);

    }

    //JUnit Test for generate or catch an employeeRecord based on employeeId
    @DisplayName("JUnit Test for generate or catch an employeeRecord based on employeeId")
    @Test
    public void givenEmployeeObject_whenFetchAnEmployeeById_thenVerifyFetchedEmployee() {
        //given - precondition or SetUp
        Employee emp = Employee.builder()
                .firstName("Anisa1")
                .email("celaa101@gmail.com")
                .lastName("Cela1")
                .build();
        Employee save = eRepository.save(emp);
        Employee saveEmpl = eRepository.save(employee1);
        //when - the operation we are going to verify in the next step
        Optional<Employee> employeeById = eRepository.findById(save.getEmplId());
        Optional<Employee> employeeById1 = eRepository.findById(saveEmpl.getEmplId());
        //then
        assertThat(employeeById).isNotNull();
        assertThat(employeeById1).isNotNull();
        assertEquals("celaa101@gmail.com", employeeById.get().getEmail());
        assertEquals("celaanisa07@gmail.com", employeeById1.get().getEmail());
    }

    //JUnit Test for updating datas of an EmployeeRecord
    @DisplayName("JUnit Test for updating datas of an EmployeeRecord")
    @Test
    public void givenEmployObject_whenUpdateEmployeeAtributes_thenReturnUpdatedEmployRecord() {
        //given - precondition or setup

        eRepository.save(employee1);
        //when - the action to be done
        Employee employeeById = eRepository.findById(employee1.getEmplId()).get();
        employeeById.setEmail("celaa123@gmail.com");
        Employee updatedEmployee = eRepository.save(employeeById);

        //then - the operation to be tested
        //  assertEquals("celaa123@gmail.com",employeeById.getEmail());
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("celaa123@gmail.com");
        assertThat(updatedEmployee.getEmplId()).isEqualTo(employeeById.getEmplId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo(employeeById.getFirstName());
        assertThat(updatedEmployee.getLastName()).isEqualTo(employeeById.getLastName());
    }

    //JUnit test for listing all Employee Records
    @DisplayName("JUnit test for listing all Employee Records")
    @Test
    public void givenEmployeeRecords_whenGenerateListOfEmployees_thenReturnListOfEmployNotEmpty() {

        //given - preconditions or setUp
        eRepository.save(employee1);
        Employee emp1 = Employee.builder()
                .firstName("Tony")
                .lastName("Spark")
                .email("tonyS01@yahoo.com")
                .build();
        Employee emp2 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark11@outlook.com")
                .build();
        Employee emp3 = Employee.builder()
                .firstName("Jenny")
                .lastName("Petersoon")
                .email("jPetersson@icloud.com")
                .build();
        eRepository.save(emp1);
        eRepository.save(emp2);
        eRepository.save(emp3);
        //when - the action to be done
        List<Employee> empList = eRepository.findAll();
        //then - the result to be tested
        assertThat(empList.size()).isGreaterThan(0);
        assertThat(empList.size()).isEqualTo(4);
        assertThat(eRepository.save(employee1)).isNotNull();
        assertThat(eRepository.save(emp1)).isNotNull();
        assertThat(eRepository.save(emp2)).isNotNull();
        assertThat(eRepository.save(emp3)).isNotNull();

    }

    //JUnit Test for remove_Records operation
    @DisplayName("JUnit Test for remove_Records operation")
    @Test
    public void givenEmployeeObject_whenDeleteEmployById_thenVerifyRemovedEmployee() {
        //given - preconditions or setUp

        Employee emp1 = Employee.builder()
                .firstName("Tony")
                .lastName("Spark")
                .email("tonyS01@yahoo.com")
                .build();
        Employee emp2 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark11@outlook.com")
                .build();
        Employee emp3 = Employee.builder()
                .firstName("Jenny")
                .lastName("Petersoon")
                .email("jPetersson@icloud.com")
                .build();
        eRepository.save(emp1);
        Employee e2 = eRepository.save(emp2);
        eRepository.save(emp3);
        eRepository.save(employee1);
        //when - Deletion of employee Record is Done
        List<Employee> employeeList = eRepository.findAll();
        // Optional<Employee> emp = Optional.of(eRepository.findById(e1.getEmplId()).get());
        Optional<Employee> emp = Optional.of(eRepository.findById(e2.getEmplId()).get());
        // Optional<Employee> emp = Optional.of(eRepository.findById(e3.getEmplId()).get());
        eRepository.deleteById(emp.get().getEmplId());
        //then - Verify the removed record
        assertThat(employeeList.size()).isEqualTo(3);
        assertThat(emp.get().getEmplId()).isNull();
        assertThat(emp).isEmpty();
    }

    //JUnit Test for fetch an EmployeeRecord using JPQL with indexed params
    @DisplayName("JUnit Test for fetch an EmployeeRecord using JPQL with indexed params")
    @Test
    public void givenEmployeeRecord_whenFindByfnameAndlName_thenReturnEmployeeRecord() {
        //given - preconditions or setUp
        //   Employee emp = Employee.builder()
        //         .firstName("Alina")
        //       .lastName("Frashtiz")
        //     .email("linaFrashtiz08@outlook.com")
        //   .build();
        Employee emp1 = Employee.builder()
                .firstName("Anisa")
                .lastName("Cela")
                .email("celaanisa07@gmail.com")
                .build();
        Employee emp2 = Employee.builder()
                .firstName("Anisa")
                .lastName("Cela")
                .email("celaanisa07@gmail.com")
                .build();
        // Employee emp3 = Employee.builder()
        //       .firstName("Jenny")
        //     .lastName("Petersoon")
        //   .email("jPetersson@icloud.com")
        // .build();
        Employee emp4 = Employee.builder()
                .firstName("Anisa")
                .lastName("Cela")
                .email("celaanisa07@gmail.com")
                .build();
        eRepository.save(employee1);
        // eRepository.save(emp);
        eRepository.save(emp1);
        eRepository.save(emp2);
        //    eRepository.save(emp3);
        eRepository.save(emp4);
        //when - the operation to be executed
        List<Employee> employees = eRepository.findAll();
        List<Employee> listOfEmployee = eRepository.findByFirstNameAndLastName("Anisa", "Cela");

        //then - the result we are going to test
        assertThat(employees.size()).isNotNull();
        assertThat(listOfEmployee.size()).isNotNull();
        assertThat(listOfEmployee.size()).isGreaterThan(0);
    }

    //JUnit Tests for filtering employees based on lastName
    @DisplayName("JUnit Tests for filtering employees based on lastName")
    @Test
    public void givenListOfEmployees_whenFindAllByLastName_thenReturnEmployeesRecords() {
        //given - preconditions
        Employee emp = Employee.builder()
                .firstName("Alina")
                .lastName("Franchtiz")
                .email("linaFrashtiz08@outlook.com")
                .build();
        Employee emp1 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("aStarkS01@yahoo.com")
                .build();
        Employee emp2 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark22@outlook.com")
                .build();
        Employee emp3 = Employee.builder()
                .firstName("Jenny")
                .lastName("Petersoon")
                .email("jPetersson@icloud.com")
                .build();
        Employee emp4 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark11@outlook.com")
                .build();
        eRepository.save(emp);
        eRepository.save(emp1);
        Employee e2 = eRepository.save(emp2);
        eRepository.save(emp3);
        eRepository.save(emp4);

        //when
        List<Employee> employeeList = eRepository.findEmployeesByLastName(e2.getLastName());
        //then
        assertThat(employeeList.size()).isGreaterThan(0);
    }

    //JUnit Tests for fetching a record filtered by THE UNIQUE Email and lastName
    @DisplayName("JUnit Tests for fetching a record filtered by THE UNIQUE Email and lastName")
    @Test
    public void givenEmployeeObject_whenFindByEmailAndLastName_thenReturnEmployeeRecord() {
        //given
        Employee emp = Employee.builder()
                .firstName("Alina")
                .lastName("Franchtiz")
                .email("linaFrashtiz08@outlook.com")
                .build();
        Employee emp1 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("aStarkS01@yahoo.com")
                .build();
        Employee emp2 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark22@outlook.com")
                .build();
        Employee emp3 = Employee.builder()
                .firstName("Jenny")
                .lastName("Petersoon")
                .email("jPetersson@icloud.com")
                .build();
        Employee emp4 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark11@outlook.com")
                .build();
        eRepository.save(emp);
        eRepository.save(emp1);
        eRepository.save(emp2);
        eRepository.save(emp3);
        eRepository.save(emp4);
        //when
        Optional<Employee> employee = eRepository.findByEmailAndLastName("ani_stark22@outlook.com", "Stark");

        //then
        assertThat(employee).isNotNull();
    }

    //JUnit Tests for fetching a record filtered by THE UNIQUE Email and firstName
    @DisplayName("JUnit Tests for fetching a record filtered by THE UNIQUE Email and firstName")
    @Test
    public void givenEmployeeObject_whenFindByEmailAndFirstName_thenReturnEmployeeRecord() {
        //given
        Employee emp = Employee.builder()
                .firstName("Alina")
                .lastName("Franchtiz")
                .email("linaFrashtiz08@outlook.com")
                .build();
        Employee emp1 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("aStarkS01@yahoo.com")
                .build();
        Employee emp2 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark22@outlook.com")
                .build();
        Employee emp3 = Employee.builder()
                .firstName("Jenny")
                .lastName("Petersoon")
                .email("jPetersson@icloud.com")
                .build();
        Employee emp4 = Employee.builder()
                .firstName("Ani")
                .lastName("Stark")
                .email("ani_stark11@outlook.com")
                .build();
        eRepository.save(emp);
        eRepository.save(emp1);
        eRepository.save(emp2);
        eRepository.save(emp3);
        eRepository.save(emp4);
        //when
        Optional<Employee> employee = eRepository.findEmployeesByEmailAndByFirstName("ani_stark22@outlook.com", "Ani");

        //then
        assertThat(employee.get().getEmail()).isEqualTo("ani_stark22@outlook.com");
        assertThat(employee.get().getFirstName()).isEqualTo("Ani");
    }


}




