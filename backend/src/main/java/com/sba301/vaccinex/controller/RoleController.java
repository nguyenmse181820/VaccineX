package com.sba301.vaccinex.controller;

import com.sba301.vaccinex.dto.request.RoleRequestDTO;
import com.sba301.vaccinex.dto.internal.ObjectResponse;
import com.sba301.vaccinex.dto.response.RoleResponseDTO;
import com.sba301.vaccinex.service.spec.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Các hoạt động liên quan đến quản lý vai trò")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("{roleId}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Integer roleId) {
        return ResponseEntity.ok(roleService.findById(roleId));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createNewRole(@Valid @RequestBody RoleRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(dto));
    }

    @PutMapping("{roleId}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Integer roleId, @Valid @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.ok(roleService.update(roleId, dto));
    }

    @DeleteMapping("{roleId}")
    public ResponseEntity<ObjectResponse> deleteRoleById(@PathVariable Integer roleId) {
        roleService.deleteById(roleId);
        return ResponseEntity.noContent().build();
    }
}
