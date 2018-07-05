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
public class RestMartInfo {

    int martCode; // 마트코드
    int pointCode; // 지점코드
    String pointName; // 지점명
    String regionName; // 지역명
    String restDateInfo; // 휴무일
    String restDateInfoNext;
    String openHours; // 영업시간
    String phone;
    String location; // 주소
    double Latitude;
    double longitude;
    public int favusersCount = 0;
    public Map<String, Boolean> favusers = new HashMap<>();

    @Exclude
    String refkey;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("martCode", martCode);
        result.put("pointCode", pointCode);
        result.put("pointName", pointName);
        result.put("regionName", regionName);
        result.put("restDateInfo", restDateInfo);
        result.put("restDateInfoNext", restDateInfoNext);
        result.put("openHours", openHours);
        result.put("phone", phone);
        result.put("location", location);
        result.put("Latitude", Latitude);
        result.put("longitude", longitude);
        result.put("favusersCount", favusersCount);
        result.put("favusers", favusers);
        return result;
    }

    public RestMartInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

}
