'use strict';

customServiceApp.controller('WebPhoneCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', 'OrderService','sessionStorage','$timeout',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                                      rootService, pager, udcModal, CustomerManageService,OrderService,sessionStorage,$timeout) {



        $scope.vm = {
            cloudUser:{},
            callOutPhone:null
        };


        var searchCloudUsers = function () {
            $scope.currentUser = sessionStorage.getCurUser();
            var queryParams = {
                panvaUserId: $scope.currentUser.userId,
            };

            CustomerManageService.retrieveCloudUsers(queryParams).then(function (cloudUsers) {
                if(cloudUsers.total!=0){
                    $scope.vm.cloudUser = cloudUsers.items[0];
                    sessionStorage.setKTYCurUser(cloudUsers);
                    //呼叫中心初始化
                    callCenterLogin();
                }else{
                    udcModal.info({"title": "错误信息", "message": "该用户没有绑定云客服账号！ "});
                }
                console.log($scope.vm.cloudUser);
            });


        };

        var init = function () {
            searchCloudUsers();
        };
        $timeout(function(){
            init();
        },300);

        $scope.$watch('vm.callOutPhone',function(newValue,oldValue){
            $rootScope.$broadcast("Event_CallOutPhoneChanged", newValue);
        });

        //电话条的集成
        function foo(data) {
            console.log(data.msg);
            if(data.msg === "READY"){
                stateReady();
            }else if(data.msg === "NOTREADY" || data.msg === "REGISTER"){
                stateNotReady();
            }else if(data.msg === "BUSY"){
                stateBusy();
            }else if(data.msg === "RINGING"){
                stateRing();
            }else if(data.msg === "ANSWERED"){
                stateAnswer();
            }else if(data.msg === "HANGUP"){
                stateHangup();
            }else if(data.msg === "HOLD"){
                stateHold();
            }else if(data.msg === "LOGOUT"){
                stateLogout();
                alert('您已登出！');
            }

        };

        function callCenterLogin(){

            //var name = "58531181@qq.com";
            //var password = "123456";
            var name = $scope.vm.cloudUser.userId;
            var password = $scope.vm.cloudUser.password;



            var config = {
                uname: name,
                pwd: password,
                debug: true,
                isAutoAnswer: true,
                forceAnswerWhenRing: false,
                autoReady: false,
                stateListenerCallBack: foo,
                popScreanCallback: popScrean};
            CallHelper.init(config, initCallback);

            document.getElementById('register').addEventListener('click', function () {
                CallHelper.register();
            }, false);

            document.getElementById('unregister').addEventListener('click', function () {
                CallHelper.unregister();
            }, false);

            document.getElementById('ready').addEventListener('click', function () {
                CallHelper.ready(stateCallBack);
            }, false);

            document.getElementById('notready').addEventListener('click', function () {
                CallHelper.notready(stateCallBack);
            }, false);

            document.getElementById('busy').addEventListener('click', function () {
                CallHelper.busy(stateCallBack);
            }, false);

            document.getElementById('makecall').addEventListener('click', function () {
                var callnum = document.getElementById('tel_num').value.replace(/\s/g,'');
                CallHelper.invite(callnum, inviteCallBack);
            }, false);

            document.getElementById('answer').addEventListener('click', function () {
                CallHelper.answer();
            }, false);

            document.getElementById('hold').addEventListener('click', function () {
                CallHelper.hold();
            }, false);

            document.getElementById('unhold').addEventListener('click', function () {
                CallHelper.unhold();
            }, false);

            document.getElementById('hangup').addEventListener('click', function () {
                CallHelper.hangup();
            }, false);

            document.getElementById('transfer').addEventListener('click', function(){
                var thirdNum = document.getElementById('tel_num').value.replace(/\s/g,'');;
                CallHelper.transfer(thirdNum);
            }, false);

            document.getElementById('threeway').addEventListener('click', function(){
                var thirdNum = document.getElementById('tel_num').value.replace(/\s/g,'');;
                CallHelper.threeCall(thirdNum);
            }, false);
        };

        function popScrean(data){
            document.getElementById('callnumber').innerHTML = data;
            //广播来电事件
            $rootScope.$broadcast("Event_Callin", data);
        }

        function initCallback(data){
            console.info('issuccess : ' + data.successChange);
            console.info('msg : ' + data.msg);
            console.info('agentnumber: ' + data.data.agentnumber);
            if(data.successChange){
                CallHelper.ready(stateCallBack);
                //alert('您已登录成功！');
                document.getElementById('agentnumber').innerHTML = data.data.agentnumber;
            }else{
                alert('登录失败，请检查账号密码!');
            }
        }

        function inviteCallBack(data){
            console.log("call state is :" + data.state);
            console.log("uuid is :" + data.msg);
        }

        function stateCallBack(data){
            console.info("successChange is :" + data.successChange);
            console.info("changeState is :" + data.changeState);
        }


        function stateLogout() {
            setButtonUseable('register');
            setButtonDisabled('unregister');
            setButtonDisabled('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonDisabled('answer');
            setButtonDisabled('hold');
            setButtonDisabled('unhold');
            setButtonDisabled('hangup');

            setButtonDisplayBlock('register');
            setButtonDisplayNone('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '签出';
            document.getElementById('agentnumber').innerHTML = '&nbsp;';
            time_stop();
        }

        function stateReady() {
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonDisabled('ready');
            setButtonUseable('notready');
            setButtonUseable('busy');
            setButtonUseable('makecall');
            setButtonDisabled('answer');
            setButtonDisabled('hold');
            setButtonDisabled('unhold');
            setButtonDisabled('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '就绪';

            //状态放浏览器缓存中，如果是上个状态没变化 不要执行这个
            time_stop();
            time_run();
        }

        function stateNotReady() {
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonUseable('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonDisabled('answer');
            setButtonDisabled('hold');
            setButtonDisabled('unhold');
            setButtonDisabled('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '离席';
            time_stop();
            time_run();
        }

        function stateBusy() {
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonUseable('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonDisabled('answer');
            setButtonDisabled('hold');
            setButtonDisabled('unhold');
            setButtonDisabled('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '示忙';
            time_stop();
            time_run();
        }

        function stateRing() {
            console.log(111);
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonDisabled('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonUseable('answer');
            setButtonDisabled('hold');
            setButtonDisabled('unhold');
            setButtonUseable('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '振铃中';
            document.getElementById('ringAudio').play();
            time_stop();
            time_run();
        }

        function stateAnswer() {
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonDisabled('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonDisabled('answer');
            setButtonUseable('hold');
            setButtonDisabled('unhold');
            setButtonUseable('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonUseable('transfer');
            setButtonUseable('threeway');

            document.getElementById('callstate').innerHTML = '通话中';
            document.getElementById('ringAudio').pause();
            time_stop();
            time_run();
        }

        function stateHold() {
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonDisabled('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonDisabled('answer');
            setButtonDisabled('hold');
            setButtonUseable('unhold');
            setButtonDisabled('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayNone('hold');
            setButtonDisplayBlock('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '保持中';
        }

        function stateHangup() {
            setButtonDisabled('register');
            setButtonUseable('unregister');
            setButtonUseable('ready');
            setButtonDisabled('notready');
            setButtonDisabled('busy');
            setButtonDisabled('makecall');
            setButtonDisabled('answer');
            setButtonDisabled('hold');
            setButtonDisabled('unhold');
            setButtonDisabled('hangup');

            setButtonDisplayNone('register');
            setButtonDisplayBlock('unregister');
            setButtonDisplayBlock('hold');
            setButtonDisplayNone('unhold');

            setButtonDisabled('transfer');
            setButtonDisabled('threeway');

            document.getElementById('callstate').innerHTML = '挂机';
            document.getElementById('callnumber').innerHTML = '&nbsp;';
            document.getElementById('ringAudio').pause();
            time_stop();
        }

        function setButtonUseable(buttonId) {
            document.getElementById(buttonId).disabled = false;
            document.getElementById(buttonId).className = buttonId;
        }

        function setButtonDisabled(buttonId) {
            document.getElementById(buttonId).disabled = true;
            document.getElementById(buttonId).className = buttonId + '-disabled';
        }

        function setButtonDisplayNone(buttonId) {
            document.getElementById(buttonId).style.display = 'none';
        }

        function setButtonDisplayBlock(buttonId) {
            document.getElementById(buttonId).style.display = 'block';
        }


        function two_char(n) {
            return n >= 10 ? n : '0'+n;
        }

        var timer;
        function time_run() {
            var sec = 0;
            timer = setInterval(function () {
                sec ++;
                var date = new Date(0, 0);
                date.setSeconds(sec);
                var h = date.getHours(), m = date.getMinutes(), s = date.getSeconds();
                document.getElementById('calltime').innerHTML = two_char(h) + ':' + two_char(m) + ':' + two_char(s);
            }, 1000);
        }
        function time_stop() {
            clearInterval(timer);
        }




    }]);
