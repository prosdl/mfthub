package de.mfthub.model.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDAO<T, I extends Serializable> implements
      IGenericDAO<T, I> {

   private Class<T> type;

   @Autowired
   private SessionFactory sessionFactory;

   public void setSessionFactory(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   protected SessionFactory getSessionFactory() {
      if (sessionFactory == null)
         throw new IllegalStateException(
               "SessionFactory has not been set on DAO before usage");
      return sessionFactory;
   }

   @Override
   public Class<T> getType() {
      return type;
   }

   @SuppressWarnings("unchecked")
   public GenericDAO() {
      this.type = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];
   }

   public Session getSession() {
      return getSessionFactory().getCurrentSession();
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<T> findAll() {
      return getSession().createCriteria(getType()).list();
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<T> findByProperty(String propertyName, Object value) {
      return getSession().createCriteria(getType())
            .add(Restrictions.eq(propertyName, value)).list();
   }

   @Override
   public T findByPropertyUnique(String propertyName, Object value) {
      return type.cast(getSession().createCriteria(getType())
            .add(Restrictions.eq(propertyName, value)).uniqueResult());
   }

   @Override
   public Integer countAll() {
      return ((Number) getSession().createCriteria(getType())
            .setProjection(Projections.rowCount()).uniqueResult()).intValue();
   }

   @Override
   public Integer countAllWithProperty(String propertyName, Object value) {
      return ((Number) getSession().createCriteria(getType())
            .add(Restrictions.eq(propertyName, value))
            .setProjection(Projections.rowCount()).uniqueResult()).intValue();
   }

   @Override
   public void delete(T obj) {
      getSession().delete(obj);
   }

   @Override
   public void saveOrUpdate(T obj) {
      getSession().saveOrUpdate(obj);
   }

   @Override
   public T merge(T obj) {
      return type.cast(getSession().merge(obj));
   }

   @SuppressWarnings("unchecked")
   @Override
   public T find(I id) {
      return (T) getSession().get(getType(), id);
   }

}