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
import javax.activation.MimeType;

/**
 * Represents a file upload for a particular course.
 * 
 * @author Daniel Lyons <fusion@storytotell.org>
 */
public class FileUpload implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Long     id;
  private Course   course;
  private String   filename;
  private String   type;
  private byte[]   content;

  public Long     getId()       { return id;       }
  public Course   getCourse()   { return course;   }
  public String   getFilename() { return filename; }
  public String   getType()     { return type; }
  public byte[]   getContent()  { return content;  }

  public void setId(Long id)                 { this.id       = id;       }
  public void setFilename(String filename)   { this.filename = filename; }
  public void setType(String type)           { this.type     = type;     }
  public void setContent(byte[] content)     { this.content  = content;  }

  public void setCourse(Course course) {
    this.course = course;
    if (course != null && !course.getUploads().contains(this))
      course.addFileUpload(this);
  }
}