package uz.urinov.youtube.dto.attach;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AttachShortInfo {
    @JsonProperty
    private String id;

    @JsonProperty
    private String url;

    public AttachShortInfo(String id, String url) {
        this.id = id;
        this.url = url;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    // Optionally, add setters if needed
}