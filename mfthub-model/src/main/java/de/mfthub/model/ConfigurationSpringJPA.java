package de.mfthub.model;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("de.mfthub")
@PropertySource("classpath:hibernate.properties")
@EnableJpaRepositories("de.mfthub")
public class ConfigurationSpringJPA {

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
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
       LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
       entityManagerFactoryBean.setDataSource(dataSource());
       entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
       entityManagerFactoryBean.setPackagesToScan("de.mfthub.model.entities");
        
       entityManagerFactoryBean.setJpaProperties(hibProperties());
        
       return entityManagerFactoryBean;
   }

   private Properties hibProperties() {
       Properties properties = new Properties();
       properties.put("hibernate.dialect", hibernateDialect);
       properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
       properties.put("hibernate.show_sql", "true");
       properties.put("hibernate.format_sql", "true");
       return properties;
   }

   @Bean
   public JpaTransactionManager transactionManager() {
       JpaTransactionManager transactionManager = new JpaTransactionManager();
       transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
       return transactionManager;
   }
}
