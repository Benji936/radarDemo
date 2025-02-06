package demo.rad.ar.main.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userId;
    LocalDateTime timestamp;

    // Device Info
    String deviceType;
    Boolean isTouchCapable;
    String browser;
    String browserVersion;
    String os;
    String osVersion;
    String device;
    String deviceBrand;
    String deviceModel;
    String userAgentString;

    // Language
    String languageCode;
    String language;
    String languageLocality;

    // Screen Info
    String dimensions;
    Integer viewArea;
    String screenDimensions;
    Integer screenArea;
    Double viewRatio;
    Integer height;
    Integer width;
    Integer screenHeight;
    Integer screenWidth;
    String screenOrientation;

    // Browser Info
    String browserVendor;
    String browserLanguage;
    Boolean adBlocker;
    Double networkSpeed;

    // Platform & Battery
    String platform;
    Boolean saveData;
    Integer batteryLevel;
    Boolean batteryCharging;

    // Time & Date
    String dayOfWeek;
    String sessionMonth;
    Integer dayOfMonth;
    Integer sessionHour;
    String season;
    Boolean isHoliday;
    String holidayName;
    
    // Referer & UTM Data
    String refererUrl;
    String refererDomain;
    String productId;
    String storeId;
    String productTag;
    String utmSource;
    String utmMedium;
    String utmCampaign;
    String utmContent;
    String utmTerm;

    // Location
    String continent;
    String country;
    String region;
    String city;
    Double latitude;
    Double longitude;
    Boolean isEu;
    String postal;
    Boolean isCapital;

    // ISP & Currency
    String asn;
    String isp;
    String currency;
    Double currencyRate;

    // Environment
    Double elevation;
    Double temperature;
    Double humidity;
    Double apparentTemperature;
    Boolean isDay;
    Double precipitation;
    Integer weatherCode;
    String timezone;
}
