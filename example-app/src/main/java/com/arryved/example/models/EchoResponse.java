package com.arryved.example.models;

import com.arryved.core.AbstractModel;
import com.arryved.example.data.Echo;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EchoResponse extends AbstractModel {
  private Echo echo;
}
