package service;

import repo.ContactTypeRepository;
import repo.ContactsRepository;
import model.ContactType;
import model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired ContactsRepository contactsRepo;
    @Autowired ContactTypeRepository contactTypeRepo;

    @Transactional
    public void addContact(Contact contact) {
        contactsRepo.addContact(contact);
    }

    @Transactional
    public List<Contact> listContact() {
        return contactsRepo.listContact();
    }

    @Transactional
    public void removeContact(Integer id) {
        contactsRepo.removeContact(id);
    }

    @Transactional
    public List<ContactType> listContactType() {
        return contactTypeRepo.listContactTypes();
    }

}