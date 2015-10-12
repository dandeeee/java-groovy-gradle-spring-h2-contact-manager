package repo;

import model.Contact;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateContactsRepository implements ContactsRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    ContactTypeRepository contactTypeRepository;

    public void addContact(Contact contact) {
        if(contact.getContacttype() == null){
            contact.setContacttype(contactTypeRepository.getDefault());
        }

        sessionFactory.getCurrentSession().save(contact);
    }

    @SuppressWarnings("unchecked")
    public List<Contact> listContact() {
        return sessionFactory.getCurrentSession().createQuery("from Contact").list();
    }

    public void removeContact(Integer id) {
        Contact contact = (Contact) sessionFactory.getCurrentSession().get(Contact.class, id);
        if (null != contact) {
            sessionFactory.getCurrentSession().delete(contact);
        }
    }

}