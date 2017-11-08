'use strict';

commonModule.filter('shortUserName',function(){
    return function(userName){
        return userName && (userName.indexOf('@') > -1)
                    && userName.slice(0, userName.indexOf('@'))
                || userName;
    }
});


commonModule.filter('lanFilter',function(){
    return function(locationStr){
        return locationStr && (locationStr.indexOf(',') > -1)
            && locationStr.slice(locationStr.indexOf(',')+1)
            || locationStr;
    }
});

commonModule.filter('lonFilter',function(){
    return function(locationStr){
        return locationStr && (locationStr.indexOf(',') > -1)
            && locationStr.slice(0, locationStr.indexOf(','))
            || locationStr;
    }
});
