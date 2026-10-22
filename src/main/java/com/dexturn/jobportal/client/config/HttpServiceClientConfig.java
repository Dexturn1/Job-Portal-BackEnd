package com.dexturn.jobportal.client.config;


import com.dexturn.jobportal.client.service.PostService;
import com.dexturn.jobportal.client.service.TodoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(types =  {TodoService.class, PostService.class})
public class HttpServiceClientConfig {
    

}
