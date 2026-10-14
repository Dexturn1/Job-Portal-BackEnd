package com.dexturn.jobportal.company.service;


import com.dexturn.jobportal.dto.CompanyDTO;

import java.util.List;


public interface CompanyService {

    List<CompanyDTO> getAllCompanies();
    List<CompanyDTO> getAllCompaniesForAdmin();
    void deleteCompanyById(Long id);
    boolean createCompany(CompanyDTO companyDTO);
    boolean updateCompanyDetails(Long id, CompanyDTO companyDTO);
}
