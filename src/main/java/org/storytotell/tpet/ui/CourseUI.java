package org.storytotell.tpet.ui;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.inject.Named;
import org.storytotell.tpet.ejb.CourseManager;
import org.storytotell.tpet.entity.Course;

/**
 *
 * @author fusion
 */
@Named("courseUI")
@RequestScoped
public class CourseUI {
  @EJB private CourseManager courseManager;
  
  @Inject @New private Course newCourse;
  
  public Course getCurrent() {
    return courseManager.findById(1);
  }

  public Course getNewCourse() {
    return newCourse;
  }
  
  public void saveNew() {
    courseManager.save(newCourse);
  }
}
