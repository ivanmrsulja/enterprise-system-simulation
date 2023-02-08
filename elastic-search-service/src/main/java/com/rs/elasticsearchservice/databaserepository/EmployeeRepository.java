package com.rs.elasticsearchservice.databaserepository;

import com.rs.elasticsearchservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e where e.username = :username")
    Optional<Employee> findByUsername(String username);
}
