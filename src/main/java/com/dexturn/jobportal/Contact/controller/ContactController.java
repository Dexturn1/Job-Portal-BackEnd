package com.dexturn.jobportal.Contact.controller;


import com.dexturn.jobportal.Contact.service.ContactService;
import com.dexturn.jobportal.constanst.ApplicationConstants;
import com.dexturn.jobportal.dto.ContactRequestDto;
import com.dexturn.jobportal.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping(path = "/public", version = "1.0")
    public ResponseEntity<String> saveContact(@RequestBody @Valid ContactRequestDto contactRequestDto) {

        boolean isSaved = contactService.saveContact(contactRequestDto);

        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Request processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed");
        }

    }

    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs(){
        List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactMsgs();
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }


    @GetMapping(version = "1.0")
    public ResponseEntity<String> fetchOpenContacts(@RequestParam
                                                    @Validated @NotBlank(message = "Status can not be blank")
                                                        @Size(min = 4, message = "Status length should be of minimum 4")
                                                    @NotBlank(message = "Status can not be blank") String status) {
        return ResponseEntity.ok("These are the contacts with the given status: " + status);
    }


    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "ace") String sortDir){
        List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactMsgsWithSort(sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
    }


    @GetMapping("/page/admin")
    public ResponseEntity<Page<ContactResponseDto>> fetchNewContactMsgsWithPaginationAndSort(
           @RequestParam(defaultValue = "0") int pageNumber,
           @RequestParam(defaultValue = "10") int pageSize,
           @RequestParam(defaultValue = "createdAt") String sortBy,
           @RequestParam(defaultValue = "ace") String sortDir
    ){
        Page<ContactResponseDto> contactResponseDtoPage = contactService.fetchNewContactMsgsWithPaginationAndSort(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtoPage);
    }


    @PatchMapping("/{id}/status/admin")
    public ResponseEntity<String> closeContactMsg(@PathVariable String id){
        boolean isUpdated = contactService.closeContactMsg(Long.valueOf(id), ApplicationConstants.CLOSED_MESSAGE);

        if(isUpdated){
            return ResponseEntity.status(HttpStatus.OK).body("Contact message updated successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update contact message");
        }
    }



}
