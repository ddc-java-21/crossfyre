package edu.cnm.deepdive.crossfyre.model.dto.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

/**
 * Represents a simplified definition structure as returned by an external dictionary API.
 * This DTO is designed to deserialize JSON responses that include a list of short definitions.
 *
 * <p>Only non-null fields will be included during serialization. The expected JSON property
 * order is defined to ensure consistent output.</p>
 */
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"shortdef"})
public class Definition {

  /**
   * A list of short definitions associated with the word.
   * This corresponds to the {@code shortdef} field in the external JSON response.
   */
  @JsonProperty("shortdef")
  private List<String> shortDef;

  /**
   * Returns the list of short definitions.
   *
   * @return a list of short definitions, or {@code null} if not set.
   */
  public List<String> getShortDef() {
    return shortDef;
  }

  /**
   * Sets the list of short definitions.
   *
   * @param shortDef a list of short definitions to assign.
   */
  public void setShortDef(List<String> shortDef) {
    this.shortDef = shortDef;
  }
}
