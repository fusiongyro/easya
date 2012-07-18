/*
 * Copyright (c) 2012, Daniel Lyons
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.storytotell.easya.ui;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storytotell.easya.annotations.LoggedIn;
import org.storytotell.easya.ejb.CourseManager;
import org.storytotell.easya.entity.Course;
import org.storytotell.easya.entity.User;
import org.storytotell.easya.events.CourseCreated;

/**
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Named("courseUI")
@RequestScoped
public class CourseUI {
  private static final Logger log = LoggerFactory.getLogger(CourseUI.class.getName());
  
  @EJB private CourseManager courseManager;
  @Inject @LoggedIn private User user;
  
  private Course course;
  private String shortCode;
  private Course newCourse = new Course();

  public Course getCourse() {
    return course;
  }
  
  public Course getNewCourse() {
    return newCourse;
  }
  
  @RequiresAuthentication
  @RequiresPermissions(value="course:create")
  public String saveNew() {
    log.debug("saving new course {}", newCourse.getName());
    newCourse.setOwner(user);
    courseManager.save(newCourse);
    shortCode = newCourse.getShortCode();
    return "pretty:course";
  }
  
  public boolean getCanDelete() {
    boolean permitted = SecurityUtils.getSubject().isPermitted("course:delete:" + course.getId());
    log.debug("determining that the current subject can{} delete course {}", permitted ? "" : "NOT", course.getName());
    return permitted;
  }
  
  public String delete() {
    log.debug("delete course called");
    // ensure this user can delete this course
    SecurityUtils.getSubject().checkPermission("course:delete:" + course.getId());
    log.debug("removing course");
    user.removeCourse(course);
    courseManager.remove(course);
    log.debug("going home");
    return "pretty:home";
  }
  
  public void save() { courseManager.update(course); }

  public String getShortCode() { return shortCode; }
  public void setShortCode(String shortCode) 
  {
    this.shortCode = shortCode; 
  }
  
  public void setName(String name) { course.setName(name); }
  
  public void logCourseCreated(@Observes CourseCreated created) {
    log.debug("just saw a course get created with name {0}", created.getNewCourse().getName());
  }

  public void loadFromShortCode() {
    log.debug("loading from short code {}", shortCode);
    if (shortCode != null)
      course = courseManager.findByShortCode(shortCode);
  }
}
