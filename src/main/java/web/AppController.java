package web;

import model.Contact;
import service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private AppService appService;

    @RequestMapping("/index")
    public String listContacts(Map<String, Object> map) {

        map.put("contact", new Contact());
        map.put("contactTypeList", appService.listContactType());
        map.put("contactList", appService.listContact());

        return "contact";
    }

    @RequestMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @RequestMapping("/lang={locale}")
    public String changeLocale(@PathVariable("locale") String locale,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(locale));

        return "redirect:/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addContact(@ModelAttribute("contact") Contact contact, BindingResult result) {

        appService.addContact(contact);

        return "redirect:/index";
    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") Integer contactId) {

        appService.removeContact(contactId);

        return "redirect:/index";
    }

    @ModelAttribute("contact")
    public Contact createModel() {
        return new Contact();
    }
}