package due.debugchain.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * IssueReviewer
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-05T11:48:51.982+02:00")

public class IssueReviewer   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("issueId")
  private Long issueId = null;

  @JsonProperty("userId")
  private Long userId = null;

  /**
   * GitLab Status
   */
  public enum StateEnum {
    CONFIRMED("confirmed"),
    
    REJECTED("rejected"),
    
    SENTBACK("sentback");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("state")
  private StateEnum state = null;

  public IssueReviewer id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public IssueReviewer issueId(Long issueId) {
    this.issueId = issueId;
    return this;
  }

  /**
   * Get issueId
   * @return issueId
  **/
  @ApiModelProperty(value = "")


  public Long getIssueId() {
    return issueId;
  }

  public void setIssueId(Long issueId) {
    this.issueId = issueId;
  }

  public IssueReviewer userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  **/
  @ApiModelProperty(value = "")


  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public IssueReviewer state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * GitLab Status
   * @return state
  **/
  @ApiModelProperty(value = "GitLab Status")


  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IssueReviewer issueReviewer = (IssueReviewer) o;
    return Objects.equals(this.id, issueReviewer.id) &&
        Objects.equals(this.issueId, issueReviewer.issueId) &&
        Objects.equals(this.userId, issueReviewer.userId) &&
        Objects.equals(this.state, issueReviewer.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, issueId, userId, state);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IssueReviewer {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    issueId: ").append(toIndentedString(issueId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

