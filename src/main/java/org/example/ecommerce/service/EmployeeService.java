package org.example.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.example.ecommerce.dto.EmployeeDTO;
import org.example.ecommerce.model.Employee;
import org.example.ecommerce.repository.jpa.EmployeeJpaRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final FileService fileService;
	private final EmployeeJpaRepository empRepository;

	public EmployeeDTO registerEmployee(EmployeeDTO dto) {
		// Phase 2: Refactored to use Spring @Valid (Manual checks removed)
		String profilePath = null;

		// 1. Process image from DTO
		if (dto.getProfilePictureBase64() != null && !dto.getProfilePictureBase64().isEmpty()) {
			String fileName = "emp_" + dto.getName().replaceAll("\\s+", "_");
			profilePath = fileService.saveBase64File(dto.getProfilePictureBase64(), fileName);
		}

		// 2. Map DTO to Model (Storage)
		Employee employee = new Employee(null, dto.getName(), dto.getEmail(), dto.getContact(), dto.getPosition(),
				profilePath);

		empRepository.save(employee);

		// 3. Map Model back to DTO for response
		return convertToDto(employee);
	}

	public List<EmployeeDTO> getAllEmployees() {
		return empRepository.findAll().stream().map(this::convertToDto).collect(java.util.stream.Collectors.toList());
	}

	public Optional<EmployeeDTO> getEmployeeByEmail(String email) {
		return empRepository.findAll().stream().filter(e -> e.getEmail().equalsIgnoreCase(email)).findFirst()
				.map(this::convertToDto);
	}

	private EmployeeDTO convertToDto(Employee model) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setName(model.getName());
		dto.setEmail(model.getEmail());
		dto.setContact(model.getContact());
		dto.setPosition(model.getPosition());
		dto.setProfilePicture(model.getProfilePicture());

		// Dynamically populate Base64 only when requested (for the API response)
		if (model.getProfilePicture() != null) {
			dto.setProfilePictureBase64(fileService.readFileAsBase64(model.getProfilePicture()));
		}

		return dto;
	}
}
