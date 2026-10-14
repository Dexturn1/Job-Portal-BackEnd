package com.dexturn.jobportal.scopes;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Setter
@Getter
public class RequestScopedBean {

    private String userName;

    public RequestScopedBean() {
        System.out.println("RequestScopedBean Created");
    }
}
