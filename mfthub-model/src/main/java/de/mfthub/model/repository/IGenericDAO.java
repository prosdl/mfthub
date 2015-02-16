package de.mfthub.model.repository;

import java.io.Serializable;
import java.util.List;

public interface IGenericDAO<T, I extends Serializable> {

   public void delete(T obj);

   public void saveOrUpdate(T obj);

   Integer countAllWithProperty(String propertyName, Object value);

   Integer countAll();

   Class<T> getType();

   List<T> findAll();

   List<T> findByProperty(String propertyName, Object value);

   T findByPropertyUnique(String propertyName, Object value);

   T merge(T obj);

   T find(I id); 
}