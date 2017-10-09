package com.wangrg.java_lib.java_program.student_manage_system.bean;

import com.wangrg.java_lib.db2.Id;

/**
 * by wangrongjun on 2017/9/12.
 */
public class Location {

    @Id
    private Integer locationId;
    private String country;// 国家
    private String province;// 省（州）
    private String city;// 市
    private String area;// 区

    public Location() {
    }

    public Location(String country, String province, String city, String area) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.area = area;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return country + "  " + province + "  " + city + "  " + area;
    }
}
