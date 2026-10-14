package com.dexturn.jobportal.company.service.imp;

import com.dexturn.jobportal.company.service.CompanyService;
import com.dexturn.jobportal.constanst.ApplicationConstants;
import com.dexturn.jobportal.dto.CompanyDTO;
import com.dexturn.jobportal.dto.JobDto;
import com.dexturn.jobportal.entity.Company;
import com.dexturn.jobportal.entity.Job;
import com.dexturn.jobportal.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;


    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<Company> companyList =companyRepository.fetchCompaniesWithJobsByStatus(ApplicationConstants.ACTIVE_STATUS);
        return companyList.stream().map(this::transformCompanyToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean createCompany(CompanyDTO companyDTO) {
        Company company = transformCompanyDtoToEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);
        return savedCompany.getId() != null && savedCompany.getId()>0;

    }

    @Override
    @Transactional
    public boolean updateCompanyDetails(Long id, CompanyDTO companyDTO) {

        int updatedRecords = companyRepository.updateCompanyDetails(
                id,companyDTO.name(),companyDTO.logo(),
                companyDTO.industry(),companyDTO.size(),companyDTO.rating(),
                companyDTO.locations(),companyDTO.founded(),companyDTO.description(),
                companyDTO.employees(),companyDTO.website()
        );
        return updatedRecords > 0;
    }


    @Cacheable("companies")
    @Override
    public List<CompanyDTO> getAllCompaniesForAdmin() {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(this::transformCompanyToDtoForAdmin).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }


    private CompanyDTO transformCompanyToDto(Company company) {
        List<JobDto> jobDtos = company.getJobs().stream()
                .map(this::transformJobToDto)
                .collect(Collectors.toList());
        return new CompanyDTO(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt(),jobDtos);
    }

    private JobDto transformJobToDto(Job job) {
        return new JobDto(
                job.getId(),
                job.getTitle(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCompany().getLogo(),
                job.getLocation(),
                job.getWorkType(),
                job.getJobType(),
                job.getCategory(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryCurrency(),
                job.getSalaryPeriod(),
                job.getDescription(),
                job.getRequirements(),
                job.getBenefits(),
                job.getPostedDate(),
                job.getApplicationDeadline(),
                job.getApplicationsCount(),
                job.getFeatured(),
                job.getUrgent(),
                job.getRemote(),
                job.getStatus()
        );
    }

    private Company transformCompanyDtoToEntity(CompanyDTO companyDTO){
        Company company = new Company();
        BeanUtils.copyProperties(companyDTO, company);
        return company;
    }

    private CompanyDTO transformCompanyToDtoForAdmin(Company company){
        return new CompanyDTO(company.getId(),
                company.getName(),
                company.getLogo(),
                company.getIndustry(),
                company.getSize(),
                company.getRating(),
                company.getLocations(),
                company.getFounded(),
                company.getDescription(),
                company.getEmployees(),
                company.getWebsite(),
                company.getCreatedAt(),
                null);
    }
}
