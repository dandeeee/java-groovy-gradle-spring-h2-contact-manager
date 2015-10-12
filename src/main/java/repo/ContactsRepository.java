package repo;

import model.Contact;

import java.util.List;

public interface ContactsRepository {

    void addContact(Contact contact);

    List<Contact> listContact();

    void removeContact(Integer id);
}