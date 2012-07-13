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
package org.storytotell.tpet.ui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.storytotell.tpet.ejb.CourseManager;
import org.storytotell.tpet.entity.Course;

/**
 * @author Daniel Lyons <fusion@storytotell.org>
 */
@Named("courseUI")
@RequestScoped
public class CourseUI {
  private static final Logger log = Logger.getLogger(CourseUI.class.getName());
  
  @EJB private CourseManager courseManager;
  
  private Course course;
  private String shortCode;
  private Course newCourse = new Course();

  public Course getCourse() {
    return course;
  }
  
  public Course getNewCourse() {
    return newCourse;
  }
  
  public void saveNew() {
    courseManager.save(newCourse);
  }

  public String getShortCode() { return shortCode; }
  public void setShortCode(String shortCode) 
  {
    log.log(Level.INFO, "assigning short code: {0}", shortCode);
    this.shortCode = shortCode; 
  }

  public void loadFromShortCode() {
    log.info("loading short code");
    if (shortCode != null)
      course = courseManager.findByShortCode(shortCode);
  }
  
  public void doSomething() {
    log.log(Level.INFO, "doing something, also our shortCode is {0}", shortCode);
  }
}
