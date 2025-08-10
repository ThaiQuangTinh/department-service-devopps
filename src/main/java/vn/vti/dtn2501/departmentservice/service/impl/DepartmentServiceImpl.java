package vn.vti.dtn2501.departmentservice.service.impl;

import org.springframework.stereotype.Service;
import vn.vti.dtn2501.departmentservice.dto.DepartmentDTO;
import vn.vti.dtn2501.departmentservice.entity.Department;
import vn.vti.dtn2501.departmentservice.repository.DepartmentRepository;
import vn.vti.dtn2501.departmentservice.service.DepartmentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(Integer id) {
        return departmentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private DepartmentDTO convertToDTO(Department department) {
        return new DepartmentDTO(
                department.getId(),
                department.getName(),
                department.getTotalMember(),
                department.getType(),
                department.getCreatedAt()
        );
    }

}
