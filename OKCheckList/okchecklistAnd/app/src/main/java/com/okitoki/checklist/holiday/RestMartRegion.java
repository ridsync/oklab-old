package com.okitoki.checklist.holiday;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * Created by okc on 2016-05-29.
 */
@Data  @IgnoreExtraProperties
public class RestMartRegion {

    String regionName; // 지역명

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("regionName", regionName);
        return result;
    }

    public RestMartRegion() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

}
