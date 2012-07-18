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
package org.storytotell.easya.entity;

import java.io.Serializable;
import java.util.List;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A user of this application.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
public class User implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String username, password, emailAddress;
  
  private List<Course> courses;
  private static final Logger log = LoggerFactory.getLogger(User.class);

  public String getUsername()      { return username; }
  public String getPassword()      { return password; }
  public String getEmailAddress()  { return emailAddress; }
  public List<Course> getCourses() { return courses; }

  public void setUsername(String username)         { this.username = username; }
  public void setPassword(String password)         { this.password = password; }
  public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
  public void setCourses(List<Course> courses)     { this.courses = courses; }

  public void addCourse(Course course) {
    courses.add(course);
    if (course.getOwner() != this)
      course.setOwner(this);
  }
  
  public void removeCourse(Course course) {
    courses.remove(course);
    course.setOwner(null);
  }

  @Override
  public String toString() {
    return "org.storytotell.easya.entity.User[" + username + "]";
  }

  public void copyPermissionsInto(SimpleAuthorizationInfo authz) {
    // alert that I can edit and delete this course
    for (Course c : getCourses()) {
      log.debug("Adding permission to edit and delete {} to authz for {}", c, username);
      authz.addStringPermission("course:edit:" + c.getId());
      authz.addStringPermission("course:delete:" + c.getId());
    }
  }
}
