package ru.streltsova.pet_project.api;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.streltsova.pet_project.models.Client;
import ru.streltsova.pet_project.security.ClientDetails;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

public class TestAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Client client = new Client();
        client.setId(3);
        client.setLogin("maver4ck@gmail.com");
        client.setPassword("password");
        ClientDetails clientDetails = new ClientDetails(client);

        Authentication authentication = new TestingAuthenticationToken(clientDetails, "password", List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
