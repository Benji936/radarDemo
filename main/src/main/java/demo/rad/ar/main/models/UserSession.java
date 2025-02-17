package demo.rad.ar.main.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class UserSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Ensure ID is auto-generated
    Long id;

    @Column(name = "user_segment")
    Integer userSegment;

    @JsonProperty("user_id")
    String userId;
    String timestamp;

    // Device Info
    @JsonProperty("device_type")
    String deviceType;
    @JsonProperty("is_touch_capable")
    Boolean isTouchCapable;
    String browser;
    @JsonProperty("browser_version")
    String browserVersion;
    String os;
    @JsonProperty("os_version")
    String osVersion;
    String device;
    @JsonProperty("device_brand")
    String deviceBrand;
    @JsonProperty("device_model")
    String deviceModel;
    @Column(length = 1000)
    @JsonProperty("user_agent_string")
    String userAgentString;

    // Language
    @JsonProperty("language_code")
    String languageCode;
    String language;
    @JsonProperty("language_locality")
    String languageLocality;

    // Screen Info
    String dimensions;
    @JsonProperty("view_area")
    Integer viewArea;
    @JsonProperty("screen_dimensions")
    String screenDimensions;
    @JsonProperty("screen_area")
    Integer screenArea;
    @JsonProperty("view_ratio")
    Double viewRatio;
    Integer height;
    Integer width;
    @JsonProperty("screen_height")
    Integer screenHeight;
    @JsonProperty("screen_width")
    Integer screenWidth;
    @JsonProperty("screen_orientation")
    String screenOrientation;

    // Browser Info
    @JsonProperty("browser_vendor")
    String browserVendor;
    @JsonProperty("browser_language")
    String browserLanguage;
    @JsonProperty("ad_blocker")
    Boolean adBlocker;
    @JsonProperty("network_speed")
    Double networkSpeed;

    // Platform & Battery
    String platform;
    @JsonProperty("save_data")
    Boolean saveData;
    @JsonProperty("battery_level")
    Integer batteryLevel;
    @JsonProperty("battery_charging")
    Boolean batteryCharging;

    // Time & Date
    @JsonProperty("day_of_week")
    String dayOfWeek;
    @JsonProperty("session_month")
    String sessionMonth;
    @JsonProperty("day_of_month")
    Integer dayOfMonth;
    @JsonProperty("session_hour")
    Integer sessionHour;
    String season;
    @JsonProperty("is_holiday")
    Boolean isHoliday;
    @JsonProperty("holiday_name")
    String holidayName;
    
    // Referer & UTM Data
    @JsonProperty("referer_url")
    String refererUrl;
    @JsonProperty("referer_domain")
    String refererDomain;
    @JsonProperty("product_id")
    String productId;
    @JsonProperty("store_id")
    String storeId;
    @JsonProperty("product_tag")
    String productTag;
    @JsonProperty("utm_source")
    String utmSource;
    @JsonProperty("utm_medium")
    String utmMedium;
    @JsonProperty("utm_campaign")
    String utmCampaign;
    @JsonProperty("utm_content")
    String utmContent;
    @JsonProperty("utm_term")
    String utmTerm;

    // Location
    String continent;
    String country;
    String region;
    String city;
    Double latitude;
    Double longitude;
    @JsonProperty("is_eu")
    Boolean isEu;
    String postal;
    @JsonProperty("is_capital")
    Boolean isCapital;

    // ISP & Currency
    String asn;
    String isp;
    String currency;
    @JsonProperty("currency_rate")
    Double currencyRate;

    // Environment
    Double elevation;
    Double temperature;
    Double humidity;
    @JsonProperty("apparent_temperature")
    Double apparentTemperature;
    @JsonProperty("is_day")
    Boolean isDay;
    Double precipitation;
    @JsonProperty("weather_code")
    Integer weatherCode;
    String timezone;
}
