/**
 * Created by Administrator on 2018/5/22.
 */
'use strict';
shopManageApp.controller('MendBillCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','udcModal', 'sessionStorage',function ($scope, close, MendSecurityComplaintService, title, initVal,udcModal,sessionStorage) {
    //$scope.modalTitle = title;
    $scope.vm = {
        mend: {},
        curUser:null,
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.doPrint = function () {
        var printData = document.getElementById("div_print").innerHTML; //获得 div 里的所有 html 数据

        window.document.body.innerHTML = printData;   //把 html 里的数据 复制给 body 的 html 数据 ，相当于重置了 整个页面的 内容
        window.print(); // 开始打印
        $scope.close(true);
        //var bdhtml=window.document.body.innerHTML;
        //var sprnstr = "<!--startprint-->";
        //var eprnstr = "<!--endprint-->";
        //var  prnhtml= bdhtml.substr(bdhtml.indexOf(sprnstr)+17);
        //var prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
        //window.document.body.innerHTML=prnhtml;
        //window.print();
        //window.document.body.innerHTML=bdhtml;//重新给页面内容赋值；
    };


    var init = function () {
        $scope.vm.mend = _.clone(initVal);
        $scope.vm.curUser = sessionStorage.getCurUser();
        console.info($scope.vm.curUser);
        console.info($scope.vm.mend);
    };

    init();
}]);