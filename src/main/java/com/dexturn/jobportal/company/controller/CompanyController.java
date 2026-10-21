package com.dexturn.jobportal.company.controller;


import com.dexturn.jobportal.company.service.CompanyService;
import com.dexturn.jobportal.dto.CompanyDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping(path = "/public", version = "1.0")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companyList = companyService.getAllCompanies();

        return ResponseEntity.ok(companyList);
    }

    @PostMapping(path = "/admin", version = "1.0")
    public ResponseEntity<String> createCompany(@RequestBody @Valid CompanyDTO companyDTO){

        boolean isCreated = companyService.createCompany(companyDTO);
        if(isCreated){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Request processed successfully");
        }
        else{
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed");
        }
    }

    @GetMapping(path = "/admin", version = "1.0")
    public ResponseEntity<List<CompanyDTO>> getAllCompaniesForAdmin(){
        List<CompanyDTO> companyDTOList =companyService.getAllCompaniesForAdmin();
        return ResponseEntity.ok().body(companyDTOList);
    }

    @PutMapping(path = "/{id}/admin", version = "1.0")
    public ResponseEntity<String> updateCompanyDetails(@PathVariable @NotBlank String id, @RequestBody @Valid CompanyDTO companyDTO){
        boolean isUpdated = companyService.updateCompanyDetails(Long.valueOf(id), companyDTO);
        if(isUpdated){
            return ResponseEntity.status(HttpStatus.OK).body("Company details updated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Company details");
        }
    }

    @DeleteMapping(path = "/{id}/admin", version = "1.0")
    public ResponseEntity<String> deleteCompanyById(@PathVariable @NotBlank String id) {
        companyService.deleteCompanyById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Company record deleted successfully.");
    }

}


