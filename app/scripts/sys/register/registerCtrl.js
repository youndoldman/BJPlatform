/**
 * Created by T470P on 2017/9/5.
 */
'use strict';

registerApp.controller('registerCtrl', function($scope,$interval) {
        $scope: {
            $scope.userName;
            $scope.passwd;
            $scope.passwdConfirm;
            $scope.tel;
            $scope.verifiCode;
            $scope.phoneVerifiCode;
            $scope.protocol;

            $scope.realName;
            $scope.idNumber;
            $scope.addProvince;
            $scope.addCity;
            $scope.addDistrict;
            $scope.addStreet;
            $scope.detailAdd;

            $scope.company;
            $scope.license;
            $scope.licenseImagePath;
            $scope.taxpayerID;
            $scope.bankAccount;
            $scope.licenseImage;
            $scope.companyAddProvince;
            $scope.companyAddCity;
            $scope.companyAddDistrict;
            $scope.companyAddStreet;
            $scope.detailCompanyAdd;
        };

        $scope.paracont = "获取验证码";
        $scope.paraclass = "but_null";
        $scope.paraevent = true;
        var second = null, timePromise = undefined;
        $scope.getCode = function () {
            var mobile = $scope.tel;
            if (second === null) {
                second = 60;
                if (mobile == undefined || mobile == "") {
                    $scope.mobiletest = "请输入手机号码";
                    return false;
                } else {
                    $scope.mobiletest = "";
                    timePromise = $interval(function () {
                        if (second <= 0) {
                            $interval.cancel(timePromise);
                            timePromise = undefined;
                            second = null;
                            $scope.paracont = "重发验证码";
                            $scope.paraclass = "but_null";
                            $scope.paraevent = true;
                        } else {
                            $scope.paracont = second + "s";
                            second--;
                        }
                    }, 1000, 100);
                }
            } else {
                return false;
            }
        };
    ["$http", function($http){
        var self = this;
        self.item = [];
        $http.get('/api/note').then(
            $scope.submitInfo= function(response){
                self.item = response.data;
                $scope.href = "RegisterStep4.html";
            },
            function(errResponse){
                console.error("Error while fetching notes");
            }
        );}];
    });