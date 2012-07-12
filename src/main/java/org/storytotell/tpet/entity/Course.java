/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.storytotell.tpet.entity;

import java.io.Serializable;

/**
 *
 * @author fusion
 */
public class Course implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long id;
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Get the value of name
   *
   * @return the value of name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the value of name
   *
   * @param name new value of name
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
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
    return "org.storytotell.tpet.entity.Course[ id=" + id + " ]";
  }
}