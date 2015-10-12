package repo

import model.ContactType


interface ContactTypeRepository {

    void addContactType(ContactType contactType)
    List<ContactType> listContactTypes()
    void removeContactType(Integer id)
    ContactType getDefault()

}