package junitAndIntegrationSprongBootTesting.demoTest.repository;

import junitAndIntegrationSprongBootTesting.demoTest.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //Define custom query using JPQL with named params
    @Query("Select e From Employee e Where e.email =:email")
    Optional<Employee> findByEmail(@Param("email") String email);

    // define custom query using JPQL with indexed params
    @Query("Select e From Employee e Where e.firstName= ?1 and e.lastName = ?2 Group BY e.employeeId")
    List<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    // define custom query using JPQL with Named Params
    @Query("Select e from Employee e Where e.lastName = :lastName")
    List<Employee> findEmployeesByLastName(@Param("lastName") String lastName);

    //define custom query using JPQL with Index Parameters
    @Query("Select e from Employee e Where e.email = ?1 and e.lastName = ?2")
    Optional<Employee> findByEmailAndLastName(String email, String lastName);

    //define custom query using Native SQL with Named Params
    @Query("Select e From Employee  e Where e.email= :email and e.firstName= :firstName")
    Optional<Employee> findEmployeesByEmailAndByFirstName(@Param("email") String email,@Param("firstName") String firstName);
}
