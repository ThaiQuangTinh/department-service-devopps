package vn.vti.dtn2501.departmentservice.service;

import java.util.List;
import vn.vti.dtn2501.departmentservice.dto.DepartmentDTO;

public interface DepartmentService {

    List<DepartmentDTO> getAllDepartments();

    DepartmentDTO getDepartmentById(Integer id);

}
