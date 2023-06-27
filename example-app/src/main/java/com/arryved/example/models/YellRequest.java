package com.arryved.example.models;

import com.arryved.core.AbstractModel;
import com.arryved.example.data.Echo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class YellRequest extends AbstractModel {
  public Echo echo;
}
