package edu.cnm.deepdive.crossfyre.model.dto.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"shortdef"})
public class Definition {

  @JsonProperty("shortdef")
  private List<String> shortDef;

}
