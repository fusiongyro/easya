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
package org.storytotell.easya.ejb;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storytotell.easya.annotations.Logged;
import org.storytotell.easya.entity.Course;
import org.storytotell.easya.entity.FileUpload;
import org.storytotell.easya.events.CourseCreated;
import org.storytotell.easya.events.CourseDeleted;
import org.storytotell.easya.events.FileUploaded;

/**
 * Finds specific courses and courses relevent to certain uses and contexts.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Stateless
@LocalBean
@Named("CourseManager")
@Logged
public class CourseManager {
  private static final Logger log = LoggerFactory.getLogger(CourseManager.class);
  @PersistenceContext private EntityManager entityManager;
  
  private @Inject Event<CourseCreated> courseCreatedEvent;
  private @Inject Event<CourseDeleted> courseDeletedEvent;
  private @Inject Event<FileUploaded>  fileUploadedEvent;
  
  public Course findById(long id) {
    return entityManager.find(Course.class, id);
  }

  /**
   * Save a brand new course to the database.
   */
  public void save(Course course) {
    // this creates the course
    entityManager.persist(course);
    
    // this causes the "owner"'s list of courses to be persisted as well
    entityManager.merge(course.getOwner());
    
    courseCreatedEvent.fire(new CourseCreated(course));
  }

  /**
   * Persist changes to this course to the database.
   */
  public void update(Course course) { 
    entityManager.merge(course);
  }

  /**
   * Locate a course by the short code (e.g.: CS-113).
   */
  public Course findByShortCode(String shortCode) {
    return entityManager
            .createQuery("SELECT c FROM Course c WHERE c.shortCode = :shortCode", Course.class)
            .setParameter("shortCode", shortCode)
            .getSingleResult();
  }

  /**
   * Get a list of interesting courses for the purpose of display on the front 
   * page. The definition of this is intentionally left vague for future 
   * refinement.
   */
  public List<Course> getFrontPageCourses() {
    String query = "SELECT c FROM Course c";
    return entityManager.createQuery(query, Course.class).getResultList();
  }

  public String getHello() { return "Hello!"; }
  
  public void remove(Course course) {
    course = entityManager.merge(course);
    entityManager.remove(course);
    courseDeletedEvent.fire(new CourseDeleted(course));
  }

  public void uploadFile(Course course, FileUpload upload) {
    course.addFileUpload(upload);
    entityManager.merge(course);
    fileUploadedEvent.fire(new FileUploaded(upload));
  }
}
