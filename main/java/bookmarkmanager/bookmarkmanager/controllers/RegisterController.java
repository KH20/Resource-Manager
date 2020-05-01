package bookmarkmanager.bookmarkmanager.controllers;

import bookmarkmanager.bookmarkmanager.data.User;
import bookmarkmanager.bookmarkmanager.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {

    /*
    Constants used for error messages
    */
    private static final String REG_ERROR_NULLS = "Form was incomplete";
    private static final String REG_ERROR_PASSWORD_MISMATCH = "Passwords do not match";
    private static final String REG_ERROR_UNKNOWN_ERROR = "Unknown Error";
    private static final String REG_ERROR_USERNAME_EXISTS = "Username already exists";

    private UserRepository repo;

    @Autowired
    public RegisterController(UserRepository r) {
        repo = r;
    }

    @GetMapping("/register")
    public ModelAndView register(HttpSession session) {

        /* If a session attribute SESSION_USERNAME is set then redirect
        the user to the homepage. This session attribute is set on
        successful login, so access to the register page after which should
        not be possible
        */
        if (session.getAttribute("SESSION_USERNAME") != null)
            return new ModelAndView("redirect:/");

        //Use register.html as the template to use
        ModelAndView mv = new ModelAndView();
        mv.setViewName("register");

        //Create an empty user object and attach to the model
        User u = new User();
        mv.addObject("user", u);

        return mv;
    }

    /*
    Attempt to register a new user based on what was submitted in the form
    on the register page
    */
    @PostMapping("/register")
    public ModelAndView registerUserAccount(@ModelAttribute("user") User user){
        ModelAndView mv = new ModelAndView("register");
        mv.addObject("user", user);

        List<String> errors = new ArrayList<>(0);

        String submittedUsername = user.getUsername();
        String submittedPassword = user.getPassword();
        String submittedCPassword = user.getcPassword();

        // BASIC FORM VALIDATION
        if (submittedUsername.isEmpty() || submittedPassword.isEmpty() || submittedCPassword.isEmpty())
            errors.add(REG_ERROR_NULLS);
        if (!submittedPassword.equals(submittedCPassword))
            errors.add(REG_ERROR_PASSWORD_MISMATCH);

        if (errors.size() > 0) {
            mv.addObject("errors", errors);
            return mv;
        }

        /*
            If the form is filled in correctly, move on to other
            potential errors.
        */
        if (repo.doesUserNameExist(user.getUsername())){
            System.out.println("USERNAME ERR");
            errors.add(REG_ERROR_USERNAME_EXISTS);
            mv.addObject("errors", errors);
            return mv;
        }

        boolean success = repo.addUser(user);
        if (!success){
            errors.add(REG_ERROR_UNKNOWN_ERROR);
            mv.addObject("errors", errors);
            return mv;
        }
        else {
            //User does not exist and was added to the database successfully.
            //Direct the user to now log in
            return new ModelAndView("redirect:/login");
        }
    }

}
