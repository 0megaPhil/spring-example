package com.arryved.example.data;

import com.arryved.core.AbstractData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Echo extends AbstractData {
  private String message;
}
