package due.debugchain.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Project
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-05T11:48:51.982+02:00")

public class Project   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  /**
   * GitLab Status
   */
  public enum VisibilityEnum {
    PUBLIC("public"),
    
    INTERNAL("internal"),
    
    PRIVATE("private");

    private String value;

    VisibilityEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static VisibilityEnum fromValue(String text) {
      for (VisibilityEnum b : VisibilityEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("visibility")
  private VisibilityEnum visibility = null;

  @JsonProperty("maintainer")
  private Long maintainer = null;

  @JsonProperty("webUrl")
  private String webUrl = null;

  @JsonProperty("issuesEnabled")
  private Boolean issuesEnabled = null;

  public Project id(Long id) {
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

  public Project name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Project description(String description) {
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

  public Project visibility(VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * GitLab Status
   * @return visibility
  **/
  @ApiModelProperty(value = "GitLab Status")


  public VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public Project maintainer(Long maintainer) {
    this.maintainer = maintainer;
    return this;
  }

  /**
   * Get maintainer
   * @return maintainer
  **/
  @ApiModelProperty(value = "")


  public Long getMaintainer() {
    return maintainer;
  }

  public void setMaintainer(Long maintainer) {
    this.maintainer = maintainer;
  }

  public Project webUrl(String webUrl) {
    this.webUrl = webUrl;
    return this;
  }

  /**
   * Get webUrl
   * @return webUrl
  **/
  @ApiModelProperty(value = "")


  public String getWebUrl() {
    return webUrl;
  }

  public void setWebUrl(String webUrl) {
    this.webUrl = webUrl;
  }

  public Project issuesEnabled(Boolean issuesEnabled) {
    this.issuesEnabled = issuesEnabled;
    return this;
  }

  /**
   * Get issuesEnabled
   * @return issuesEnabled
  **/
  @ApiModelProperty(value = "")


  public Boolean isIssuesEnabled() {
    return issuesEnabled;
  }

  public void setIssuesEnabled(Boolean issuesEnabled) {
    this.issuesEnabled = issuesEnabled;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Project project = (Project) o;
    return Objects.equals(this.id, project.id) &&
        Objects.equals(this.name, project.name) &&
        Objects.equals(this.description, project.description) &&
        Objects.equals(this.visibility, project.visibility) &&
        Objects.equals(this.maintainer, project.maintainer) &&
        Objects.equals(this.webUrl, project.webUrl) &&
        Objects.equals(this.issuesEnabled, project.issuesEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, visibility, maintainer, webUrl, issuesEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Project {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    maintainer: ").append(toIndentedString(maintainer)).append("\n");
    sb.append("    webUrl: ").append(toIndentedString(webUrl)).append("\n");
    sb.append("    issuesEnabled: ").append(toIndentedString(issuesEnabled)).append("\n");
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

