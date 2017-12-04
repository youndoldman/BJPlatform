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

    var name = "58531181@qq.com";
    var password = "123456";
    var config = {uname: name, pwd: password,debug: true, isAutoAnswer: true, stateListenerCallBack: foo};
    console.log(config);
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
};

function initCallback(data){
    console.info('issuccess : ' + data.successChange);
    console.info('msg : ' + data.msg);
    if(data.successChange){
        CallHelper.ready(stateCallBack);
        //alert('您已登录成功！');
    }else{
        //alert('登录失败，请检查账号密码!');
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

    document.getElementById('callstate').innerHTML = '签出';
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

    document.getElementById('callstate').innerHTML = '就绪';
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

    document.getElementById('callstate').innerHTML = '未就绪';
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

    document.getElementById('callstate').innerHTML = '示忙';
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

    document.getElementById('callstate').innerHTML = '振铃中';
}

function stateAnswer() {
    console.log(222);
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

    document.getElementById('callstate').innerHTML = '通话中';
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

    document.getElementById('callstate').innerHTML = '挂机';
    document.getElementById('callnumber').innerHTML = '&nbsp;';
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
