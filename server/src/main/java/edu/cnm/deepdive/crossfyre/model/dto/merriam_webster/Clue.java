package edu.cnm.deepdive.crossfyre.model.dto.merriam_webster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * Represents a clue derived from the Merriam-Webster API response,
 * containing the word's definition and relevant metadata like the headword
 * and functional label (e.g., noun, verb).
 *
 * <p>Only the {@code def} field is serialized in API responses; the headword and
 * functional label are ignored for JSON purposes.</p>
 */
public class Clue {

  /**
   * The main word being defined (ignored during JSON serialization/deserialization).
   */
  @JsonIgnore
  private String headword;

  /**
   * The part of speech or grammatical label for the word (ignored during JSON serialization/deserialization).
   */
  @JsonIgnore
  private String functionalLabel;

  /**
   * The array of definition texts for the clue. Serialized from the {@code def} field in JSON.
   */
  @JsonProperty(value = "def", access = Access.READ_ONLY)
  private String[] definitionText;

  /**
   * Returns the headword of the clue.
   *
   * @return the headword string.
   */
  public String getHeadword() {
    return headword;
  }

  /**
   * Sets the headword of the clue.
   *
   * @param headword the main word to define.
   * @return the current {@link Clue} instance for method chaining.
   */
  public Clue setHeadword(String headword) {
    this.headword = headword;
    return this;
  }

  /**
   * Returns the functional label (e.g., part of speech).
   *
   * @return the functional label string.
   */
  public String getFunctionalLabel() {
    return functionalLabel;
  }

  /**
   * Sets the functional label (e.g., part of speech).
   *
   * @param functionalLabel the grammatical label to set.
   * @return the current {@link Clue} instance for method chaining.
   */
  public Clue setFunctionalLabel(String functionalLabel) {
    this.functionalLabel = functionalLabel;
    return this;
  }

  /**
   * Returns the array of definition texts.
   *
   * @return an array of clue definitions.
   */
  public String[] getDefinitionText() {
    return definitionText;
  }

  /**
   * Sets the array of definition texts.
   *
   * @param definitionText an array of definitions.
   * @return the current {@link Clue} instance for method chaining.
   */
  public Clue setDefinitionText(String[] definitionText) {
    this.definitionText = definitionText;
    return this;
  }
}
