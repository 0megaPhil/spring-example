package com.arryved.example.data;

import com.arryved.core.AbstractData;
//import com.google.cloud.spring.data.spanner.core.mapping.Column;
//import com.google.cloud.spring.data.spanner.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Table(name = "ECHOS")
public class Echo extends AbstractData {

//  @Column(name = "MESSAGE")
  private String message;
  
  public Echo(String message) {
//    super();
    this.message = message;
  }

}
