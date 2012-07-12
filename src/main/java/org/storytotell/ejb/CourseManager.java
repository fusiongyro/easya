/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.storytotell.ejb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.storytotell.tpet.entity.Course;

/**
 *
 * @author fusion
 */
@Stateless
@LocalBean
public class CourseManager {

  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")
  
  @PersistenceContext
  private EntityManager entityManager;
  
  public Course findById(long id) {
    return entityManager.find(Course.class, id);
  }
  
}
