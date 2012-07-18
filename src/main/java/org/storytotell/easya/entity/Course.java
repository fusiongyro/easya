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
import javax.validation.constraints.NotNull;
import org.storytotell.easya.validations.annotations.ShortCode;

/**
 * Represents a particular class on a particular topic taught by a particular
 * teacher and happening over a particular range of time.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
public class Course implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long id;
  
  private @NotNull   String name;
  private            String description;
  private @ShortCode String shortCode;
  private            User   owner;

  public Long   getId()          { return id; }
  public String getName()        { return name; }
  public String getDescription() { return description; }
  public String getShortCode()   { return shortCode; }
  public User   getOwner()       { return owner; }
  
  public void setId(Long id)                     { this.id = id; }
  public void setName(String name)               { this.name = name; }
  public void setDescription(String description) { this.description = description; }
  public void setShortCode(String shortCode)     { this.shortCode = shortCode; }
  
  public void setOwner(User owner) {
    this.owner = owner;
    if (owner != null && !owner.getCourses().contains(this))
      owner.addCourse(this);
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Course)) {
      return false;
    }
    Course other = (Course) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.storytotell.easya.entity.Course[id=" + id + ", shortCode=" + shortCode + "]";
  }
}