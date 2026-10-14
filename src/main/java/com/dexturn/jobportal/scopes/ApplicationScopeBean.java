package com.dexturn.jobportal.scopes;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
@Getter
@Setter
public class ApplicationScopeBean {

    private int visitorsCount;

    public ApplicationScopeBean(){
        System.out.println("ApplicationScopedBean created");
    }


    public void incrementVisitorCount(){
        visitorsCount++;
    }

}
