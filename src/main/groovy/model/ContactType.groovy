package model

import model.Contact

import javax.persistence.Basic
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "CONTACT_TYPES", uniqueConstraints = [
        @UniqueConstraint(columnNames = ["code"]),
        @UniqueConstraint(columnNames = ["name"])
])
class ContactType {

    @Id
    @GeneratedValue
    Integer id

    @Basic
    @Column(unique = true, nullable = true)
    String code

    @Basic
    @Column(unique = true, nullable = true)
    String name

    @Basic
    @Column(nullable = true)
    Boolean defaulttype = false

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.REFRESH, CascadeType.MERGE], mappedBy = "contacttype")
    List<Contact> contacts = null
}