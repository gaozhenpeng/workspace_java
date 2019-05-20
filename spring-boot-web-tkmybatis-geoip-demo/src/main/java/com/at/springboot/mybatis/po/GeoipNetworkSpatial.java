package com.at.springboot.mybatis.po;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`geoip_network_spatial`")
public class GeoipNetworkSpatial {
    @Id
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`network`")
    private String network;

    @Column(name = "`network_start_ip`")
    private String networkStartIp;

    @Column(name = "`network_last_ip`")
    private String networkLastIp;

    @Column(name = "`network_start_integer`")
    private Long networkStartInteger;

    @Column(name = "`network_last_integer`")
    private Long networkLastInteger;

    @Column(name = "`geoname_id`")
    private Long geonameId;

    @Column(name = "`registered_country_geoname_id`")
    private Long registeredCountryGeonameId;

    @Column(name = "`represented_country_geoname_id`")
    private String representedCountryGeonameId;

    @Column(name = "`is_anonymous_proxy`")
    private Byte isAnonymousProxy;

    @Column(name = "`is_satellite_provider`")
    private Byte isSatelliteProvider;

    @Column(name = "`postal_code`")
    private String postalCode;

    @Column(name = "`latitude`")
    private BigDecimal latitude;

    @Column(name = "`longitude`")
    private BigDecimal longitude;

    @Column(name = "`accuracy_radius`")
    private Integer accuracyRadius;

    @Column(name = "`network_geometry`")
    private byte[] networkGeometry;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String NETWORK = "network";

    public static final String DB_NETWORK = "network";

    public static final String NETWORK_START_IP = "networkStartIp";

    public static final String DB_NETWORK_START_IP = "network_start_ip";

    public static final String NETWORK_LAST_IP = "networkLastIp";

    public static final String DB_NETWORK_LAST_IP = "network_last_ip";

    public static final String NETWORK_START_INTEGER = "networkStartInteger";

    public static final String DB_NETWORK_START_INTEGER = "network_start_integer";

    public static final String NETWORK_LAST_INTEGER = "networkLastInteger";

    public static final String DB_NETWORK_LAST_INTEGER = "network_last_integer";

    public static final String GEONAME_ID = "geonameId";

    public static final String DB_GEONAME_ID = "geoname_id";

    public static final String REGISTERED_COUNTRY_GEONAME_ID = "registeredCountryGeonameId";

    public static final String DB_REGISTERED_COUNTRY_GEONAME_ID = "registered_country_geoname_id";

    public static final String REPRESENTED_COUNTRY_GEONAME_ID = "representedCountryGeonameId";

    public static final String DB_REPRESENTED_COUNTRY_GEONAME_ID = "represented_country_geoname_id";

    public static final String IS_ANONYMOUS_PROXY = "isAnonymousProxy";

    public static final String DB_IS_ANONYMOUS_PROXY = "is_anonymous_proxy";

    public static final String IS_SATELLITE_PROVIDER = "isSatelliteProvider";

    public static final String DB_IS_SATELLITE_PROVIDER = "is_satellite_provider";

    public static final String POSTAL_CODE = "postalCode";

    public static final String DB_POSTAL_CODE = "postal_code";

    public static final String LATITUDE = "latitude";

    public static final String DB_LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    public static final String DB_LONGITUDE = "longitude";

    public static final String ACCURACY_RADIUS = "accuracyRadius";

    public static final String DB_ACCURACY_RADIUS = "accuracy_radius";

    public static final String NETWORK_GEOMETRY = "networkGeometry";

    public static final String DB_NETWORK_GEOMETRY = "network_geometry";
}