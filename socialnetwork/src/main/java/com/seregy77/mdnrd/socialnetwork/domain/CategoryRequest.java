package com.seregy77.mdnrd.socialnetwork.domain;

import com.orientechnologies.orient.core.record.OVertex;
import lombok.Data;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
public class CategoryRequest {
    private long id;
    private String name;
    private String description;
}
