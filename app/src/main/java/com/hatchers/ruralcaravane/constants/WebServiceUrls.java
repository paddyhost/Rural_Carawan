package com.hatchers.ruralcaravane.constants;

/**
 * Created by Nikam on 19/11/2017.
 */
public class WebServiceUrls
{

    public static final String urlUserLogin="http://hatchers.in/caravan/index.php/api/v1/login";
    public static final String urlGetCityList = "http://www.hatchers.in/caravan/index.php/api/v1/getCityList";
    public static final String urlGetVillageList = "http://www.hatchers.in/caravan/index.php/api/v1/getVillageList";
    public static final String urlAddCustomer = "http://www.hatchers.in/caravan/index.php/api/V1/addNewCustomerInquery";


    //http://www.hatchers.in/caravan/index.php/api/v1/getVillageList?city_id=1&format=json

   // http://www.hatchers.in/caravan/index.php/api/v1/getCityList?format=json

    //http://www.hatchers.in/caravan/index.php/api/V1/addNewCustomerInquery?customerid=vbcvv&adharid=1576&name=hhdhd&address=sgdhgd&age=24&gender=F&mobile=9975294782&villageid=1&citi_id=2&added_date=12-12-2018&addedby_id=1&format=json&mobile=9975294782&password=user@123&ufile=image
//{"status":"success","count":1,"type":"addNewCustomerInquery","result":{"id":"17","customerid":"vbcvv","adharid":"1576","name":"hhdhd","address":"sgdhgd","age":"24","gender":"F","mobile":"9975294782","villageid":"1","citi_id":"2","added_date":"0000-00-00 00:00:00","uploaddate":"2018-01-14 11:37:43","updateDate":"2018-01-14 04:37:43","addedby_id":"9975294782","imagepath":""},"message":"Customer added successfully"}
}
