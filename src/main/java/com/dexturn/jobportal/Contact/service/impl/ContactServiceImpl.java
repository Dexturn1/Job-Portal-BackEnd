package com.dexturn.jobportal.Contact.service.impl;


import com.dexturn.jobportal.Contact.service.ContactService;
import com.dexturn.jobportal.constanst.ApplicationConstants;
import com.dexturn.jobportal.dto.ContactRequestDto;
import com.dexturn.jobportal.dto.ContactResponseDto;
import com.dexturn.jobportal.entity.Contact;
import com.dexturn.jobportal.repository.ContactRepository;
import com.dexturn.jobportal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    @Transactional()
    public boolean saveContact(ContactRequestDto contactRequestDto) {
        boolean result = false;
        Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
        if(contact.getId() != null) {
            result = true;
        }
        return result;
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgs() {
         List<Contact> contacts =    contactRepository.findContactsByStatusOrderByCreatedAtAsc
                 (ApplicationConstants.NEW_MESSAGE);

        return contacts.stream().map(this::transformToDto).toList();
    }

    @Override
    public List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir) {
        // create Sort Object based on sortBy and sortDir parameters

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        List<Contact> contacts = contactRepository.findContactsByStatus(ApplicationConstants.NEW_MESSAGE, sort);

       return contacts.stream().map(this::transformToDto).toList();
    }

    @Override
    public Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Create Sort object based on sortBy and sortDir parameters
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        // Create Pageable Object with page number, page size, and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        //Fetch paginated and sorted contacts form repository
        Page<Contact> contactPage = contactRepository.findContactsByStatus(
                ApplicationConstants.NEW_MESSAGE, pageable
        );

        // Transform Contact entities to ContactResponseDto
        Page<ContactResponseDto> responseDtosPage = contactPage.map(this::transformToDto);
        return responseDtosPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeContactMsg(Long id, String status) {
         int  updatedRow =  contactRepository.updateStatusById(status,id, ApplicationUtility.getLoggedInUser());    // Select

        return updatedRow > 0;


    }


    private ContactResponseDto transformToDto(Contact contact){

        return new ContactResponseDto(contact.getId(),
                contact.getName(), contact.getEmail(), contact.getUserType(), contact.getSubject(),
                contact.getMessage(), contact.getStatus(), contact.getCreatedAt());
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
//        contact.setCreatedAt(Instant.now());
//        contact.setCreatedBy("System");
        contact.setStatus("NEW");
        return contact;
    }
}