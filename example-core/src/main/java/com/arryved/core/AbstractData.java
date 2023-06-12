package com.arryved.core;

//import com.google.cloud.Timestamp;
//import com.google.cloud.spring.data.spanner.core.mapping.Column;
//import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractData {
//  @PrimaryKey
//  @Column(name = "ID")
  protected String uuid;
  
//  @Column(name = "CREATED_ON")
//  protected final Timestamp createdOn;
  
  public AbstractData() {
    uuid = UUID.randomUUID().toString();
//    createdOn = Timestamp.now();
  }
  
}
