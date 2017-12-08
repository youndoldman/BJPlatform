package com.donno.nj.util;

public class DistanceHelper
{
    static Double EARTH_RADIUS = 6371.0;//km 地球半径 平均值，千米

    public static Double Distance(double lat1,double lon1, double lat2,double lon2)
    {
        //用haversine公式计算球面两点间的距离。
        //经纬度转换成弧度
        lat1 = ConvertDegreesToRadians(lat1);
        lon1 = ConvertDegreesToRadians(lon1);
        lat2 = ConvertDegreesToRadians(lat2);
        lon2 = ConvertDegreesToRadians(lon2);

        //差值
        Double vLon = Math.abs(lon1 - lon2);
        Double vLat = Math.abs(lat1 - lat2);

        //h is the great circle distance in radians, great circle就是一个球体上的切面，它的圆心即是球心的一个周长最大的圆。
        Double h = HaverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * HaverSin(vLon);

        Double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));

        return distance;
    }

    /// <summary>
    /// 将角度换算为弧度。
    /// </summary>
    /// <param name="degrees">角度</param>
    /// <returns>弧度</returns>
    public static Double ConvertDegreesToRadians(Double degrees)
    {
        return degrees * Math.PI / 180;
    }


    public static Double HaverSin(Double theta)
    {
        Double v = Math.sin(theta / 2);
        return v * v;
    }

}
