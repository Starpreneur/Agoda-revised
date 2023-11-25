package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.example.Klouud.AgodaJson.Enum.ImageCategory;
import com.example.Klouud.AgodaJson.Enum.ImageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {


    private String description;
    private ImageType type;
    private String url;
    private ImageCategory imageCategory;
    private String defaultImage;
    //Which room does this image belong to
    private String roomTypeId;

}
