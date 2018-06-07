package due.debugchain.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Issue
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-05T11:48:51.982+02:00")

public class Issue   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("projectId")
  private Long projectId = null;

  @JsonProperty("webUrl")
  private String webUrl = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("description")
  private String description = null;

  /**
   * GitLab Status
   */
  public enum StateEnum {
    OPENED("opened"),
    
    CLOSED("closed");

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

  @JsonProperty("approve")
  private Boolean approve = false;

  @JsonProperty("lockedAdress")
  private String lockedAdress = null;

  @JsonProperty("lockedGitLabId")
  private Long lockedGitLabId = null;

  @JsonProperty("complete")
  private Boolean complete = false;

  public Issue id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID from GitLab
   * @return id
  **/
  @ApiModelProperty(value = "ID from GitLab")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Issue projectId(Long projectId) {
    this.projectId = projectId;
    return this;
  }

  /**
   * ID from GitLab
   * @return projectId
  **/
  @ApiModelProperty(value = "ID from GitLab")


  public Long getProjectId() {
    return projectId;
  }

  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  public Issue webUrl(String webUrl) {
    this.webUrl = webUrl;
    return this;
  }

  /**
   * GitLab link
   * @return webUrl
  **/
  @ApiModelProperty(value = "GitLab link")


  public String getWebUrl() {
    return webUrl;
  }

  public void setWebUrl(String webUrl) {
    this.webUrl = webUrl;
  }

  public Issue title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(value = "")


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Issue description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Issue state(StateEnum state) {
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

  public Issue approve(Boolean approve) {
    this.approve = approve;
    return this;
  }

  /**
   * Get approve
   * @return approve
  **/
  @ApiModelProperty(value = "")


  public Boolean isApprove() {
    return approve;
  }

  public void setApprove(Boolean approve) {
    this.approve = approve;
  }

  public Issue lockedAdress(String lockedAdress) {
    this.lockedAdress = lockedAdress;
    return this;
  }

  /**
   * locked by user with pub-key
   * @return lockedAdress
  **/
  @ApiModelProperty(value = "locked by user with pub-key")


  public String getLockedAdress() {
    return lockedAdress;
  }

  public void setLockedAdress(String lockedAdress) {
    this.lockedAdress = lockedAdress;
  }

  public Issue lockedGitLabId(Long lockedGitLabId) {
    this.lockedGitLabId = lockedGitLabId;
    return this;
  }

  /**
   * locked by user with GitLab ID
   * @return lockedGitLabId
  **/
  @ApiModelProperty(value = "locked by user with GitLab ID")


  public Long getLockedGitLabId() {
    return lockedGitLabId;
  }

  public void setLockedGitLabId(Long lockedGitLabId) {
    this.lockedGitLabId = lockedGitLabId;
  }

  public Issue complete(Boolean complete) {
    this.complete = complete;
    return this;
  }

  /**
   * Get complete
   * @return complete
  **/
  @ApiModelProperty(value = "")


  public Boolean isComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Issue issue = (Issue) o;
    return Objects.equals(this.id, issue.id) &&
        Objects.equals(this.projectId, issue.projectId) &&
        Objects.equals(this.webUrl, issue.webUrl) &&
        Objects.equals(this.title, issue.title) &&
        Objects.equals(this.description, issue.description) &&
        Objects.equals(this.state, issue.state) &&
        Objects.equals(this.approve, issue.approve) &&
        Objects.equals(this.lockedAdress, issue.lockedAdress) &&
        Objects.equals(this.lockedGitLabId, issue.lockedGitLabId) &&
        Objects.equals(this.complete, issue.complete);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, projectId, webUrl, title, description, state, approve, lockedAdress, lockedGitLabId, complete);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Issue {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    projectId: ").append(toIndentedString(projectId)).append("\n");
    sb.append("    webUrl: ").append(toIndentedString(webUrl)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    approve: ").append(toIndentedString(approve)).append("\n");
    sb.append("    lockedAdress: ").append(toIndentedString(lockedAdress)).append("\n");
    sb.append("    lockedGitLabId: ").append(toIndentedString(lockedGitLabId)).append("\n");
    sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
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

