package de.mfthub.model;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("de.mfthub")
@PropertySource("classpath:hibernate-test.properties")
public class SimpleTestConfiguration {

   @Value("${hibernate.dialect}")
   private String hibernateDialect;

   @Value("${hibernate.hbm2ddl.auto}")
   private String hibernateHbm2ddlAuto;

   @Bean
   public DataSource dataSource() {
      return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .build();
   }

   @Bean
   public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
   }

   @Bean
   public SessionFactory sessionFactory() {
      return new LocalSessionFactoryBuilder(dataSource())
            .scanPackages("de.mfthub.model.entities")
            .setProperty("hibernate.dialect", hibernateDialect)
            .setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto)
            .setProperty("hibernate.show_sql", "true")
            .setProperty("hibernate.format_sql", "true").buildSessionFactory();
   }

   @Bean
   public PlatformTransactionManager annotationDrivenTransactionManager() {
      return new HibernateTransactionManager(sessionFactory());
   }

}
