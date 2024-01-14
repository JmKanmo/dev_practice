package com.network.network_project.video.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoParameter {
    @NotNull
    @NotEmpty
    private final String savePath;

    @NotNull
    @NotEmpty
    private final String vodName;
}
