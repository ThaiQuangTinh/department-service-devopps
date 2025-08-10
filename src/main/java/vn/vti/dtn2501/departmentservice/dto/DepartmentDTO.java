package vn.vti.dtn2501.departmentservice.dto;

import java.time.LocalDateTime;

public class DepartmentDTO {

    private Integer id;

    private String name;

    private Integer totalMember;

    private String type;

    private LocalDateTime createdAt;

    public DepartmentDTO(Integer id, String name, Integer totalMember, String type, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.totalMember = totalMember;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}