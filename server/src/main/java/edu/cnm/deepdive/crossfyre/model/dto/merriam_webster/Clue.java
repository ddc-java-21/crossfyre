package edu.cnm.deepdive.crossfyre.model.dto.merriam_webster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Clue {

  @JsonIgnore
  private String headword;

  @JsonIgnore
  private String functionalLabel;

  @JsonProperty(value = "def", access = Access.READ_ONLY)
  private String[] definitionText;

  public String getHeadword() {
    return headword;
  }

  public Clue setHeadword(String headword) {
    this.headword = headword;
    return this;
  }

  public String getFunctionalLabel() {
    return functionalLabel;
  }

  public Clue setFunctionalLabel(String functionalLabel) {
    this.functionalLabel = functionalLabel;
    return this;
  }

  public String[] getDefinitionText() {
    return definitionText;
  }

  public Clue setDefinitionText(String[] definitionText) {
    this.definitionText = definitionText;
    return this;
  }



}
