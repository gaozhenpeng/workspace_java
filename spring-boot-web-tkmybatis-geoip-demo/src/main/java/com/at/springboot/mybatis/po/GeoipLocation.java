package com.at.springboot.mybatis.po;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`geoip_location`")
public class GeoipLocation {
    @Id
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`geoname_id`")
    private Long geonameId;

    @Column(name = "`locale_code`")
    private String localeCode;

    @Column(name = "`continent_code`")
    private String continentCode;

    @Column(name = "`continent_name`")
    private String continentName;

    @Column(name = "`country_iso_code`")
    private String countryIsoCode;

    @Column(name = "`country_name`")
    private String countryName;

    @Column(name = "`subdivision_1_iso_code`")
    private String subdivision1IsoCode;

    @Column(name = "`subdivision_1_name`")
    private String subdivision1Name;

    @Column(name = "`subdivision_2_iso_code`")
    private String subdivision2IsoCode;

    @Column(name = "`subdivision_2_name`")
    private String subdivision2Name;

    @Column(name = "`city_name`")
    private String cityName;

    @Column(name = "`metro_code`")
    private String metroCode;

    @Column(name = "`time_zone`")
    private String timeZone;

    @Column(name = "`is_in_european_union`")
    private Byte isInEuropeanUnion;

    public static final String ID = "id";

    public static final String DB_ID = "id";

    public static final String GEONAME_ID = "geonameId";

    public static final String DB_GEONAME_ID = "geoname_id";

    public static final String LOCALE_CODE = "localeCode";

    public static final String DB_LOCALE_CODE = "locale_code";

    public static final String CONTINENT_CODE = "continentCode";

    public static final String DB_CONTINENT_CODE = "continent_code";

    public static final String CONTINENT_NAME = "continentName";

    public static final String DB_CONTINENT_NAME = "continent_name";

    public static final String COUNTRY_ISO_CODE = "countryIsoCode";

    public static final String DB_COUNTRY_ISO_CODE = "country_iso_code";

    public static final String COUNTRY_NAME = "countryName";

    public static final String DB_COUNTRY_NAME = "country_name";

    public static final String SUBDIVISION_1_ISO_CODE = "subdivision1IsoCode";

    public static final String DB_SUBDIVISION_1_ISO_CODE = "subdivision_1_iso_code";

    public static final String SUBDIVISION_1_NAME = "subdivision1Name";

    public static final String DB_SUBDIVISION_1_NAME = "subdivision_1_name";

    public static final String SUBDIVISION_2_ISO_CODE = "subdivision2IsoCode";

    public static final String DB_SUBDIVISION_2_ISO_CODE = "subdivision_2_iso_code";

    public static final String SUBDIVISION_2_NAME = "subdivision2Name";

    public static final String DB_SUBDIVISION_2_NAME = "subdivision_2_name";

    public static final String CITY_NAME = "cityName";

    public static final String DB_CITY_NAME = "city_name";

    public static final String METRO_CODE = "metroCode";

    public static final String DB_METRO_CODE = "metro_code";

    public static final String TIME_ZONE = "timeZone";

    public static final String DB_TIME_ZONE = "time_zone";

    public static final String IS_IN_EUROPEAN_UNION = "isInEuropeanUnion";

    public static final String DB_IS_IN_EUROPEAN_UNION = "is_in_european_union";
}