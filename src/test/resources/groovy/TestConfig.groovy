//package groovy
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.ImportResource
//import org.springframework.context.annotation.PropertySource
//import org.springframework.orm.hibernate4.HibernateTransactionManager
//import org.springframework.orm.hibernate4.LocalSessionFactoryBean
//import org.springframework.transaction.annotation.EnableTransactionManagement
//
//@Configuration
//@ComponentScan(['main.java.repo', 'main.groovy.repo', 'main.java.web', 'main.java.service'])
//@PropertySource('classpath:test/resources/testdb.properties')
//@ImportResource('classpath:test/resources/security.xml')
//@EnableTransactionManagement
//class TestConfig {
//
//    @Value('${db.user.name}')
//    String userName
//
//    @Value('${db.user.pass}')
//    String userPass
//
//    @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//
//        LocalSessionFactoryBean bean = new LocalSessionFactoryBean()
//
////        Это возможно только в Hibernate 4!
//        bean.packagesToScan = [
//                "groovy.domain",
//                'java.domain'] as String[]
//
//        Properties props = new Properties()
//        props."hibernate.connection.driver_class" = "com.mysql.jdbc.Driver"
//        props."hibernate.connection.url" = "jdbc:mysql://localhost:3306/contactmanager"
//        props."hibernate.connection.username" = userName
//        props."hibernate.connection.password" = userPass
//        props."hibernate.dialect" = "org.hibernate.dialect.MySQLDialect"
//        props."hibernate.hbm2ddl.auto" = "update"
//
//        // кстати вот эта штука помогает, когда в тестах тормозит старт контекста на получении метаданных из БД (замечено на Постгрес)
//        // http://stackoverflow.com/questions/10075081/hibernate-slow-to-acquire-postgres-connection
//        props."hibernate.temp.use_jdbc_metadata_defaults" = "false"
//
//        bean.hibernateProperties = props
//
//        bean
//    }
//
//    @Bean
//    public HibernateTransactionManager transactionManager() {
//        HibernateTransactionManager txManager = new HibernateTransactionManager()
//        txManager.autodetectDataSource = false
//        txManager.sessionFactory = sessionFactory().object
//        txManager
//    }
//
//}
