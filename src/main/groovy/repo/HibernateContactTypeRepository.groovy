package repo

import model.ContactType
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class HibernateContactTypeRepository implements ContactTypeRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        sessionFactory.getCurrentSession()
    }

    @Override
    @Transactional
    void addContactType(ContactType contactType) {
        currentSession.save(contactType)
    }

    @Override
    @Transactional
    List<ContactType> listContactTypes() {

        // в вызовах любых get-методов можно опускать префикс get и пустые скобки
        def result = currentSession.createQuery("from ContactType").list()

        // проверка, что список пустой, выглядит так
        if(!result){

            // Нужен List<Map<String, Object>>? Что может быть проще!
            def types = [
                    // кавычки для ключей не обязательны,
                    // значения могут быть любого типа
                    [name:'Family',     code:'family', defaulttype: false],
                    [name:'Job', code:'job', defaulttype: false],
                    [name:'Friends',    code:'stuff', defaulttype: true]
            ]

            // вместо цикла можно использовать замыкание
            types.each { type ->
                ContactType contactType = new ContactType(
                        // в любой Groovy-класс по умолчанию добавляется конструктор,
                        // принимающий параметром Map
                        code: type.code,
                        name : type.name,
                        defaulttype : type.defaulttype
                )
                currentSession.save(contactType)
                // перегруженный оператор << добавляет элемент в список
                // переменная result доступна в контексте замыкания
                result << contactType
            }
        }
        // ключевое слово return не обязательно
        result
    }

    @Override
    @Transactional
    void removeContactType(Integer id) {
        ContactType contactType = currentSession.get(ContactType.class, id) as ContactType
        if (contactType) {
            currentSession.delete(contactType)
        }
    }

    @Override
    @Transactional
    ContactType getDefault() {
        currentSession.createCriteria(ContactType.class).add(Restrictions.eq('defaulttype', true)).uniqueResult() as ContactType
    }
}