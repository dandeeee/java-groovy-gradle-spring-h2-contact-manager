package service;

import model.ContactType;
import model.Contact;

import java.util.List;


public interface AppService {

    void addContact(Contact contact);

    List<Contact> listContact();

    void removeContact(Integer id);

    List<ContactType> listContactType();
}