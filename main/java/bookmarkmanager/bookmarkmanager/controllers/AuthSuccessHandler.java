package bookmarkmanager.bookmarkmanager.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class AuthSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException  {

        /*
            Set the username of the user as a session attribute
        */
        String user = ((User) authentication.getPrincipal()).getUsername();
        request.getSession().setAttribute("SESSION_USERNAME", user);

        /* Debug example. Get the list of authorities attached to a user.
         In this project ROLE_USER is the only authority group a user can
         be a part of.
         */
        /*
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities)
            System.out.println(grantedAuthority);
         */

        //Send the user to home page
        new DefaultRedirectStrategy().sendRedirect(request, response, "/");
    }
}
