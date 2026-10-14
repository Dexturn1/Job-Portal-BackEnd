package com.dexturn.jobportal.scopes;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scope")
@RequiredArgsConstructor
public class ScopeController {

    private final RequestScopedBean requestScopedBean;
    private final SessionScopedBean sessionScopedBean;
    private final ApplicationScopeBean applicationScopeBean;


    @GetMapping("/request")
    public ResponseEntity<String> testRequestScope(){
        requestScopedBean.setUserName("John Doe");
        return ResponseEntity.ok().body(requestScopedBean.getUserName());
    }


    @GetMapping("/session")
    public ResponseEntity<String> testSessionScope(){
        sessionScopedBean.setUserName("John Doe");
        return ResponseEntity.ok().body(sessionScopedBean.getUserName());
    }

    @GetMapping("/application")
    public ResponseEntity<Integer> testApplicationScope(){
        applicationScopeBean.incrementVisitorCount();
        return ResponseEntity.ok().body(applicationScopeBean.getVisitorsCount());
    }

    @GetMapping("/test")
    public ResponseEntity<Integer> testScope(){
        return ResponseEntity.ok().body(applicationScopeBean.getVisitorsCount());
    }
}
