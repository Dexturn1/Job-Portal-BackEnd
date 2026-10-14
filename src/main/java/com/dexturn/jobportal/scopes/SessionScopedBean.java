package com.dexturn.jobportal.scopes;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter @Setter
public class SessionScopedBean {
    private String userName;

    public SessionScopedBean(){
        System.out.println("RequestScopedBean created");
    }
}
