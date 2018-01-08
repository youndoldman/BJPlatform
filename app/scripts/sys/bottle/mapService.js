'use strict';

bottleApp.service('MapService', ['$http', 'URI', 'MISC', 'promiseWrapper', function ($http, URI, MISC, promise) {

    var geocodeUri = URI.resources.geocode;
    var key = MISC.keys.gaodeKey;

    //地理编码经纬度查询
    this.retrieveLocation = function (address) {
        return promise.wrap($http.get(geocodeUri+"?key="+key+"&address="+address));
    };


}]);

