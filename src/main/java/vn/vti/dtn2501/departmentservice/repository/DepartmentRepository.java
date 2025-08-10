package vn.vti.dtn2501.departmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.vti.dtn2501.departmentservice.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {


}
