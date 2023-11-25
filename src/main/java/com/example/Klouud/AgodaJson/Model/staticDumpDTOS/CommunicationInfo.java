package com.example.Klouud.AgodaJson.Model.staticDumpDTOS;

import com.example.Klouud.AgodaJson.Enum.CommunicationCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationInfo {

    private String phone;//phone
    private String email;//email
    private String fax;//fax
    private String website;//website
    private String mobile;
    private CommunicationCategory communicationCategory;
}
