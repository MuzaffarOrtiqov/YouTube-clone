package uz.urinov.youtube.dto.attach;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
    /*id(uuid),origin_name,size,type (extension),path,duration*/
    private String id;
    private String originName;
    private Long size;
    private String extension;
    private String path;
    private Long duration;

}
