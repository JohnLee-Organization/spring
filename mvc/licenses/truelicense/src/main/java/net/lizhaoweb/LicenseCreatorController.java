/*
 * Copyright (c) 2023, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb
 * @date : 2023-08-03
 * @time : 17:42
 */
package net.lizhaoweb;

import com.alibaba.fastjson2.util.IOUtils;
import net.lizhaoweb.lic.truelicense.s.AbstractServerInfos;
import net.lizhaoweb.lic.truelicense.s.LicenseCreator;
import net.lizhaoweb.lic.truelicense.s.LinuxServerInfos;
import net.lizhaoweb.lic.truelicense.s.WindowsServerInfos;
import net.lizhaoweb.lic.truelicense.vo.LicenseCheckModel;
import net.lizhaoweb.lic.truelicense.vo.LicenseCreatorParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成证书
 * <p>
 * Created by Jhon.Lee on 8/3/2023 5:42 PM
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 0.0.1
 * @email 404644381@qq.com
 */
@RestController("net.lizhaoweb.LicenseCreatorController")
@RequestMapping("/license")
public class LicenseCreatorController {

    private final static String CHARSET_OUT = "UTF-8";

    private final static int CACHE_SIZE = 4096;
    private static byte[] BUFFER_CACHE = new byte[CACHE_SIZE];

    /**
     * 证书生成路径
     */
    @Value("${license.licensePath:/license/license.lic}")
    private String licensePath;

    /**
     * 获取服务器硬件信息
     *
     * @param osName 操作系统类型，如果为空则自动判断
     * @return com.ccx.models.license.LicenseCheckModel
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        return abstractServerInfos.getServerInfos();
    }

    /**
     * 生成证书
     *
     * @param param 生成证书需要的参数
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, Object> generateLicense(@RequestBody LicenseCreatorParam param) {
        Map<String, Object> resultMap = new HashMap<>(2);

        if (StringUtils.isBlank(param.getLicensePath())) {
            param.setLicensePath(licensePath);
        }

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();

        if (result) {
            resultMap.put("result", "ok");
            resultMap.put("msg", param);
        } else {
            resultMap.put("result", "error");
            resultMap.put("msg", "证书文件生成失败！");
        }

        return resultMap;
    }

    @GetMapping
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=" + CHARSET_OUT);
            response.setCharacterEncoding(CHARSET_OUT);
            out = response.getWriter();
            out.print("<!DOCTYPE html><html lang=\"zh\">");
            out.print("<head>");
            out.print("<meta charset=\"utf-8\">");
            out.print("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.print("<meta name=\"renderer\" content=\"webkit\">");
            out.print("<title>许可证</title>");
            out.print("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
            out.print("<style type=\"text/css\">#server_info{width:100%;height:auto;}</style>");
            this.printJS4Jquery(out); // Jquery v3.6.3
            out.print("<script type=\"text/javascript\" charset=\"UTF-8\">");
            out.print("jQuery(document).ready(function (docEvent) {");
            out.print("jQuery('#look_server_info').click(function (e) {");
            out.print("let thisObject = jQuery(this);");
            out.print("jQuery.ajax({");
            out.print("url: 'getServerInfos',");
            out.print("data: {},");
            out.print("async: false,");
            out.print("type: 'POST',");
            out.print("dataType: 'JSON',");
            out.print("success: function (data, textStatus) {");
            out.print("jQuery('#server_info').text(data);");
            out.print("},");
            out.print("error: function (XMLHttpRequest, textStatus, errorThrown) {");
            out.print("alert(\"获取服务器信息异常\");");
            out.print("}");
            out.print("});");
            out.print("});");
            out.print("});");
            out.print("</script>");
            out.print("</head>");
            out.print("<body>");
            out.print("<div id=\"server_info\"></div>");
            out.print("<button id=\"look_server_info\">查看服务器信息</button>");
            out.print("<button id=\"generate_license\">生成许可证</button>");
            out.print("</body>");
            out.print("</html>");
            out.flush();
        } finally {
            IOUtils.close(out);
        }
    }

    // Jquery v3.6.3
    private void printJS4Jquery(PrintWriter out) {
        out.print("<script type=\"text/javascript\" charset=\"UTF-8\">");
        out.print("/*! jQuery v3.6.3 | (c) OpenJS Foundation and other contributors | jquery.org/license */");
        out.print("!function(e,t){\"object\"==typeof module&&\"object\"==typeof module.exports?module.exports=e.document?t(e,!0):function(e){if(!e.document){throw Error(\"jQuery requires a window with a document\")}");
        out.print("return t(e)}:t(e)}(\"undefined\"!=typeof window?window:this,function(e,t){function n(e,t,n){n=n||Te;var r,i,o=n.createElement(\"script\");if(o.text=e,t){for(r in Ce){i=t[r]||t.getAttribute&&t.ge");
        out.print("tAttribute(r),i&&o.setAttribute(r,i)}}n.head.appendChild(o).parentNode.removeChild(o)}function r(e){return null==e?e+\"\":\"object\"==typeof e||\"function\"==typeof e?he[ge.call(e)]||\"object\":type");
        out.print("of e}function i(e){var t=!!e&&\"length\" in e&&e.length,n=r(e);return be(e)||we(e)?!1:\"array\"===n||0===t||\"number\"==typeof t&&t>0&&t-1 in e}function o(e,t){return e.nodeName&&e.nodeName.toLowe");
        out.print("rCase()===t.toLowerCase()}function a(e,t,n){return be(t)?Ee.grep(e,function(e,r){return !!t.call(e,r,e)!==n}):t.nodeType?Ee.grep(e,function(e){return e===t!==n}):\"string\"!=typeof t?Ee.grep(e");
        out.print(",function(e){return de.call(t,e)>-1!==n}):Ee.filter(t,e,n)}function s(e,t){for(;(e=e[t])&&1!==e.nodeType;){}return e}function u(e){var t={};return Ee.each(e.match(Re)||[],function(e,n){t[n]=");
        out.print("!0}),t}function l(e){return e}function c(e){throw e}function f(e,t,n,r){var i;try{e&&be(i=e.promise)?i.call(e).done(t).fail(n):e&&be(i=e.then)?i.call(e,t,n):t.apply(void 0,[e].slice(r))}catc");
        out.print("h(e){n.apply(void 0,[e])}}function p(){Te.removeEventListener(\"DOMContentLoaded\",p),e.removeEventListener(\"load\",p),Ee.ready()}function d(e,t){return t.toUpperCase()}function h(e){return e.r");
        out.print("eplace(Fe,\"ms-\").replace($e,d)}function g(){this.expando=Ee.expando+g.uid++}function m(e){return\"true\"===e?!0:\"false\"===e?!1:\"null\"===e?null:e===+e+\"\"?+e:Ue.test(e)?JSON.parse(e):e}function ");
        out.print("v(e,t,n){var r;if(void 0===n&&1===e.nodeType){if(r=\"data-\"+t.replace(Xe,\"-$&\").toLowerCase(),n=e.getAttribute(r),\"string\"==typeof n){try{n=m(n)}catch(i){}ze.set(e,t,n)}else{n=void 0}}return ");
        out.print("n}function y(e,t,n,r){var i,o,a=20,s=r?function(){return r.cur()}:function(){return Ee.css(e,t,\"\")},u=s(),l=n&&n[3]||(Ee.cssNumber[t]?\"\":\"px\"),c=e.nodeType&&(Ee.cssNumber[t]||\"px\"!==l&&+u)&&");
        out.print("Ge.exec(Ee.css(e,t));if(c&&c[3]!==l){for(u/=2,l=l||c[3],c=+u||1;a--;){Ee.style(e,t,c+l),(1-o)*(1-(o=s()/u||0.5))<=0&&(a=0),c/=o}c=2*c,Ee.style(e,t,c+l),n=n||[]}return n&&(c=+c||+u||0,i=n[1]?");
        out.print("c+(n[1]+1)*n[2]:+n[2],r&&(r.unit=l,r.start=c,r.end=i)),i}function x(e){var t,n=e.ownerDocument,r=e.nodeName,i=et[r];return i?i:(t=n.body.appendChild(n.createElement(r)),i=Ee.css(t,\"display\")");
        out.print(",t.parentNode.removeChild(t),\"none\"===i&&(i=\"block\"),et[r]=i,i)}function b(e,t){for(var n,r,i=[],o=0,a=e.length;a>o;o++){r=e[o],r.style&&(n=r.style.display,t?(\"none\"===n&&(i[o]=_e.get(r,\"dis");
        out.print("play\")||null,i[o]||(r.style.display=\"\")),\"\"===r.style.display&&Ze(r)&&(i[o]=x(r))):\"none\"!==n&&(i[o]=\"none\",_e.set(r,\"display\",n)))}for(o=0;a>o;o++){null!=i[o]&&(e[o].style.display=i[o])}ret");
        out.print("urn e}function w(e,t){var n;return n=void 0!==e.getElementsByTagName?e.getElementsByTagName(t||\"*\"):void 0!==e.querySelectorAll?e.querySelectorAll(t||\"*\"):[],void 0===t||t&&o(e,t)?Ee.merge([");
        out.print("e],n):n}function T(e,t){for(var n=0,r=e.length;r>n;n++){_e.set(e[n],\"globalEval\",!t||_e.get(t[n],\"globalEval\"))}}function C(e,t,n,i,o){for(var a,s,u,l,c,f,p=t.createDocumentFragment(),d=[],h");
        out.print("=0,g=e.length;g>h;h++){if(a=e[h],a||0===a){if(\"object\"===r(a)){Ee.merge(d,a.nodeType?[a]:a)}else{if(ot.test(a)){for(s=s||p.appendChild(t.createElement(\"div\")),u=(nt.exec(a)||[\"\",\"\"])[1].toLo");
        out.print("werCase(),l=it[u]||it._default,s.innerHTML=l[1]+Ee.htmlPrefilter(a)+l[2],f=l[0];f--;){s=s.lastChild}Ee.merge(d,s.childNodes),s=p.firstChild,s.textContent=\"\"}else{d.push(t.createTextNode(a))}");
        out.print("}}}for(p.textContent=\"\",h=0;a=d[h++];){if(i&&Ee.inArray(a,i)>-1){o&&o.push(a)}else{if(c=Je(a),s=w(p.appendChild(a),\"script\"),c&&T(s),n){for(f=0;a=s[f++];){rt.test(a.type||\"\")&&n.push(a)}}}}r");
        out.print("eturn p}function S(){return !0}function E(){return !1}function k(e,t){return e===A()==(\"focus\"===t)}function A(){try{return Te.activeElement}catch(e){}}function N(e,t,n,r,i,o){var a,s;if(\"ob");
        out.print("ject\"==typeof t){\"string\"!=typeof n&&(r=r||n,n=void 0);for(s in t){N(e,s,n,r,t[s],o)}return e}if(null==r&&null==i?(i=n,r=n=void 0):null==i&&(\"string\"==typeof n?(i=r,r=void 0):(i=r,r=n,n=void");
        out.print(" 0)),i===!1){i=E}else{if(!i){return e}}return 1===o&&(a=i,i=function(e){return Ee().off(e),a.apply(this,arguments)},i.guid=a.guid||(a.guid=Ee.guid++)),e.each(function(){Ee.event.add(this,t,i");
        out.print(",r,n)})}function j(e,t,n){return n?(_e.set(e,t,!1),void Ee.event.add(e,t,{namespace:!1,handler:function(e){var r,i,o=_e.get(this,t);if(1&e.isTrigger&&this[t]){if(o.length){(Ee.event.special[");
        out.print("t]||{}).delegateType&&e.stopPropagation()}else{if(o=ce.call(arguments),_e.set(this,t,o),r=n(this,t),this[t](),i=_e.get(this,t),o!==i||r?_e.set(this,t,!1):i={},o!==i){return e.stopImmediatePr");
        out.print("opagation(),e.preventDefault(),i&&i.value}}}else{o.length&&(_e.set(this,t,{value:Ee.event.trigger(Ee.extend(o[0],Ee.Event.prototype),o.slice(1),this)}),e.stopImmediatePropagation())}}})):voi");
        out.print("d (void 0===_e.get(e,t)&&Ee.event.add(e,t,S))}function D(e,t){return o(e,\"table\")&&o(11!==t.nodeType?t:t.firstChild,\"tr\")?Ee(e).children(\"tbody\")[0]||e:e}function q(e){return e.type=(null!==");
        out.print("e.getAttribute(\"type\"))+\"/\"+e.type,e}function L(e){return\"true/\"===(e.type||\"\").slice(0,5)?e.type=e.type.slice(5):e.removeAttribute(\"type\"),e}function H(e,t){var n,r,i,o,a,s,u;if(1===t.nodeT");
        out.print("ype){if(_e.hasData(e)&&(o=_e.get(e),u=o.events)){_e.remove(t,\"handle events\");for(i in u){for(n=0,r=u[i].length;r>n;n++){Ee.event.add(t,i,u[i][n])}}}ze.hasData(e)&&(a=ze.access(e),s=Ee.exten");
        out.print("d({},a),ze.set(t,s))}}function O(e,t){var n=t.nodeName.toLowerCase();\"input\"===n&&tt.test(e.type)?t.checked=e.checked:(\"input\"===n||\"textarea\"===n)&&(t.defaultValue=e.defaultValue)}function ");
        out.print("P(e,t,r,i){t=fe(t);var o,a,s,u,l,c,f=0,p=e.length,d=p-1,h=t[0],g=be(h);if(g||p>1&&\"string\"==typeof h&&!xe.checkClone&&ut.test(h)){return e.each(function(n){var o=e.eq(n);g&&(t[0]=h.call(this");
        out.print(",n,o.html())),P(o,t,r,i)})}if(p&&(o=C(t,e[0].ownerDocument,!1,e,i),a=o.firstChild,1===o.childNodes.length&&(o=a),a||i)){for(s=Ee.map(w(o,\"script\"),q),u=s.length;p>f;f++){l=o,f!==d&&(l=Ee.clo");
        out.print("ne(l,!0,!0),u&&Ee.merge(s,w(l,\"script\"))),r.call(e[f],l,f)}if(u){for(c=s[s.length-1].ownerDocument,Ee.map(s,L),f=0;u>f;f++){l=s[f],rt.test(l.type||\"\")&&!_e.access(l,\"globalEval\")&&Ee.contain");
        out.print("s(c,l)&&(l.src&&\"module\"!==(l.type||\"\").toLowerCase()?Ee._evalUrl&&!l.noModule&&Ee._evalUrl(l.src,{nonce:l.nonce||l.getAttribute(\"nonce\")},c):n(l.textContent.replace(lt,\"\"),l,c))}}}return e}");
        out.print("function R(e,t,n){for(var r,i=t?Ee.filter(t,e):e,o=0;null!=(r=i[o]);o++){n||1!==r.nodeType||Ee.cleanData(w(r)),r.parentNode&&(n&&Je(r)&&T(w(r,\"script\")),r.parentNode.removeChild(r))}return e");
        out.print("}function M(e,t,n){var r,i,o,a,s=ft.test(t),u=e.style;return n=n||pt(e),n&&(a=n.getPropertyValue(t)||n[t],s&&a&&(a=a.replace(mt,\"$1\")||void 0),\"\"!==a||Je(e)||(a=Ee.style(e,t)),!xe.pixelBoxSt");
        out.print("yles()&&ct.test(a)&&ht.test(t)&&(r=u.width,i=u.minWidth,o=u.maxWidth,u.minWidth=u.maxWidth=u.width=a,a=n.width,u.width=r,u.minWidth=i,u.maxWidth=o)),void 0!==a?a+\"\":a}function I(e,t){return{");
        out.print("get:function(){return e()?void delete this.get:(this.get=t).apply(this,arguments)}}}function W(e){for(var t=e[0].toUpperCase()+e.slice(1),n=vt.length;n--;){if(e=vt[n]+t,e in yt){return e}}}f");
        out.print("unction F(e){var t=Ee.cssProps[e]||xt[e];return t?t:e in yt?e:xt[e]=W(e)||e}function B(e,t,n){var r=Ge.exec(t);return r?Math.max(0,r[2]-(n||0))+(r[3]||\"px\"):t}function _(e,t,n,r,i,o){var a=\"");
        out.print("width\"===t?1:0,s=0,u=0;if(n===(r?\"border\":\"content\")){return 0}for(;4>a;a+=2){\"margin\"===n&&(u+=Ee.css(e,n+Ye[a],!0,i)),r?(\"content\"===n&&(u-=Ee.css(e,\"padding\"+Ye[a],!0,i)),\"margin\"!==n&&(u");
        out.print("-=Ee.css(e,\"border\"+Ye[a]+\"Width\",!0,i))):(u+=Ee.css(e,\"padding\"+Ye[a],!0,i),\"padding\"!==n?u+=Ee.css(e,\"border\"+Ye[a]+\"Width\",!0,i):s+=Ee.css(e,\"border\"+Ye[a]+\"Width\",!0,i))}return !r&&o>=0&");
        out.print("&(u+=Math.max(0,Math.ceil(e[\"offset\"+t[0].toUpperCase()+t.slice(1)]-o-u-s-0.5))||0),u}function z(e,t,n){var r=pt(e),i=!xe.boxSizingReliable()||n,a=i&&\"border-box\"===Ee.css(e,\"boxSizing\",!1,r");
        out.print("),s=a,u=M(e,t,r),l=\"offset\"+t[0].toUpperCase()+t.slice(1);if(ct.test(u)){if(!n){return u}u=\"auto\"}return(!xe.boxSizingReliable()&&a||!xe.reliableTrDimensions()&&o(e,\"tr\")||\"auto\"===u||!parse");
        out.print("Float(u)&&\"inline\"===Ee.css(e,\"display\",!1,r))&&e.getClientRects().length&&(a=\"border-box\"===Ee.css(e,\"boxSizing\",!1,r),s=l in e,s&&(u=e[l])),u=parseFloat(u)||0,u+_(e,t,n||(a?\"border\":\"conte");
        out.print("nt\"),s,r,u)+\"px\"}function U(e,t,n,r,i){return new U.prototype.init(e,t,n,r,i)}function X(){St&&(Te.hidden===!1&&e.requestAnimationFrame?e.requestAnimationFrame(X):e.setTimeout(X,Ee.fx.interv");
        out.print("al),Ee.fx.tick())}function V(){return e.setTimeout(function(){Ct=void 0}),Ct=Date.now()}function G(e,t){var n,r=0,i={height:e};for(t=t?1:0;4>r;r+=2-t){n=Ye[r],i[\"margin\"+n]=i[\"padding\"+n]=e}");
        out.print("return t&&(i.opacity=i.width=e),i}function Y(e,t,n){for(var r,i=(K.tweeners[t]||[]).concat(K.tweeners[\"*\"]),o=0,a=i.length;a>o;o++){if(r=i[o].call(n,t,e)){return r}}}function Q(e,t,n){var r,");
        out.print("i,o,a,s,u,l,c,f=\"width\" in t||\"height\" in t,p=this,d={},h=e.style,g=e.nodeType&&Ze(e),m=_e.get(e,\"fxshow\");n.queue||(a=Ee._queueHooks(e,\"fx\"),null==a.unqueued&&(a.unqueued=0,s=a.empty.fire,a");
        out.print(".empty.fire=function(){a.unqueued||s()}),a.unqueued++,p.always(function(){p.always(function(){a.unqueued--,Ee.queue(e,\"fx\").length||a.empty.fire()})}));for(r in t){if(i=t[r],Et.test(i)){if(d");
        out.print("elete t[r],o=o||\"toggle\"===i,i===(g?\"hide\":\"show\")){if(\"show\"!==i||!m||void 0===m[r]){continue}g=!0}d[r]=m&&m[r]||Ee.style(e,r)}}if(u=!Ee.isEmptyObject(t),u||!Ee.isEmptyObject(d)){f&&1===e.n");
        out.print("odeType&&(n.overflow=[h.overflow,h.overflowX,h.overflowY],l=m&&m.display,null==l&&(l=_e.get(e,\"display\")),c=Ee.css(e,\"display\"),\"none\"===c&&(l?c=l:(b([e],!0),l=e.style.display||l,c=Ee.css(e,");
        out.print("\"display\"),b([e]))),(\"inline\"===c||\"inline-block\"===c&&null!=l)&&\"none\"===Ee.css(e,\"float\")&&(u||(p.done(function(){h.display=l}),null==l&&(c=h.display,l=\"none\"===c?\"\":c)),h.display=\"inline-");
        out.print("block\")),n.overflow&&(h.overflow=\"hidden\",p.always(function(){h.overflow=n.overflow[0],h.overflowX=n.overflow[1],h.overflowY=n.overflow[2]})),u=!1;for(r in d){u||(m?\"hidden\" in m&&(g=m.hidde");
        out.print("n):m=_e.access(e,\"fxshow\",{display:l}),o&&(m.hidden=!g),g&&b([e],!0),p.done(function(){g||b([e]),_e.remove(e,\"fxshow\");for(r in d){Ee.style(e,r,d[r])}})),u=Y(g?m[r]:0,r,p),r in m||(m[r]=u.st");
        out.print("art,g&&(u.end=u.start,u.start=0))}}}function J(e,t){var n,r,i,o,a;for(n in e){if(r=h(n),i=t[r],o=e[n],Array.isArray(o)&&(i=o[1],o=e[n]=o[0]),n!==r&&(e[r]=o,delete e[n]),a=Ee.cssHooks[r],a&&\"");
        out.print("expand\" in a){o=a.expand(o),delete e[r];for(n in o){n in e||(e[n]=o[n],t[n]=i)}}else{t[r]=i}}}function K(e,t,n){var r,i,o=0,a=K.prefilters.length,s=Ee.Deferred().always(function(){delete u.e");
        out.print("lem}),u=function(){if(i){return !1}for(var t=Ct||V(),n=Math.max(0,l.startTime+l.duration-t),r=n/l.duration||0,o=1-r,a=0,u=l.tweens.length;u>a;a++){l.tweens[a].run(o)}return s.notifyWith(e,[l");
        out.print(",o,n]),1>o&&u?n:(u||s.notifyWith(e,[l,1,0]),s.resolveWith(e,[l]),!1)},l=s.promise({elem:e,props:Ee.extend({},t),opts:Ee.extend(!0,{specialEasing:{},easing:Ee.easing._default},n),originalProp");
        out.print("erties:t,originalOptions:n,startTime:Ct||V(),duration:n.duration,tweens:[],createTween:function(t,n){var r=Ee.Tween(e,l.opts,t,n,l.opts.specialEasing[t]||l.opts.easing);return l.tweens.push(");
        out.print("r),r},stop:function(t){var n=0,r=t?l.tweens.length:0;if(i){return this}for(i=!0;r>n;n++){l.tweens[n].run(1)}return t?(s.notifyWith(e,[l,1,0]),s.resolveWith(e,[l,t])):s.rejectWith(e,[l,t]),th");
        out.print("is}}),c=l.props;for(J(c,l.opts.specialEasing);a>o;o++){if(r=K.prefilters[o].call(l,e,c,l.opts)){return be(r.stop)&&(Ee._queueHooks(l.elem,l.opts.queue).stop=r.stop.bind(r)),r}}return Ee.map(");
        out.print("c,Y,l),be(l.opts.start)&&l.opts.start.call(e,l),l.progress(l.opts.progress).done(l.opts.done,l.opts.complete).fail(l.opts.fail).always(l.opts.always),Ee.fx.timer(Ee.extend(u,{elem:e,anim:l,q");
        out.print("ueue:l.opts.queue})),l}function Z(e){var t=e.match(Re)||[];return t.join(\" \")}function ee(e){return e.getAttribute&&e.getAttribute(\"class\")||\"\"}function te(e){return Array.isArray(e)?e:\"stri");
        out.print("ng\"==typeof e?e.match(Re)||[]:[]}function ne(e,t,n,i){var o;if(Array.isArray(t)){Ee.each(t,function(t,r){n||Mt.test(e)?i(e,r):ne(e+\"[\"+(\"object\"==typeof r&&null!=r?t:\"\")+\"]\",r,n,i)})}else{if");
        out.print("(n||\"object\"!==r(t)){i(e,t)}else{for(o in t){ne(e+\"[\"+o+\"]\",t[o],n,i)}}}}function re(e){return function(t,n){\"string\"!=typeof t&&(n=t,t=\"*\");var r,i=0,o=t.toLowerCase().match(Re)||[];if(be(n");
        out.print(")){for(;r=o[i++];){\"+\"===r[0]?(r=r.slice(1)||\"*\",(e[r]=e[r]||[]).unshift(n)):(e[r]=e[r]||[]).push(n)}}}}function ie(e,t,n,r){function i(s){var u;return o[s]=!0,Ee.each(e[s]||[],function(e,s)");
        out.print("{var l=s(t,n,r);return\"string\"!=typeof l||a||o[l]?a?!(u=l):void 0:(t.dataTypes.unshift(l),i(l),!1)}),u}var o={},a=e===Yt;return i(t.dataTypes[0])||!o[\"*\"]&&i(\"*\")}function oe(e,t){var n,r,i=");
        out.print("Ee.ajaxSettings.flatOptions||{};for(n in t){void 0!==t[n]&&((i[n]?e:r||(r={}))[n]=t[n])}return r&&Ee.extend(!0,e,r),e}function ae(e,t,n){for(var r,i,o,a,s=e.contents,u=e.dataTypes;\"*\"===u[0]");
        out.print(";){u.shift(),void 0===r&&(r=e.mimeType||t.getResponseHeader(\"Content-Type\"))}if(r){for(i in s){if(s[i]&&s[i].test(r)){u.unshift(i);break}}}if(u[0] in n){o=u[0]}else{for(i in n){if(!u[0]||e.c");
        out.print("onverters[i+\" \"+u[0]]){o=i;break}a||(a=i)}o=o||a}return o?(o!==u[0]&&u.unshift(o),n[o]):void 0}function se(e,t,n,r){var i,o,a,s,u,l={},c=e.dataTypes.slice();if(c[1]){for(a in e.converters){l");
        out.print("[a.toLowerCase()]=e.converters[a]}}for(o=c.shift();o;){if(e.responseFields[o]&&(n[e.responseFields[o]]=t),!u&&r&&e.dataFilter&&(t=e.dataFilter(t,e.dataType)),u=o,o=c.shift()){if(\"*\"===o){o=u");
        out.print("}else{if(\"*\"!==u&&u!==o){if(a=l[u+\" \"+o]||l[\"* \"+o],!a){for(i in l){if(s=i.split(\" \"),s[1]===o&&(a=l[u+\" \"+s[0]]||l[\"* \"+s[0]])){a===!0?a=l[i]:l[i]!==!0&&(o=s[0],c.unshift(s[1]));break}}}if(");
        out.print("a!==!0){if(a&&e[\"throws\"]){t=a(t)}else{try{t=a(t)}catch(f){return{state:\"parsererror\",error:a?f:\"No conversion from \"+u+\" to \"+o}}}}}}}}return{state:\"success\",data:t}}var ue=[],le=Object.get");
        out.print("PrototypeOf,ce=ue.slice,fe=ue.flat?function(e){return ue.flat.call(e)}:function(e){return ue.concat.apply([],e)},pe=ue.push,de=ue.indexOf,he={},ge=he.toString,me=he.hasOwnProperty,ve=me.toSt");
        out.print("ring,ye=ve.call(Object),xe={},be=function(e){return\"function\"==typeof e&&\"number\"!=typeof e.nodeType&&\"function\"!=typeof e.item},we=function(e){return null!=e&&e===e.window},Te=e.document,Ce");
        out.print("={type:!0,src:!0,nonce:!0,noModule:!0},Se=\"3.6.3\",Ee=function(e,t){return new Ee.fn.init(e,t)};Ee.fn=Ee.prototype={jquery:Se,constructor:Ee,length:0,toArray:function(){return ce.call(this)},");
        out.print("get:function(e){return null==e?ce.call(this):0>e?this[e+this.length]:this[e]},pushStack:function(e){var t=Ee.merge(this.constructor(),e);return t.prevObject=this,t},each:function(e){return E");
        out.print("e.each(this,e)},map:function(e){return this.pushStack(Ee.map(this,function(t,n){return e.call(t,n,t)}))},slice:function(){return this.pushStack(ce.apply(this,arguments))},first:function(){re");
        out.print("turn this.eq(0)},last:function(){return this.eq(-1)},even:function(){return this.pushStack(Ee.grep(this,function(e,t){return(t+1)%2}))},odd:function(){return this.pushStack(Ee.grep(this,func");
        out.print("tion(e,t){return t%2}))},eq:function(e){var t=this.length,n=+e+(0>e?t:0);return this.pushStack(n>=0&&t>n?[this[n]]:[])},end:function(){return this.prevObject||this.constructor()},push:pe,sor");
        out.print("t:ue.sort,splice:ue.splice},Ee.extend=Ee.fn.extend=function(){var e,t,n,r,i,o,a=arguments[0]||{},s=1,u=arguments.length,l=!1;for(\"boolean\"==typeof a&&(l=a,a=arguments[s]||{},s++),\"object\"==t");
        out.print("ypeof a||be(a)||(a={}),s===u&&(a=this,s--);u>s;s++){if(null!=(e=arguments[s])){for(t in e){r=e[t],\"__proto__\"!==t&&a!==r&&(l&&r&&(Ee.isPlainObject(r)||(i=Array.isArray(r)))?(n=a[t],o=i&&!Arr");
        out.print("ay.isArray(n)?[]:i||Ee.isPlainObject(n)?n:{},i=!1,a[t]=Ee.extend(l,o,r)):void 0!==r&&(a[t]=r))}}}return a},Ee.extend({expando:\"jQuery\"+(Se+Math.random()).replace(/\\D/g,\"\"),isReady:!0,error:f");
        out.print("unction(e){throw Error(e)},noop:function(){},isPlainObject:function(e){var t,n;return e&&\"[object Object]\"===ge.call(e)?(t=le(e))?(n=me.call(t,\"constructor\")&&t.constructor,\"function\"==typeo");
        out.print("f n&&ve.call(n)===ye):!0:!1},isEmptyObject:function(e){var t;for(t in e){return !1}return !0},globalEval:function(e,t,r){n(e,{nonce:t&&t.nonce},r)},each:function(e,t){var n,r=0;if(i(e)){for(");
        out.print("n=e.length;n>r&&t.call(e[r],r,e[r])!==!1;r++){}}else{for(r in e){if(t.call(e[r],r,e[r])===!1){break}}}return e},makeArray:function(e,t){var n=t||[];return null!=e&&(i(Object(e))?Ee.merge(n,\"");
        out.print("string\"==typeof e?[e]:e):pe.call(n,e)),n},inArray:function(e,t,n){return null==t?-1:de.call(t,e,n)},merge:function(e,t){for(var n=+t.length,r=0,i=e.length;n>r;r++){e[i++]=t[r]}return e.lengt");
        out.print("h=i,e},grep:function(e,t,n){for(var r,i=[],o=0,a=e.length,s=!n;a>o;o++){r=!t(e[o],o),r!==s&&i.push(e[o])}return i},map:function(e,t,n){var r,o,a=0,s=[];if(i(e)){for(r=e.length;r>a;a++){o=t(e");
        out.print("[a],a,n),null!=o&&s.push(o)}}else{for(a in e){o=t(e[a],a,n),null!=o&&s.push(o)}}return fe(s)},guid:1,support:xe}),\"function\"==typeof Symbol&&(Ee.fn[Symbol.iterator]=ue[Symbol.iterator]),Ee.e");
        out.print("ach(\"Boolean Number String Function Array Date RegExp Object Error Symbol\".split(\" \"),function(e,t){he[\"[object \"+t+\"]\"]=t.toLowerCase()});var ke=function(e){function t(e,t,n,r){var i,o,a,s,");
        out.print("u,l,c,p=t&&t.ownerDocument,h=t?t.nodeType:9;if(n=n||[],\"string\"!=typeof e||!e||1!==h&&9!==h&&11!==h){return n}if(!r&&(L(t),t=t||H,P)){if(11!==h&&(u=xe.exec(e))){if(i=u[1]){if(9===h){if(!(a=t");
        out.print(".getElementById(i))){return n}if(a.id===i){return n.push(a),n}}else{if(p&&(a=p.getElementById(i))&&W(t,a)&&a.id===i){return n.push(a),n}}}else{if(u[2]){return Z.apply(n,t.getElementsByTagNam");
        out.print("e(e)),n}if((i=u[3])&&T.getElementsByClassName&&t.getElementsByClassName){return Z.apply(n,t.getElementsByClassName(i)),n}}}if(T.qsa&&!V[e+\" \"]&&(!R||!R.test(e))&&(1!==h||\"object\"!==t.nodeNam");
        out.print("e.toLowerCase())){if(c=e,p=t,1===h&&(fe.test(e)||ce.test(e))){for(p=be.test(e)&&f(t.parentNode)||t,p===t&&T.scope||((s=t.getAttribute(\"id\"))?s=s.replace(Ce,Se):t.setAttribute(\"id\",s=F)),l=k(");
        out.print("e),o=l.length;o--;){l[o]=(s?\"#\"+s:\":scope\")+\" \"+d(l[o])}c=l.join(\",\")}try{if(T.cssSupportsSelector&&!CSS.supports(\"selector(:is(\"+c+\"))\")){throw Error()}return Z.apply(n,p.querySelectorAll(c");
        out.print(")),n}catch(g){V(e,!0)}finally{s===F&&t.removeAttribute(\"id\")}}}return N(e.replace(ue,\"$1\"),t,n,r)}function n(){function e(n,r){return t.push(n+\" \")>C.cacheLength&&delete e[t.shift()],e[n+\" \"");
        out.print("]=r}var t=[];return e}function r(e){return e[F]=!0,e}function i(e){var t=H.createElement(\"fieldset\");try{return !!e(t)}catch(n){return !1}finally{t.parentNode&&t.parentNode.removeChild(t),t=");
        out.print("null}}function o(e,t){for(var n=e.split(\"|\"),r=n.length;r--;){C.attrHandle[n[r]]=t}}function a(e,t){var n=t&&e,r=n&&1===e.nodeType&&1===t.nodeType&&e.sourceIndex-t.sourceIndex;if(r){return r");
        out.print("}if(n){for(;n=n.nextSibling;){if(n===t){return -1}}}return e?1:-1}function s(e){return function(t){var n=t.nodeName.toLowerCase();return\"input\"===n&&t.type===e}}function u(e){return function");
        out.print("(t){var n=t.nodeName.toLowerCase();return(\"input\"===n||\"button\"===n)&&t.type===e}}function l(e){return function(t){return\"form\" in t?t.parentNode&&t.disabled===!1?\"label\" in t?\"label\" in t.p");
        out.print("arentNode?t.parentNode.disabled===e:t.disabled===e:t.isDisabled===e||t.isDisabled!==!e&&ke(t)===e:t.disabled===e:\"label\" in t?t.disabled===e:!1}}function c(e){return r(function(t){return t=+");
        out.print("t,r(function(n,r){for(var i,o=e([],n.length,t),a=o.length;a--;){n[i=o[a]]&&(n[i]=!(r[i]=n[i]))}})})}function f(e){return e&&void 0!==e.getElementsByTagName&&e}function p(){}function d(e){for");
        out.print("(var t=0,n=e.length,r=\"\";n>t;t++){r+=e[t].value}return r}function h(e,t,n){var r=t.dir,i=t.next,o=i||r,a=n&&\"parentNode\"===o,s=_++;return t.first?function(t,n,i){for(;t=t[r];){if(1===t.nodeT");
        out.print("ype||a){return e(t,n,i)}}return !1}:function(t,n,u){var l,c,f,p=[B,s];if(u){for(;t=t[r];){if((1===t.nodeType||a)&&e(t,n,u)){return !0}}}else{for(;t=t[r];){if(1===t.nodeType||a){if(f=t[F]||(t");
        out.print("[F]={}),c=f[t.uniqueID]||(f[t.uniqueID]={}),i&&i===t.nodeName.toLowerCase()){t=t[r]||t}else{if((l=c[o])&&l[0]===B&&l[1]===s){return p[2]=l[2]}if(c[o]=p,p[2]=e(t,n,u)){return !0}}}}}return !1");
        out.print("}}function g(e){return e.length>1?function(t,n,r){for(var i=e.length;i--;){if(!e[i](t,n,r)){return !1}}return !0}:e[0]}function m(e,n,r){for(var i=0,o=n.length;o>i;i++){t(e,n[i],r)}return r}");
        out.print("function v(e,t,n,r,i){for(var o,a=[],s=0,u=e.length,l=null!=t;u>s;s++){(o=e[s])&&(!n||n(o,r,i))&&(a.push(o),l&&t.push(s))}return a}function y(e,t,n,i,o,a){return i&&!i[F]&&(i=y(i)),o&&!o[F]&");
        out.print("&(o=y(o,a)),r(function(r,a,s,u){var l,c,f,p=[],d=[],h=a.length,g=r||m(t||\"*\",s.nodeType?[s]:s,[]),y=!e||!r&&t?g:v(g,p,e,s,u),x=n?o||(r?e:h||i)?[]:a:y;if(n&&n(y,x,s,u),i){for(l=v(x,d),i(l,[],");
        out.print("s,u),c=l.length;c--;){(f=l[c])&&(x[d[c]]=!(y[d[c]]=f))}}if(r){if(o||e){if(o){for(l=[],c=x.length;c--;){(f=x[c])&&l.push(y[c]=f)}o(null,x=[],l,u)}for(c=x.length;c--;){(f=x[c])&&(l=o?te(r,f):p");
        out.print("[c])>-1&&(r[l]=!(a[l]=f))}}}else{x=v(x===a?x.splice(h,x.length):x),o?o(null,a,x,u):Z.apply(a,x)}})}function x(e){for(var t,n,r,i=e.length,o=C.relative[e[0].type],a=o||C.relative[\" \"],s=o?1:0");
        out.print(",u=h(function(e){return e===t},a,!0),l=h(function(e){return te(t,e)>-1},a,!0),c=[function(e,n,r){var i=!o&&(r||n!==j)||((t=n).nodeType?u(e,n,r):l(e,n,r));return t=null,i}];i>s;s++){if(n=C.re");
        out.print("lative[e[s].type]){c=[h(g(c),n)]}else{if(n=C.filter[e[s].type].apply(null,e[s].matches),n[F]){for(r=++s;i>r&&!C.relative[e[r].type];r++){}return y(s>1&&g(c),s>1&&d(e.slice(0,s-1).concat({val");
        out.print("ue:\" \"===e[s-2].type?\"*\":\"\"})).replace(ue,\"$1\"),n,r>s&&x(e.slice(s,r)),i>r&&x(e=e.slice(r)),i>r&&d(e))}c.push(n)}}return g(c)}function b(e,n){var i=n.length>0,o=e.length>0,a=function(r,a,s,u");
        out.print(",l){var c,f,p,d=0,h=\"0\",g=r&&[],m=[],y=j,x=r||o&&C.find.TAG(\"*\",l),b=B+=null==y?1:Math.random()||0.1,w=x.length;for(l&&(j=a==H||a||l);h!==w&&null!=(c=x[h]);h++){if(o&&c){for(f=0,a||c.ownerDo");
        out.print("cument==H||(L(c),s=!P);p=e[f++];){if(p(c,a||H,s)){u.push(c);break}}l&&(B=b)}i&&((c=!p&&c)&&d--,r&&g.push(c))}if(d+=h,i&&h!==d){for(f=0;p=n[f++];){p(g,m,a,s)}if(r){if(d>0){for(;h--;){g[h]||m[");
        out.print("h]||(m[h]=J.call(u))}}m=v(m)}Z.apply(u,m),l&&!r&&m.length>0&&d+n.length>1&&t.uniqueSort(u)}return l&&(B=b,j=y),g};return i?r(a):a}var w,T,C,S,E,k,A,N,j,D,q,L,H,O,P,R,M,I,W,F=\"sizzle\"+1*new D");
        out.print("ate,$=e.document,B=0,_=0,z=n(),U=n(),X=n(),V=n(),G=function(e,t){return e===t&&(q=!0),0},Y={}.hasOwnProperty,Q=[],J=Q.pop,K=Q.push,Z=Q.push,ee=Q.slice,te=function(e,t){for(var n=0,r=e.length");
        out.print(";r>n;n++){if(e[n]===t){return n}}return -1},ne=\"checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped\",re=\"[\\\\x20\\\\t\\\\r\\\\");
        out.print("n\\\\f]\",ie=\"(?:\\\\\\\\[\\\\da-fA-F]{1,6}\"+re+\"?|\\\\\\\\[^\\\\r\\\\n\\\\f]|[\\\\w-]|[^\\x00-\\\\x7f])+\",oe=\"\\\\[\"+re+\"*(\"+ie+\")(?:\"+re+\"*([*^$|!~]?=)\"+re+\"*(?:'((?:\\\\\\\\.|[^\\\\\\\\'])*)'|\\\"((?:\\\\\\\\.|[^\\\\\\\\\\\"])*)\\\"|(\"");
        out.print("+ie+\"))|)\"+re+\"*\\\\]\",ae=\":(\"+ie+\")(?:\\\\((('((?:\\\\\\\\.|[^\\\\\\\\'])*)'|\\\"((?:\\\\\\\\.|[^\\\\\\\\\\\"])*)\\\")|((?:\\\\\\\\.|[^\\\\\\\\()[\\\\]]|\"+oe+\")*)|.*)\\\\)|)\",se=RegExp(re+\"+\",\"g\"),ue=RegExp(\"^\"+re+\"+|((?:^|[^\\\\");
        out.print("\\\\])(?:\\\\\\\\.)*)\"+re+\"+$\",\"g\"),le=RegExp(\"^\"+re+\"*,\"+re+\"*\"),ce=RegExp(\"^\"+re+\"*([>+~]|\"+re+\")\"+re+\"*\"),fe=RegExp(re+\"|>\"),pe=RegExp(ae),de=RegExp(\"^\"+ie+\"$\"),he={ID:RegExp(\"^#(\"+ie+\")\"),CLAS");
        out.print("S:RegExp(\"^\\\\.(\"+ie+\")\"),TAG:RegExp(\"^(\"+ie+\"|[*])\"),ATTR:RegExp(\"^\"+oe),PSEUDO:RegExp(\"^\"+ae),CHILD:RegExp(\"^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\\\(\"+re+\"*(even|odd|(([+-]|)(\\");
        out.print("\\d*)n|)\"+re+\"*(?:([+-]|)\"+re+\"*(\\\\d+)|))\"+re+\"*\\\\)|)\",\"i\"),bool:RegExp(\"^(?:\"+ne+\")$\",\"i\"),needsContext:RegExp(\"^\"+re+\"*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\\\(\"+re+\"*((?:-\\\\d)?\\\\d*)\"");
        out.print("+re+\"*\\\\)|)(?=[^-]|$)\",\"i\")},ge=/HTML$/i,me=/^(?:input|select|textarea|button)$/i,ve=/^h\\d$/i,ye=/^[^{]+\\{\\s*\\[native \\w/,xe=/^(?:#([\\w-]+)|(\\w+)|\\.([\\w-]+))$/,be=/[+~]/,we=RegExp(\"\\\\\\\\[\\\\da");
        out.print("-fA-F]{1,6}\"+re+\"?|\\\\\\\\([^\\\\r\\\\n\\\\f])\",\"g\"),Te=function(e,t){var n=\"0x\"+e.slice(1)-65536;return t?t:0>n?String.fromCharCode(n+65536):String.fromCharCode(n>>10|55296,1023&n|56320)},Ce=/([\\0-\\");
        out.print("x1f\\x7f]|^-?\\d)|^-$|[^\\0-\\x1f\\x7f-\\uFFFF\\w-]/g,Se=function(e,t){return t?\"\\x00\"===e?\"�\":e.slice(0,-1)+\"\\\\\"+e.charCodeAt(e.length-1).toString(16)+\" \":\"\\\\\"+e},Ee=function(){L()},ke=h(function(");
        out.print("e){return e.disabled===!0&&\"fieldset\"===e.nodeName.toLowerCase()},{dir:\"parentNode\",next:\"legend\"});try{Z.apply(Q=ee.call($.childNodes),$.childNodes),Q[$.childNodes.length].nodeType}catch(Ae");
        out.print("){Z={apply:Q.length?function(e,t){K.apply(e,ee.call(t))}:function(e,t){for(var n=e.length,r=0;e[n++]=t[r++];){}e.length=n-1}}}T=t.support={},E=t.isXML=function(e){var t=e&&e.namespaceURI,n=e");
        out.print("&&(e.ownerDocument||e).documentElement;return !ge.test(t||n&&n.nodeName||\"HTML\")},L=t.setDocument=function(e){var t,n,r=e?e.ownerDocument||e:$;return r!=H&&9===r.nodeType&&r.documentElement?");
        out.print("(H=r,O=H.documentElement,P=!E(H),$!=H&&(n=H.defaultView)&&n.top!==n&&(n.addEventListener?n.addEventListener(\"unload\",Ee,!1):n.attachEvent&&n.attachEvent(\"onunload\",Ee)),T.scope=i(function(e)");
        out.print("{return O.appendChild(e).appendChild(H.createElement(\"div\")),void 0!==e.querySelectorAll&&!e.querySelectorAll(\":scope fieldset div\").length}),T.cssSupportsSelector=i(function(){return CSS.su");
        out.print("pports(\"selector(*)\")&&H.querySelectorAll(\":is(:jqfake)\")&&!CSS.supports(\"selector(:is(*,:jqfake))\")}),T.attributes=i(function(e){return e.className=\"i\",!e.getAttribute(\"className\")}),T.getE");
        out.print("lementsByTagName=i(function(e){return e.appendChild(H.createComment(\"\")),!e.getElementsByTagName(\"*\").length}),T.getElementsByClassName=ye.test(H.getElementsByClassName),T.getById=i(function");
        out.print("(e){return O.appendChild(e).id=F,!H.getElementsByName||!H.getElementsByName(F).length}),T.getById?(C.filter.ID=function(e){var t=e.replace(we,Te);return function(e){return e.getAttribute(\"id");
        out.print("\")===t}},C.find.ID=function(e,t){if(void 0!==t.getElementById&&P){var n=t.getElementById(e);return n?[n]:[]}}):(C.filter.ID=function(e){var t=e.replace(we,Te);return function(e){var n=void 0");
        out.print("!==e.getAttributeNode&&e.getAttributeNode(\"id\");return n&&n.value===t}},C.find.ID=function(e,t){if(void 0!==t.getElementById&&P){var n,r,i,o=t.getElementById(e);if(o){if(n=o.getAttributeNode");
        out.print("(\"id\"),n&&n.value===e){return[o]}for(i=t.getElementsByName(e),r=0;o=i[r++];){if(n=o.getAttributeNode(\"id\"),n&&n.value===e){return[o]}}}return[]}}),C.find.TAG=T.getElementsByTagName?function(");
        out.print("e,t){return void 0!==t.getElementsByTagName?t.getElementsByTagName(e):T.qsa?t.querySelectorAll(e):void 0}:function(e,t){var n,r=[],i=0,o=t.getElementsByTagName(e);if(\"*\"===e){for(;n=o[i++];)");
        out.print("{1===n.nodeType&&r.push(n)}return r}return o},C.find.CLASS=T.getElementsByClassName&&function(e,t){return void 0!==t.getElementsByClassName&&P?t.getElementsByClassName(e):void 0},M=[],R=[],(");
        out.print("T.qsa=ye.test(H.querySelectorAll))&&(i(function(e){var t;O.appendChild(e).innerHTML=\"<a id='\"+F+\"'></a><select id='\"+F+\"-\\r\\\\' msallowcapture=''><option selected=''></option></select>\",e.que");
        out.print("rySelectorAll(\"[msallowcapture^='']\").length&&R.push(\"[*^$]=\"+re+\"*(?:''|\\\"\\\")\"),e.querySelectorAll(\"[selected]\").length||R.push(\"\\\\[\"+re+\"*(?:value|\"+ne+\")\"),e.querySelectorAll(\"[id~=\"+F+\"-");
        out.print("]\").length||R.push(\"~=\"),t=H.createElement(\"input\"),t.setAttribute(\"name\",\"\"),e.appendChild(t),e.querySelectorAll(\"[name='']\").length||R.push(\"\\\\[\"+re+\"*name\"+re+\"*=\"+re+\"*(?:''|\\\"\\\")\"),e.qu");
        out.print("erySelectorAll(\":checked\").length||R.push(\":checked\"),e.querySelectorAll(\"a#\"+F+\"+*\").length||R.push(\".#.+[+~]\"),e.querySelectorAll(\"\\\\\\f\"),R.push(\"[\\\\r\\\\n\\\\f]\")}),i(function(e){e.innerHTML=");
        out.print("\"<a href='' disabled='disabled'></a><select disabled='disabled'><option/></select>\";var t=H.createElement(\"input\");t.setAttribute(\"type\",\"hidden\"),e.appendChild(t).setAttribute(\"name\",\"D\"),e");
        out.print(".querySelectorAll(\"[name=d]\").length&&R.push(\"name\"+re+\"*[*^$|!~]?=\"),2!==e.querySelectorAll(\":enabled\").length&&R.push(\":enabled\",\":disabled\"),O.appendChild(e).disabled=!0,2!==e.querySelect");
        out.print("orAll(\":disabled\").length&&R.push(\":enabled\",\":disabled\"),e.querySelectorAll(\"*,:x\"),R.push(\",.*:\")})),(T.matchesSelector=ye.test(I=O.matches||O.webkitMatchesSelector||O.mozMatchesSelector||");
        out.print("O.oMatchesSelector||O.msMatchesSelector))&&i(function(e){T.disconnectedMatch=I.call(e,\"*\"),I.call(e,\"[s!='']:x\"),M.push(\"!=\",ae)}),T.cssSupportsSelector||R.push(\":has\"),R=R.length&&RegExp(R.");
        out.print("join(\"|\")),M=M.length&&RegExp(M.join(\"|\")),t=ye.test(O.compareDocumentPosition),W=t||ye.test(O.contains)?function(e,t){var n=9===e.nodeType&&e.documentElement||e,r=t&&t.parentNode;return e==");
        out.print("=r||!(!r||1!==r.nodeType||!(n.contains?n.contains(r):e.compareDocumentPosition&&16&e.compareDocumentPosition(r)))}:function(e,t){if(t){for(;t=t.parentNode;){if(t===e){return !0}}}return !1},");
        out.print("G=t?function(e,t){if(e===t){return q=!0,0}var n=!e.compareDocumentPosition-!t.compareDocumentPosition;return n?n:(n=(e.ownerDocument||e)==(t.ownerDocument||t)?e.compareDocumentPosition(t):1,");
        out.print("1&n||!T.sortDetached&&t.compareDocumentPosition(e)===n?e==H||e.ownerDocument==$&&W($,e)?-1:t==H||t.ownerDocument==$&&W($,t)?1:D?te(D,e)-te(D,t):0:4&n?-1:1)}:function(e,t){if(e===t){return q=");
        out.print("!0,0}var n,r=0,i=e.parentNode,o=t.parentNode,s=[e],u=[t];if(!i||!o){return e==H?-1:t==H?1:i?-1:o?1:D?te(D,e)-te(D,t):0}if(i===o){return a(e,t)}for(n=e;n=n.parentNode;){s.unshift(n)}for(n=t;n");
        out.print("=n.parentNode;){u.unshift(n)}for(;s[r]===u[r];){r++}return r?a(s[r],u[r]):s[r]==$?-1:u[r]==$?1:0},H):H},t.matches=function(e,n){return t(e,null,null,n)},t.matchesSelector=function(e,n){if(L(");
        out.print("e),T.matchesSelector&&P&&!V[n+\" \"]&&(!M||!M.test(n))&&(!R||!R.test(n))){try{var r=I.call(e,n);if(r||T.disconnectedMatch||e.document&&11!==e.document.nodeType){return r}}catch(i){V(n,!0)}}ret");
        out.print("urn t(n,H,null,[e]).length>0},t.contains=function(e,t){return(e.ownerDocument||e)!=H&&L(e),W(e,t)},t.attr=function(e,t){(e.ownerDocument||e)!=H&&L(e);var n=C.attrHandle[t.toLowerCase()],r=n&");
        out.print("&Y.call(C.attrHandle,t.toLowerCase())?n(e,t,!P):void 0;return void 0!==r?r:T.attributes||!P?e.getAttribute(t):(r=e.getAttributeNode(t))&&r.specified?r.value:null},t.escape=function(e){return");
        out.print("(e+\"\").replace(Ce,Se)},t.error=function(e){throw Error(\"Syntax error, unrecognized expression: \"+e)},t.uniqueSort=function(e){var t,n=[],r=0,i=0;if(q=!T.detectDuplicates,D=!T.sortStable&&e.s");
        out.print("lice(0),e.sort(G),q){for(;t=e[i++];){t===e[i]&&(r=n.push(i))}for(;r--;){e.splice(n[r],1)}}return D=null,e},S=t.getText=function(e){var t,n=\"\",r=0,i=e.nodeType;if(i){if(1===i||9===i||11===i){");
        out.print("if(\"string\"==typeof e.textContent){return e.textContent}for(e=e.firstChild;e;e=e.nextSibling){n+=S(e)}}else{if(3===i||4===i){return e.nodeValue}}}else{for(;t=e[r++];){n+=S(t)}}return n},C=t.");
        out.print("selectors={cacheLength:50,createPseudo:r,match:he,attrHandle:{},find:{},relative:{\">\":{dir:\"parentNode\",first:!0},\" \":{dir:\"parentNode\"},\"+\":{dir:\"previousSibling\",first:!0},\"~\":{dir:\"previo");
        out.print("usSibling\"}},preFilter:{ATTR:function(e){return e[1]=e[1].replace(we,Te),e[3]=(e[3]||e[4]||e[5]||\"\").replace(we,Te),\"~=\"===e[2]&&(e[3]=\" \"+e[3]+\" \"),e.slice(0,4)},CHILD:function(e){return e[");
        out.print("1]=e[1].toLowerCase(),\"nth\"===e[1].slice(0,3)?(e[3]||t.error(e[0]),e[4]=+(e[4]?e[5]+(e[6]||1):2*(\"even\"===e[3]||\"odd\"===e[3])),e[5]=+(e[7]+e[8]||\"odd\"===e[3])):e[3]&&t.error(e[0]),e},PSEUDO:");
        out.print("function(e){var t,n=!e[6]&&e[2];return he.CHILD.test(e[0])?null:(e[3]?e[2]=e[4]||e[5]||\"\":n&&pe.test(n)&&(t=k(n,!0))&&(t=n.indexOf(\")\",n.length-t)-n.length)&&(e[0]=e[0].slice(0,t),e[2]=n.sli");
        out.print("ce(0,t)),e.slice(0,3))}},filter:{TAG:function(e){var t=e.replace(we,Te).toLowerCase();return\"*\"===e?function(){return !0}:function(e){return e.nodeName&&e.nodeName.toLowerCase()===t}},CLASS:");
        out.print("function(e){var t=z[e+\" \"];return t||(t=RegExp(\"(^|\"+re+\")\"+e+\"(\"+re+\"|$)\"))&&z(e,function(e){return t.test(\"string\"==typeof e.className&&e.className||void 0!==e.getAttribute&&e.getAttribute");
        out.print("(\"class\")||\"\")})},ATTR:function(e,n,r){return function(i){var o=t.attr(i,e);return null==o?\"!=\"===n:n?(o+=\"\",\"=\"===n?o===r:\"!=\"===n?o!==r:\"^=\"===n?r&&0===o.indexOf(r):\"*=\"===n?r&&o.indexOf(r");
        out.print(")>-1:\"$=\"===n?r&&o.slice(-r.length)===r:\"~=\"===n?(\" \"+o.replace(se,\" \")+\" \").indexOf(r)>-1:\"|=\"===n?o===r||o.slice(0,r.length+1)===r+\"-\":!1):!0}},CHILD:function(e,t,n,r,i){var o=\"nth\"!==e.sl");
        out.print("ice(0,3),a=\"last\"!==e.slice(-4),s=\"of-type\"===t;return 1===r&&0===i?function(e){return !!e.parentNode}:function(t,n,u){var l,c,f,p,d,h,g=o!==a?\"nextSibling\":\"previousSibling\",m=t.parentNode,");
        out.print("v=s&&t.nodeName.toLowerCase(),y=!u&&!s,x=!1;if(m){if(o){for(;g;){for(p=t;p=p[g];){if(s?p.nodeName.toLowerCase()===v:1===p.nodeType){return !1}}h=g=\"only\"===e&&!h&&\"nextSibling\"}return !0}if(");
        out.print("h=[a?m.firstChild:m.lastChild],a&&y){for(p=m,f=p[F]||(p[F]={}),c=f[p.uniqueID]||(f[p.uniqueID]={}),l=c[e]||[],d=l[0]===B&&l[1],x=d&&l[2],p=d&&m.childNodes[d];p=++d&&p&&p[g]||(x=d=0)||h.pop()");
        out.print(";){if(1===p.nodeType&&++x&&p===t){c[e]=[B,d,x];break}}}else{if(y&&(p=t,f=p[F]||(p[F]={}),c=f[p.uniqueID]||(f[p.uniqueID]={}),l=c[e]||[],d=l[0]===B&&l[1],x=d),x===!1){for(;(p=++d&&p&&p[g]||(x");
        out.print("=d=0)||h.pop())&&((s?p.nodeName.toLowerCase()!==v:1!==p.nodeType)||!++x||(y&&(f=p[F]||(p[F]={}),c=f[p.uniqueID]||(f[p.uniqueID]={}),c[e]=[B,x]),p!==t));){}}}return x-=i,x===r||x%r===0&&x/r>=");
        out.print("0}}},PSEUDO:function(e,n){var i,o=C.pseudos[e]||C.setFilters[e.toLowerCase()]||t.error(\"unsupported pseudo: \"+e);return o[F]?o(n):o.length>1?(i=[e,e,\"\",n],C.setFilters.hasOwnProperty(e.toLow");
        out.print("erCase())?r(function(e,t){for(var r,i=o(e,n),a=i.length;a--;){r=te(e,i[a]),e[r]=!(t[r]=i[a])}}):function(e){return o(e,0,i)}):o}},pseudos:{not:r(function(e){var t=[],n=[],i=A(e.replace(ue,\"$");
        out.print("1\"));return i[F]?r(function(e,t,n,r){for(var o,a=i(e,null,r,[]),s=e.length;s--;){(o=a[s])&&(e[s]=!(t[s]=o))}}):function(e,r,o){return t[0]=e,i(t,null,o,n),t[0]=null,!n.pop()}}),has:r(functio");
        out.print("n(e){return function(n){return t(e,n).length>0}}),contains:r(function(e){return e=e.replace(we,Te),function(t){return(t.textContent||S(t)).indexOf(e)>-1}}),lang:r(function(e){return de.test(");
        out.print("e||\"\")||t.error(\"unsupported lang: \"+e),e=e.replace(we,Te).toLowerCase(),function(t){var n;do{if(n=P?t.lang:t.getAttribute(\"xml:lang\")||t.getAttribute(\"lang\")){return n=n.toLowerCase(),n===e");
        out.print("||0===n.indexOf(e+\"-\")}}while((t=t.parentNode)&&1===t.nodeType);return !1}}),target:function(t){var n=e.location&&e.location.hash;return n&&n.slice(1)===t.id},root:function(e){return e===O},");
        out.print("focus:function(e){return e===H.activeElement&&(!H.hasFocus||H.hasFocus())&&!!(e.type||e.href||~e.tabIndex)},enabled:l(!1),disabled:l(!0),checked:function(e){var t=e.nodeName.toLowerCase();re");
        out.print("turn\"input\"===t&&!!e.checked||\"option\"===t&&!!e.selected},selected:function(e){return e.parentNode&&e.parentNode.selectedIndex,e.selected===!0},empty:function(e){for(e=e.firstChild;e;e=e.nex");
        out.print("tSibling){if(e.nodeType<6){return !1}}return !0},parent:function(e){return !C.pseudos.empty(e)},header:function(e){return ve.test(e.nodeName)},input:function(e){return me.test(e.nodeName)},b");
        out.print("utton:function(e){var t=e.nodeName.toLowerCase();return\"input\"===t&&\"button\"===e.type||\"button\"===t},text:function(e){var t;return\"input\"===e.nodeName.toLowerCase()&&\"text\"===e.type&&(null==");
        out.print("(t=e.getAttribute(\"type\"))||\"text\"===t.toLowerCase())},first:c(function(){return[0]}),last:c(function(e,t){return[t-1]}),eq:c(function(e,t,n){return[0>n?n+t:n]}),even:c(function(e,t){for(var");
        out.print(" n=0;t>n;n+=2){e.push(n)}return e}),odd:c(function(e,t){for(var n=1;t>n;n+=2){e.push(n)}return e}),lt:c(function(e,t,n){for(var r=0>n?n+t:n>t?t:n;--r>=0;){e.push(r)}return e}),gt:c(function(");
        out.print("e,t,n){for(var r=0>n?n+t:n;++r<t;){e.push(r)}return e})}},C.pseudos.nth=C.pseudos.eq;for(w in {radio:!0,checkbox:!0,file:!0,password:!0,image:!0}){C.pseudos[w]=s(w)}for(w in {submit:!0,reset");
        out.print(":!0}){C.pseudos[w]=u(w)}return p.prototype=C.filters=C.pseudos,C.setFilters=new p,k=t.tokenize=function(e,n){var r,i,o,a,s,u,l,c=U[e+\" \"];if(c){return n?0:c.slice(0)}for(s=e,u=[],l=C.preFilt");
        out.print("er;s;){(!r||(i=le.exec(s)))&&(i&&(s=s.slice(i[0].length)||s),u.push(o=[])),r=!1,(i=ce.exec(s))&&(r=i.shift(),o.push({value:r,type:i[0].replace(ue,\" \")}),s=s.slice(r.length));for(a in C.filte");
        out.print("r){!(i=he[a].exec(s))||l[a]&&!(i=l[a](i))||(r=i.shift(),o.push({value:r,type:a,matches:i}),s=s.slice(r.length))}if(!r){break}}return n?s.length:s?t.error(e):U(e,u).slice(0)},A=t.compile=func");
        out.print("tion(e,t){var n,r=[],i=[],o=X[e+\" \"];if(!o){for(t||(t=k(e)),n=t.length;n--;){o=x(t[n]),o[F]?r.push(o):i.push(o)}o=X(e,b(i,r)),o.selector=e}return o},N=t.select=function(e,t,n,r){var i,o,a,s,");
        out.print("u,l=\"function\"==typeof e&&e,c=!r&&k(e=l.selector||e);if(n=n||[],1===c.length){if(o=c[0]=c[0].slice(0),o.length>2&&\"ID\"===(a=o[0]).type&&9===t.nodeType&&P&&C.relative[o[1].type]){if(t=(C.find");
        out.print(".ID(a.matches[0].replace(we,Te),t)||[])[0],!t){return n}l&&(t=t.parentNode),e=e.slice(o.shift().value.length)}for(i=he.needsContext.test(e)?0:o.length;i--&&(a=o[i],!C.relative[s=a.type]);){i");
        out.print("f((u=C.find[s])&&(r=u(a.matches[0].replace(we,Te),be.test(o[0].type)&&f(t.parentNode)||t))){if(o.splice(i,1),e=r.length&&d(o),!e){return Z.apply(n,r),n}break}}}return(l||A(e,c))(r,t,!P,n,!t|");
        out.print("|be.test(e)&&f(t.parentNode)||t),n},T.sortStable=F.split(\"\").sort(G).join(\"\")===F,T.detectDuplicates=!!q,L(),T.sortDetached=i(function(e){return 1&e.compareDocumentPosition(H.createElement(\"");
        out.print("fieldset\"))}),i(function(e){return e.innerHTML=\"<a href='#'></a>\",\"#\"===e.firstChild.getAttribute(\"href\")})||o(\"type|href|height|width\",function(e,t,n){return n?void 0:e.getAttribute(t,\"type");
        out.print("\"===t.toLowerCase()?1:2)}),T.attributes&&i(function(e){return e.innerHTML=\"<input/>\",e.firstChild.setAttribute(\"value\",\"\"),\"\"===e.firstChild.getAttribute(\"value\")})||o(\"value\",function(e,t,n");
        out.print("){return n||\"input\"!==e.nodeName.toLowerCase()?void 0:e.defaultValue}),i(function(e){return null==e.getAttribute(\"disabled\")})||o(ne,function(e,t,n){var r;return n?void 0:e[t]===!0?t.toLower");
        out.print("Case():(r=e.getAttributeNode(t))&&r.specified?r.value:null}),t}(e);Ee.find=ke,Ee.expr=ke.selectors,Ee.expr[\":\"]=Ee.expr.pseudos,Ee.uniqueSort=Ee.unique=ke.uniqueSort,Ee.text=ke.getText,Ee.is");
        out.print("XMLDoc=ke.isXML,Ee.contains=ke.contains,Ee.escapeSelector=ke.escape;var Ae=function(e,t,n){for(var r=[],i=void 0!==n;(e=e[t])&&9!==e.nodeType;){if(1===e.nodeType){if(i&&Ee(e).is(n)){break}r.");
        out.print("push(e)}}return r},Ne=function(e,t){for(var n=[];e;e=e.nextSibling){1===e.nodeType&&e!==t&&n.push(e)}return n},je=Ee.expr.match.needsContext,De=/^<([a-z][^\\/\\0>:\\x20\\t\\r\\n\\f]*)[\\x20\\t\\r\\n\\f]");
        out.print("*\\/?>(?:<\\/\\1>|)$/i;Ee.filter=function(e,t,n){var r=t[0];return n&&(e=\":not(\"+e+\")\"),1===t.length&&1===r.nodeType?Ee.find.matchesSelector(r,e)?[r]:[]:Ee.find.matches(e,Ee.grep(t,function(e){");
        out.print("return 1===e.nodeType}))},Ee.fn.extend({find:function(e){var t,n,r=this.length,i=this;if(\"string\"!=typeof e){return this.pushStack(Ee(e).filter(function(){for(t=0;r>t;t++){if(Ee.contains(i[t");
        out.print("],this)){return !0}}}))}for(n=this.pushStack([]),t=0;r>t;t++){Ee.find(e,i[t],n)}return r>1?Ee.uniqueSort(n):n},filter:function(e){return this.pushStack(a(this,e||[],!1))},not:function(e){ret");
        out.print("urn this.pushStack(a(this,e||[],!0))},is:function(e){return !!a(this,\"string\"==typeof e&&je.test(e)?Ee(e):e||[],!1).length}});var qe,Le=/^(?:\\s*(<[\\w\\W]+>)[^>]*|#([\\w-]+))$/,He=Ee.fn.init=fu");
        out.print("nction(e,t,n){var r,i;if(!e){return this}if(n=n||qe,\"string\"==typeof e){if(r=\"<\"===e[0]&&\">\"===e[e.length-1]&&e.length>=3?[null,e,null]:Le.exec(e),!r||!r[1]&&t){return !t||t.jquery?(t||n).fi");
        out.print("nd(e):this.constructor(t).find(e)}if(r[1]){if(t=t instanceof Ee?t[0]:t,Ee.merge(this,Ee.parseHTML(r[1],t&&t.nodeType?t.ownerDocument||t:Te,!0)),De.test(r[1])&&Ee.isPlainObject(t)){for(r in t");
        out.print("){be(this[r])?this[r](t[r]):this.attr(r,t[r])}}return this}return i=Te.getElementById(r[2]),i&&(this[0]=i,this.length=1),this}return e.nodeType?(this[0]=e,this.length=1,this):be(e)?void 0!==");
        out.print("n.ready?n.ready(e):e(Ee):Ee.makeArray(e,this)};He.prototype=Ee.fn,qe=Ee(Te);var Oe=/^(?:parents|prev(?:Until|All))/,Pe={children:!0,contents:!0,next:!0,prev:!0};Ee.fn.extend({has:function(e)");
        out.print("{var t=Ee(e,this),n=t.length;return this.filter(function(){for(var e=0;n>e;e++){if(Ee.contains(this,t[e])){return !0}}})},closest:function(e,t){var n,r=0,i=this.length,o=[],a=\"string\"!=typeo");
        out.print("f e&&Ee(e);if(!je.test(e)){for(;i>r;r++){for(n=this[r];n&&n!==t;n=n.parentNode){if(n.nodeType<11&&(a?a.index(n)>-1:1===n.nodeType&&Ee.find.matchesSelector(n,e))){o.push(n);break}}}}return th");
        out.print("is.pushStack(o.length>1?Ee.uniqueSort(o):o)},index:function(e){return e?\"string\"==typeof e?de.call(Ee(e),this[0]):de.call(this,e.jquery?e[0]:e):this[0]&&this[0].parentNode?this.first().prevA");
        out.print("ll().length:-1},add:function(e,t){return this.pushStack(Ee.uniqueSort(Ee.merge(this.get(),Ee(e,t))))},addBack:function(e){return this.add(null==e?this.prevObject:this.prevObject.filter(e))}}");
        out.print("),Ee.each({parent:function(e){var t=e.parentNode;return t&&11!==t.nodeType?t:null},parents:function(e){return Ae(e,\"parentNode\")},parentsUntil:function(e,t,n){return Ae(e,\"parentNode\",n)},ne");
        out.print("xt:function(e){return s(e,\"nextSibling\")},prev:function(e){return s(e,\"previousSibling\")},nextAll:function(e){return Ae(e,\"nextSibling\")},prevAll:function(e){return Ae(e,\"previousSibling\")},");
        out.print("nextUntil:function(e,t,n){return Ae(e,\"nextSibling\",n)},prevUntil:function(e,t,n){return Ae(e,\"previousSibling\",n)},siblings:function(e){return Ne((e.parentNode||{}).firstChild,e)},children:");
        out.print("function(e){return Ne(e.firstChild)},contents:function(e){return null!=e.contentDocument&&le(e.contentDocument)?e.contentDocument:(o(e,\"template\")&&(e=e.content||e),Ee.merge([],e.childNodes)");
        out.print(")}},function(e,t){Ee.fn[e]=function(n,r){var i=Ee.map(this,t,n);return\"Until\"!==e.slice(-5)&&(r=n),r&&\"string\"==typeof r&&(i=Ee.filter(r,i)),this.length>1&&(Pe[e]||Ee.uniqueSort(i),Oe.test(e");
        out.print(")&&i.reverse()),this.pushStack(i)}});var Re=/[^\\x20\\t\\r\\n\\f]+/g;Ee.Callbacks=function(e){e=\"string\"==typeof e?u(e):Ee.extend({},e);var t,n,i,o,a=[],s=[],l=-1,c=function(){for(o=o||e.once,i=t");
        out.print("=!0;s.length;l=-1){for(n=s.shift();++l<a.length;){a[l].apply(n[0],n[1])===!1&&e.stopOnFalse&&(l=a.length,n=!1)}}e.memory||(n=!1),t=!1,o&&(a=n?[]:\"\")},f={add:function(){return a&&(n&&!t&&(l=a");
        out.print(".length-1,s.push(n)),function i(t){Ee.each(t,function(t,n){be(n)?e.unique&&f.has(n)||a.push(n):n&&n.length&&\"string\"!==r(n)&&i(n)})}(arguments),n&&!t&&c()),this},remove:function(){return Ee.");
        out.print("each(arguments,function(e,t){for(var n;(n=Ee.inArray(t,a,n))>-1;){a.splice(n,1),l>=n&&l--}}),this},has:function(e){return e?Ee.inArray(e,a)>-1:a.length>0},empty:function(){return a&&(a=[]),t");
        out.print("his},disable:function(){return o=s=[],a=n=\"\",this},disabled:function(){return !a},lock:function(){return o=s=[],n||t||(a=n=\"\"),this},locked:function(){return !!o},fireWith:function(e,n){retu");
        out.print("rn o||(n=n||[],n=[e,n.slice?n.slice():n],s.push(n),t||c()),this},fire:function(){return f.fireWith(this,arguments),this},fired:function(){return !!i}};return f},Ee.extend({Deferred:function(");
        out.print("t){var n=[[\"notify\",\"progress\",Ee.Callbacks(\"memory\"),Ee.Callbacks(\"memory\"),2],[\"resolve\",\"done\",Ee.Callbacks(\"once memory\"),Ee.Callbacks(\"once memory\"),0,\"resolved\"],[\"reject\",\"fail\",Ee.Ca");
        out.print("llbacks(\"once memory\"),Ee.Callbacks(\"once memory\"),1,\"rejected\"]],r=\"pending\",i={state:function(){return r},always:function(){return o.done(arguments).fail(arguments),this},\"catch\":function(");
        out.print("e){return i.then(null,e)},pipe:function(){var e=arguments;return Ee.Deferred(function(t){Ee.each(n,function(n,r){var i=be(e[r[4]])&&e[r[4]];o[r[1]](function(){var e=i&&i.apply(this,arguments");
        out.print(");e&&be(e.promise)?e.promise().progress(t.notify).done(t.resolve).fail(t.reject):t[r[0]+\"With\"](this,i?[e]:arguments)})}),e=null}).promise()},then:function(t,r,i){function o(t,n,r,i){return ");
        out.print("function(){var s=this,u=arguments,f=function(){var e,f;if(!(a>t)){if(e=r.apply(s,u),e===n.promise()){throw new TypeError(\"Thenable self-resolution\")}f=e&&(\"object\"==typeof e||\"function\"==typ");
        out.print("eof e)&&e.then,be(f)?i?f.call(e,o(a,n,l,i),o(a,n,c,i)):(a++,f.call(e,o(a,n,l,i),o(a,n,c,i),o(a,n,l,n.notifyWith))):(r!==l&&(s=void 0,u=[e]),(i||n.resolveWith)(s,u))}},p=i?f:function(){try{f(");
        out.print(")}catch(e){Ee.Deferred.exceptionHook&&Ee.Deferred.exceptionHook(e,p.stackTrace),t+1>=a&&(r!==c&&(s=void 0,u=[e]),n.rejectWith(s,u))}};t?p():(Ee.Deferred.getStackHook&&(p.stackTrace=Ee.Deferr");
        out.print("ed.getStackHook()),e.setTimeout(p))}}var a=0;return Ee.Deferred(function(e){n[0][3].add(o(0,e,be(i)?i:l,e.notifyWith)),n[1][3].add(o(0,e,be(t)?t:l)),n[2][3].add(o(0,e,be(r)?r:c))}).promise()");
        out.print("},promise:function(e){return null!=e?Ee.extend(e,i):i}},o={};return Ee.each(n,function(e,t){var a=t[2],s=t[5];i[t[1]]=a.add,s&&a.add(function(){r=s},n[3-e][2].disable,n[3-e][3].disable,n[0][");
        out.print("2].lock,n[0][3].lock),a.add(t[3].fire),o[t[0]]=function(){return o[t[0]+\"With\"](this===o?void 0:this,arguments),this},o[t[0]+\"With\"]=a.fireWith}),i.promise(o),t&&t.call(o,o),o},when:function");
        out.print("(e){var t=arguments.length,n=t,r=Array(n),i=ce.call(arguments),o=Ee.Deferred(),a=function(e){return function(n){r[e]=this,i[e]=arguments.length>1?ce.call(arguments):n,--t||o.resolveWith(r,i)");
        out.print("}};if(1>=t&&(f(e,o.done(a(n)).resolve,o.reject,!t),\"pending\"===o.state()||be(i[n]&&i[n].then))){return o.then()}for(;n--;){f(i[n],a(n),o.reject)}return o.promise()}});var Me=/^(Eval|Internal");
        out.print("|Range|Reference|Syntax|Type|URI)Error$/;Ee.Deferred.exceptionHook=function(t,n){e.console&&e.console.warn&&t&&Me.test(t.name)&&e.console.warn(\"jQuery.Deferred exception: \"+t.message,t.stack");
        out.print(",n)},Ee.readyException=function(t){e.setTimeout(function(){throw t})};var Ie=Ee.Deferred();Ee.fn.ready=function(e){return Ie.then(e)[\"catch\"](function(e){Ee.readyException(e)}),this},Ee.exte");
        out.print("nd({isReady:!1,readyWait:1,ready:function(e){(e===!0?--Ee.readyWait:Ee.isReady)||(Ee.isReady=!0,e!==!0&&--Ee.readyWait>0||Ie.resolveWith(Te,[Ee]))}}),Ee.ready.then=Ie.then,\"complete\"===Te.re");
        out.print("adyState||\"loading\"!==Te.readyState&&!Te.documentElement.doScroll?e.setTimeout(Ee.ready):(Te.addEventListener(\"DOMContentLoaded\",p),e.addEventListener(\"load\",p));var We=function(e,t,n,i,o,a,");
        out.print("s){var u=0,l=e.length,c=null==n;if(\"object\"===r(n)){o=!0;for(u in n){We(e,t,u,n[u],!0,a,s)}}else{if(void 0!==i&&(o=!0,be(i)||(s=!0),c&&(s?(t.call(e,i),t=null):(c=t,t=function(e,t,n){return c");
        out.print(".call(Ee(e),n)})),t)){for(;l>u;u++){t(e[u],n,s?i:i.call(e[u],u,t(e[u],n)))}}}return o?e:c?t.call(e):l?t(e[0],n):a},Fe=/^-ms-/,$e=/-([a-z])/g,Be=function(e){return 1===e.nodeType||9===e.nodeT");
        out.print("ype||!+e.nodeType};g.uid=1,g.prototype={cache:function(e){var t=e[this.expando];return t||(t={},Be(e)&&(e.nodeType?e[this.expando]=t:Object.defineProperty(e,this.expando,{value:t,configurabl");
        out.print("e:!0}))),t},set:function(e,t,n){var r,i=this.cache(e);if(\"string\"==typeof t){i[h(t)]=n}else{for(r in t){i[h(r)]=t[r]}}return i},get:function(e,t){return void 0===t?this.cache(e):e[this.expan");
        out.print("do]&&e[this.expando][h(t)]},access:function(e,t,n){return void 0===t||t&&\"string\"==typeof t&&void 0===n?this.get(e,t):(this.set(e,t,n),void 0!==n?n:t)},remove:function(e,t){var n,r=e[this.ex");
        out.print("pando];if(void 0!==r){if(void 0!==t){Array.isArray(t)?t=t.map(h):(t=h(t),t=t in r?[t]:t.match(Re)||[]),n=t.length;for(;n--;){delete r[t[n]]}}(void 0===t||Ee.isEmptyObject(r))&&(e.nodeType?e[");
        out.print("this.expando]=void 0:delete e[this.expando])}},hasData:function(e){var t=e[this.expando];return void 0!==t&&!Ee.isEmptyObject(t)}};var _e=new g,ze=new g,Ue=/^(?:\\{[\\w\\W]*\\}|\\[[\\w\\W]*\\])$/,Xe");
        out.print("=/[A-Z]/g;Ee.extend({hasData:function(e){return ze.hasData(e)||_e.hasData(e)},data:function(e,t,n){return ze.access(e,t,n)},removeData:function(e,t){ze.remove(e,t)},_data:function(e,t,n){ret");
        out.print("urn _e.access(e,t,n)},_removeData:function(e,t){_e.remove(e,t)}}),Ee.fn.extend({data:function(e,t){var n,r,i,o=this[0],a=o&&o.attributes;if(void 0===e){if(this.length&&(i=ze.get(o),1===o.nod");
        out.print("eType&&!_e.get(o,\"hasDataAttrs\"))){for(n=a.length;n--;){a[n]&&(r=a[n].name,0===r.indexOf(\"data-\")&&(r=h(r.slice(5)),v(o,r,i[r])))}_e.set(o,\"hasDataAttrs\",!0)}return i}return\"object\"==typeof ");
        out.print("e?this.each(function(){ze.set(this,e)}):We(this,function(t){var n;if(o&&void 0===t){if(n=ze.get(o,e),void 0!==n){return n}if(n=v(o,e),void 0!==n){return n}}else{this.each(function(){ze.set(t");
        out.print("his,e,t)})}},null,t,arguments.length>1,null,!0)},removeData:function(e){return this.each(function(){ze.remove(this,e)})}}),Ee.extend({queue:function(e,t,n){var r;return e?(t=(t||\"fx\")+\"queue");
        out.print("\",r=_e.get(e,t),n&&(!r||Array.isArray(n)?r=_e.access(e,t,Ee.makeArray(n)):r.push(n)),r||[]):void 0},dequeue:function(e,t){t=t||\"fx\";var n=Ee.queue(e,t),r=n.length,i=n.shift(),o=Ee._queueHook");
        out.print("s(e,t),a=function(){Ee.dequeue(e,t)};\"inprogress\"===i&&(i=n.shift(),r--),i&&(\"fx\"===t&&n.unshift(\"inprogress\"),delete o.stop,i.call(e,a,o)),!r&&o&&o.empty.fire()},_queueHooks:function(e,t){v");
        out.print("ar n=t+\"queueHooks\";return _e.get(e,n)||_e.access(e,n,{empty:Ee.Callbacks(\"once memory\").add(function(){_e.remove(e,[t+\"queue\",n])})})}}),Ee.fn.extend({queue:function(e,t){var n=2;return\"str");
        out.print("ing\"!=typeof e&&(t=e,e=\"fx\",n--),arguments.length<n?Ee.queue(this[0],e):void 0===t?this:this.each(function(){var n=Ee.queue(this,e,t);Ee._queueHooks(this,e),\"fx\"===e&&\"inprogress\"!==n[0]&&Ee");
        out.print(".dequeue(this,e)})},dequeue:function(e){return this.each(function(){Ee.dequeue(this,e)})},clearQueue:function(e){return this.queue(e||\"fx\",[])},promise:function(e,t){var n,r=1,i=Ee.Deferred(");
        out.print("),o=this,a=this.length,s=function(){--r||i.resolveWith(o,[o])};for(\"string\"!=typeof e&&(t=e,e=void 0),e=e||\"fx\";a--;){n=_e.get(o[a],e+\"queueHooks\"),n&&n.empty&&(r++,n.empty.add(s))}return s(");
        out.print("),i.promise(t)}});var Ve=/[+-]?(?:\\d*\\.|)\\d+(?:[eE][+-]?\\d+|)/.source,Ge=RegExp(\"^(?:([+-])=|)(\"+Ve+\")([a-z%]*)$\",\"i\"),Ye=[\"Top\",\"Right\",\"Bottom\",\"Left\"],Qe=Te.documentElement,Je=function(e)");
        out.print("{return Ee.contains(e.ownerDocument,e)},Ke={composed:!0};Qe.getRootNode&&(Je=function(e){return Ee.contains(e.ownerDocument,e)||e.getRootNode(Ke)===e.ownerDocument});var Ze=function(e,t){ret");
        out.print("urn e=t||e,\"none\"===e.style.display||\"\"===e.style.display&&Je(e)&&\"none\"===Ee.css(e,\"display\")},et={};Ee.fn.extend({show:function(){return b(this,!0)},hide:function(){return b(this)},toggle:");
        out.print("function(e){return\"boolean\"==typeof e?e?this.show():this.hide():this.each(function(){Ze(this)?Ee(this).show():Ee(this).hide()})}});var tt=/^(?:checkbox|radio)$/i,nt=/<([a-z][^\\/\\0>\\x20\\t\\r\\n");
        out.print("\\f]*)/i,rt=/^$|^module$|\\/(?:java|ecma)script/i;!function(){var e=Te.createDocumentFragment(),t=e.appendChild(Te.createElement(\"div\")),n=Te.createElement(\"input\");n.setAttribute(\"type\",\"radi");
        out.print("o\"),n.setAttribute(\"checked\",\"checked\"),n.setAttribute(\"name\",\"t\"),t.appendChild(n),xe.checkClone=t.cloneNode(!0).cloneNode(!0).lastChild.checked,t.innerHTML=\"<textarea>x</textarea>\",xe.noCl");
        out.print("oneChecked=!!t.cloneNode(!0).lastChild.defaultValue,t.innerHTML=\"<option></option>\",xe.option=!!t.lastChild}();var it={thead:[1,\"<table>\",\"</table>\"],col:[2,\"<table><colgroup>\",\"</colgroup><");
        out.print("/table>\"],tr:[2,\"<table><tbody>\",\"</tbody></table>\"],td:[3,\"<table><tbody><tr>\",\"</tr></tbody></table>\"],_default:[0,\"\",\"\"]};it.tbody=it.tfoot=it.colgroup=it.caption=it.thead,it.th=it.td,xe.");
        out.print("option||(it.optgroup=it.option=[1,\"<select multiple='multiple'>\",\"</select>\"]);var ot=/<|&#?\\w+;/,at=/^([^.]*)(?:\\.(.+)|)/;Ee.event={global:{},add:function(e,t,n,r,i){var o,a,s,u,l,c,f,p,d,h");
        out.print(",g,m=_e.get(e);if(Be(e)){for(n.handler&&(o=n,n=o.handler,i=o.selector),i&&Ee.find.matchesSelector(Qe,i),n.guid||(n.guid=Ee.guid++),(u=m.events)||(u=m.events=Object.create(null)),(a=m.handle)");
        out.print("||(a=m.handle=function(t){return void 0!==Ee&&Ee.event.triggered!==t.type?Ee.event.dispatch.apply(e,arguments):void 0}),t=(t||\"\").match(Re)||[\"\"],l=t.length;l--;){s=at.exec(t[l])||[],d=g=s[1");
        out.print("],h=(s[2]||\"\").split(\".\").sort(),d&&(f=Ee.event.special[d]||{},d=(i?f.delegateType:f.bindType)||d,f=Ee.event.special[d]||{},c=Ee.extend({type:d,origType:g,data:r,handler:n,guid:n.guid,select");
        out.print("or:i,needsContext:i&&Ee.expr.match.needsContext.test(i),namespace:h.join(\".\")},o),(p=u[d])||(p=u[d]=[],p.delegateCount=0,f.setup&&f.setup.call(e,r,h,a)!==!1||e.addEventListener&&e.addEventLi");
        out.print("stener(d,a)),f.add&&(f.add.call(e,c),c.handler.guid||(c.handler.guid=n.guid)),i?p.splice(p.delegateCount++,0,c):p.push(c),Ee.event.global[d]=!0)}}},remove:function(e,t,n,r,i){var o,a,s,u,l,c");
        out.print(",f,p,d,h,g,m=_e.hasData(e)&&_e.get(e);if(m&&(u=m.events)){for(t=(t||\"\").match(Re)||[\"\"],l=t.length;l--;){if(s=at.exec(t[l])||[],d=g=s[1],h=(s[2]||\"\").split(\".\").sort(),d){for(f=Ee.event.spec");
        out.print("ial[d]||{},d=(r?f.delegateType:f.bindType)||d,p=u[d]||[],s=s[2]&&RegExp(\"(^|\\\\.)\"+h.join(\"\\\\.(?:.*\\\\.|)\")+\"(\\\\.|$)\"),a=o=p.length;o--;){c=p[o],!i&&g!==c.origType||n&&n.guid!==c.guid||s&&!s.t");
        out.print("est(c.namespace)||r&&r!==c.selector&&(\"**\"!==r||!c.selector)||(p.splice(o,1),c.selector&&p.delegateCount--,f.remove&&f.remove.call(e,c))}a&&!p.length&&(f.teardown&&f.teardown.call(e,h,m.hand");
        out.print("le)!==!1||Ee.removeEvent(e,d,m.handle),delete u[d])}else{for(d in u){Ee.event.remove(e,d+t[l],n,r,!0)}}}Ee.isEmptyObject(u)&&_e.remove(e,\"handle events\")}},dispatch:function(e){var t,n,r,i,o");
        out.print(",a,s=Array(arguments.length),u=Ee.event.fix(e),l=(_e.get(this,\"events\")||Object.create(null))[u.type]||[],c=Ee.event.special[u.type]||{};for(s[0]=u,t=1;t<arguments.length;t++){s[t]=arguments");
        out.print("[t]}if(u.delegateTarget=this,!c.preDispatch||c.preDispatch.call(this,u)!==!1){for(a=Ee.event.handlers.call(this,u,l),t=0;(i=a[t++])&&!u.isPropagationStopped();){for(u.currentTarget=i.elem,n=");
        out.print("0;(o=i.handlers[n++])&&!u.isImmediatePropagationStopped();){(!u.rnamespace||o.namespace===!1||u.rnamespace.test(o.namespace))&&(u.handleObj=o,u.data=o.data,r=((Ee.event.special[o.origType]||");
        out.print("{}).handle||o.handler).apply(i.elem,s),void 0!==r&&(u.result=r)===!1&&(u.preventDefault(),u.stopPropagation()))}}return c.postDispatch&&c.postDispatch.call(this,u),u.result}},handlers:functi");
        out.print("on(e,t){var n,r,i,o,a,s=[],u=t.delegateCount,l=e.target;if(u&&l.nodeType&&!(\"click\"===e.type&&e.button>=1)){for(;l!==this;l=l.parentNode||this){if(1===l.nodeType&&(\"click\"!==e.type||l.disabl");
        out.print("ed!==!0)){for(o=[],a={},n=0;u>n;n++){r=t[n],i=r.selector+\" \",void 0===a[i]&&(a[i]=r.needsContext?Ee(i,this).index(l)>-1:Ee.find(i,this,null,[l]).length),a[i]&&o.push(r)}o.length&&s.push({ele");
        out.print("m:l,handlers:o})}}}return l=this,u<t.length&&s.push({elem:l,handlers:t.slice(u)}),s},addProp:function(e,t){Object.defineProperty(Ee.Event.prototype,e,{enumerable:!0,configurable:!0,get:be(t)");
        out.print("?function(){return this.originalEvent?t(this.originalEvent):void 0}:function(){return this.originalEvent?this.originalEvent[e]:void 0},set:function(t){Object.defineProperty(this,e,{enumerabl");
        out.print("e:!0,configurable:!0,writable:!0,value:t})}})},fix:function(e){return e[Ee.expando]?e:new Ee.Event(e)},special:{load:{noBubble:!0},click:{setup:function(e){var t=this||e;return tt.test(t.typ");
        out.print("e)&&t.click&&o(t,\"input\")&&j(t,\"click\",S),!1},trigger:function(e){var t=this||e;return tt.test(t.type)&&t.click&&o(t,\"input\")&&j(t,\"click\"),!0},_default:function(e){var t=e.target;return tt.");
        out.print("test(t.type)&&t.click&&o(t,\"input\")&&_e.get(t,\"click\")||o(t,\"a\")}},beforeunload:{postDispatch:function(e){void 0!==e.result&&e.originalEvent&&(e.originalEvent.returnValue=e.result)}}}},Ee.re");
        out.print("moveEvent=function(e,t,n){e.removeEventListener&&e.removeEventListener(t,n)},Ee.Event=function(e,t){return this instanceof Ee.Event?(e&&e.type?(this.originalEvent=e,this.type=e.type,this.isD");
        out.print("efaultPrevented=e.defaultPrevented||void 0===e.defaultPrevented&&e.returnValue===!1?S:E,this.target=e.target&&3===e.target.nodeType?e.target.parentNode:e.target,this.currentTarget=e.currentT");
        out.print("arget,this.relatedTarget=e.relatedTarget):this.type=e,t&&Ee.extend(this,t),this.timeStamp=e&&e.timeStamp||Date.now(),void (this[Ee.expando]=!0)):new Ee.Event(e,t)},Ee.Event.prototype={constr");
        out.print("uctor:Ee.Event,isDefaultPrevented:E,isPropagationStopped:E,isImmediatePropagationStopped:E,isSimulated:!1,preventDefault:function(){var e=this.originalEvent;this.isDefaultPrevented=S,e&&!thi");
        out.print("s.isSimulated&&e.preventDefault()},stopPropagation:function(){var e=this.originalEvent;this.isPropagationStopped=S,e&&!this.isSimulated&&e.stopPropagation()},stopImmediatePropagation:functio");
        out.print("n(){var e=this.originalEvent;this.isImmediatePropagationStopped=S,e&&!this.isSimulated&&e.stopImmediatePropagation(),this.stopPropagation()}},Ee.each({altKey:!0,bubbles:!0,cancelable:!0,chan");
        out.print("gedTouches:!0,ctrlKey:!0,detail:!0,eventPhase:!0,metaKey:!0,pageX:!0,pageY:!0,shiftKey:!0,view:!0,\"char\":!0,code:!0,charCode:!0,key:!0,keyCode:!0,button:!0,buttons:!0,clientX:!0,clientY:!0,o");
        out.print("ffsetX:!0,offsetY:!0,pointerId:!0,pointerType:!0,screenX:!0,screenY:!0,targetTouches:!0,toElement:!0,touches:!0,which:!0},Ee.event.addProp),Ee.each({focus:\"focusin\",blur:\"focusout\"},function");
        out.print("(e,t){Ee.event.special[e]={setup:function(){return j(this,e,k),!1},trigger:function(){return j(this,e),!0},_default:function(t){return _e.get(t.target,e)},delegateType:t}}),Ee.each({mouseent");
        out.print("er:\"mouseover\",mouseleave:\"mouseout\",pointerenter:\"pointerover\",pointerleave:\"pointerout\"},function(e,t){Ee.event.special[e]={delegateType:t,bindType:t,handle:function(e){var n,r=this,i=e.re");
        out.print("latedTarget,o=e.handleObj;return(!i||i!==r&&!Ee.contains(r,i))&&(e.type=o.origType,n=o.handler.apply(this,arguments),e.type=t),n}}}),Ee.fn.extend({on:function(e,t,n,r){return N(this,e,t,n,r)");
        out.print("},one:function(e,t,n,r){return N(this,e,t,n,r,1)},off:function(e,t,n){var r,i;if(e&&e.preventDefault&&e.handleObj){return r=e.handleObj,Ee(e.delegateTarget).off(r.namespace?r.origType+\".\"+r.");
        out.print("namespace:r.origType,r.selector,r.handler),this}if(\"object\"==typeof e){for(i in e){this.off(i,t,e[i])}return this}return(t===!1||\"function\"==typeof t)&&(n=t,t=void 0),n===!1&&(n=E),this.each");
        out.print("(function(){Ee.event.remove(this,e,n,t)})}});var st=/<script|<style|<link/i,ut=/checked\\s*(?:[^=]|=\\s*.checked.)/i,lt=/^\\s*<!\\[CDATA\\[|\\]\\]>\\s*$/g;Ee.extend({htmlPrefilter:function(e){return");
        out.print(" e},clone:function(e,t,n){var r,i,o,a,s=e.cloneNode(!0),u=Je(e);if(!(xe.noCloneChecked||1!==e.nodeType&&11!==e.nodeType||Ee.isXMLDoc(e))){for(a=w(s),o=w(e),r=0,i=o.length;i>r;r++){O(o[r],a[r");
        out.print("])}}if(t){if(n){for(o=o||w(e),a=a||w(s),r=0,i=o.length;i>r;r++){H(o[r],a[r])}}else{H(e,s)}}return a=w(s,\"script\"),a.length>0&&T(a,!u&&w(e,\"script\")),s},cleanData:function(e){for(var t,n,r,i=");
        out.print("Ee.event.special,o=0;void 0!==(n=e[o]);o++){if(Be(n)){if(t=n[_e.expando]){if(t.events){for(r in t.events){i[r]?Ee.event.remove(n,r):Ee.removeEvent(n,r,t.handle)}}n[_e.expando]=void 0}n[ze.ex");
        out.print("pando]&&(n[ze.expando]=void 0)}}}}),Ee.fn.extend({detach:function(e){return R(this,e,!0)},remove:function(e){return R(this,e)},text:function(e){return We(this,function(e){return void 0===e?E");
        out.print("e.text(this):this.empty().each(function(){(1===this.nodeType||11===this.nodeType||9===this.nodeType)&&(this.textContent=e)})},null,e,arguments.length)},append:function(){return P(this,argume");
        out.print("nts,function(e){if(1===this.nodeType||11===this.nodeType||9===this.nodeType){var t=D(this,e);t.appendChild(e)}})},prepend:function(){return P(this,arguments,function(e){if(1===this.nodeType|");
        out.print("|11===this.nodeType||9===this.nodeType){var t=D(this,e);t.insertBefore(e,t.firstChild)}})},before:function(){return P(this,arguments,function(e){this.parentNode&&this.parentNode.insertBefore");
        out.print("(e,this)})},after:function(){return P(this,arguments,function(e){this.parentNode&&this.parentNode.insertBefore(e,this.nextSibling)})},empty:function(){for(var e,t=0;null!=(e=this[t]);t++){1=");
        out.print("==e.nodeType&&(Ee.cleanData(w(e,!1)),e.textContent=\"\")}return this},clone:function(e,t){return e=null==e?!1:e,t=null==t?e:t,this.map(function(){return Ee.clone(this,e,t)})},html:function(e){");
        out.print("return We(this,function(e){var t=this[0]||{},n=0,r=this.length;if(void 0===e&&1===t.nodeType){return t.innerHTML}if(\"string\"==typeof e&&!st.test(e)&&!it[(nt.exec(e)||[\"\",\"\"])[1].toLowerCase(");
        out.print(")]){e=Ee.htmlPrefilter(e);try{for(;r>n;n++){t=this[n]||{},1===t.nodeType&&(Ee.cleanData(w(t,!1)),t.innerHTML=e)}t=0}catch(i){}}t&&this.empty().append(e)},null,e,arguments.length)},replaceWit");
        out.print("h:function(){var e=[];return P(this,arguments,function(t){var n=this.parentNode;Ee.inArray(this,e)<0&&(Ee.cleanData(w(this)),n&&n.replaceChild(t,this))},e)}}),Ee.each({appendTo:\"append\",prep");
        out.print("endTo:\"prepend\",insertBefore:\"before\",insertAfter:\"after\",replaceAll:\"replaceWith\"},function(e,t){Ee.fn[e]=function(e){for(var n,r=[],i=Ee(e),o=i.length-1,a=0;o>=a;a++){n=a===o?this:this.clo");
        out.print("ne(!0),Ee(i[a])[t](n),pe.apply(r,n.get())}return this.pushStack(r)}});var ct=RegExp(\"^(\"+Ve+\")(?!px)[a-z%]+$\",\"i\"),ft=/^--/,pt=function(t){var n=t.ownerDocument.defaultView;return n&&n.opene");
        out.print("r||(n=e),n.getComputedStyle(t)},dt=function(e,t,n){var r,i,o={};for(i in t){o[i]=e.style[i],e.style[i]=t[i]}r=n.call(e);for(i in t){e.style[i]=o[i]}return r},ht=RegExp(Ye.join(\"|\"),\"i\"),gt=\"");
        out.print("[\\\\x20\\\\t\\\\r\\\\n\\\\f]\",mt=RegExp(\"^\"+gt+\"+|((?:^|[^\\\\\\\\])(?:\\\\\\\\.)*)\"+gt+\"+$\",\"g\");!function(){function t(){if(c){l.style.cssText=\"position:absolute;left:-11111px;width:60px;margin-top:1px;pad");
        out.print("ding:0;border:0\",c.style.cssText=\"position:relative;display:block;box-sizing:border-box;overflow:scroll;margin:auto;border:1px;padding:1px;width:60%;top:1%\",Qe.appendChild(l).appendChild(c);");
        out.print("var t=e.getComputedStyle(c);r=\"1%\"!==t.top,u=12===n(t.marginLeft),c.style.right=\"60%\",a=36===n(t.right),i=36===n(t.width),c.style.position=\"absolute\",o=12===n(c.offsetWidth/3),Qe.removeChild");
        out.print("(l),c=null}}function n(e){return Math.round(parseFloat(e))}var r,i,o,a,s,u,l=Te.createElement(\"div\"),c=Te.createElement(\"div\");c.style&&(c.style.backgroundClip=\"content-box\",c.cloneNode(!0).");
        out.print("style.backgroundClip=\"\",xe.clearCloneStyle=\"content-box\"===c.style.backgroundClip,Ee.extend(xe,{boxSizingReliable:function(){return t(),i},pixelBoxStyles:function(){return t(),a},pixelPositi");
        out.print("on:function(){return t(),r},reliableMarginLeft:function(){return t(),u},scrollboxSize:function(){return t(),o},reliableTrDimensions:function(){var t,n,r,i;return null==s&&(t=Te.createElement");
        out.print("(\"table\"),n=Te.createElement(\"tr\"),r=Te.createElement(\"div\"),t.style.cssText=\"position:absolute;left:-11111px;border-collapse:separate\",n.style.cssText=\"border:1px solid\",n.style.height=\"1px");
        out.print("\",r.style.height=\"9px\",r.style.display=\"block\",Qe.appendChild(t).appendChild(n).appendChild(r),i=e.getComputedStyle(n),s=parseInt(i.height,10)+parseInt(i.borderTopWidth,10)+parseInt(i.border");
        out.print("BottomWidth,10)===n.offsetHeight,Qe.removeChild(t)),s}}))}();var vt=[\"Webkit\",\"Moz\",\"ms\"],yt=Te.createElement(\"div\").style,xt={},bt=/^(none|table(?!-c[ea]).+)/,wt={position:\"absolute\",visibi");
        out.print("lity:\"hidden\",display:\"block\"},Tt={letterSpacing:\"0\",fontWeight:\"400\"};Ee.extend({cssHooks:{opacity:{get:function(e,t){if(t){var n=M(e,\"opacity\");return\"\"===n?\"1\":n}}}},cssNumber:{animationI");
        out.print("terationCount:!0,columnCount:!0,fillOpacity:!0,flexGrow:!0,flexShrink:!0,fontWeight:!0,gridArea:!0,gridColumn:!0,gridColumnEnd:!0,gridColumnStart:!0,gridRow:!0,gridRowEnd:!0,gridRowStart:!0,");
        out.print("lineHeight:!0,opacity:!0,order:!0,orphans:!0,widows:!0,zIndex:!0,zoom:!0},cssProps:{},style:function(e,t,n,r){if(e&&3!==e.nodeType&&8!==e.nodeType&&e.style){var i,o,a,s=h(t),u=ft.test(t),l=e");
        out.print(".style;return u||(t=F(s)),a=Ee.cssHooks[t]||Ee.cssHooks[s],void 0===n?a&&\"get\" in a&&void 0!==(i=a.get(e,!1,r))?i:l[t]:(o=typeof n,\"string\"===o&&(i=Ge.exec(n))&&i[1]&&(n=y(e,t,i),o=\"number\")");
        out.print(",null!=n&&n===n&&(\"number\"!==o||u||(n+=i&&i[3]||(Ee.cssNumber[s]?\"\":\"px\")),xe.clearCloneStyle||\"\"!==n||0!==t.indexOf(\"background\")||(l[t]=\"inherit\"),a&&\"set\" in a&&void 0===(n=a.set(e,n,r))|");
        out.print("|(u?l.setProperty(t,n):l[t]=n)),void 0)}},css:function(e,t,n,r){var i,o,a,s=h(t),u=ft.test(t);return u||(t=F(s)),a=Ee.cssHooks[t]||Ee.cssHooks[s],a&&\"get\" in a&&(i=a.get(e,!0,n)),void 0===i&");
        out.print("&(i=M(e,t,r)),\"normal\"===i&&t in Tt&&(i=Tt[t]),\"\"===n||n?(o=parseFloat(i),n===!0||isFinite(o)?o||0:i):i}}),Ee.each([\"height\",\"width\"],function(e,t){Ee.cssHooks[t]={get:function(e,n,r){return");
        out.print(" n?!bt.test(Ee.css(e,\"display\"))||e.getClientRects().length&&e.getBoundingClientRect().width?z(e,t,r):dt(e,wt,function(){return z(e,t,r)}):void 0},set:function(e,n,r){var i,o=pt(e),a=!xe.scr");
        out.print("ollboxSize()&&\"absolute\"===o.position,s=a||r,u=s&&\"border-box\"===Ee.css(e,\"boxSizing\",!1,o),l=r?_(e,t,r,u,o):0;return u&&a&&(l-=Math.ceil(e[\"offset\"+t[0].toUpperCase()+t.slice(1)]-parseFloat");
        out.print("(o[t])-_(e,t,\"border\",!1,o)-0.5)),l&&(i=Ge.exec(n))&&\"px\"!==(i[3]||\"px\")&&(e.style[t]=n,n=Ee.css(e,t)),B(e,n,l)}}}),Ee.cssHooks.marginLeft=I(xe.reliableMarginLeft,function(e,t){return t?(par");
        out.print("seFloat(M(e,\"marginLeft\"))||e.getBoundingClientRect().left-dt(e,{marginLeft:0},function(){return e.getBoundingClientRect().left}))+\"px\":void 0}),Ee.each({margin:\"\",padding:\"\",border:\"Width\"}");
        out.print(",function(e,t){Ee.cssHooks[e+t]={expand:function(n){for(var r=0,i={},o=\"string\"==typeof n?n.split(\" \"):[n];4>r;r++){i[e+Ye[r]+t]=o[r]||o[r-2]||o[0]}return i}},\"margin\"!==e&&(Ee.cssHooks[e+t]");
        out.print(".set=B)}),Ee.fn.extend({css:function(e,t){return We(this,function(e,t,n){var r,i,o={},a=0;if(Array.isArray(t)){for(r=pt(e),i=t.length;i>a;a++){o[t[a]]=Ee.css(e,t[a],!1,r)}return o}return voi");
        out.print("d 0!==n?Ee.style(e,t,n):Ee.css(e,t)},e,t,arguments.length>1)}}),Ee.Tween=U,U.prototype={constructor:U,init:function(e,t,n,r,i,o){this.elem=e,this.prop=n,this.easing=i||Ee.easing._default,thi");
        out.print("s.options=t,this.start=this.now=this.cur(),this.end=r,this.unit=o||(Ee.cssNumber[n]?\"\":\"px\")},cur:function(){var e=U.propHooks[this.prop];return e&&e.get?e.get(this):U.propHooks._default.get");
        out.print("(this)},run:function(e){var t,n=U.propHooks[this.prop];return this.options.duration?this.pos=t=Ee.easing[this.easing](e,this.options.duration*e,0,1,this.options.duration):this.pos=t=e,this.n");
        out.print("ow=(this.end-this.start)*t+this.start,this.options.step&&this.options.step.call(this.elem,this.now,this),n&&n.set?n.set(this):U.propHooks._default.set(this),this}},U.prototype.init.prototype");
        out.print("=U.prototype,U.propHooks={_default:{get:function(e){var t;return 1!==e.elem.nodeType||null!=e.elem[e.prop]&&null==e.elem.style[e.prop]?e.elem[e.prop]:(t=Ee.css(e.elem,e.prop,\"\"),t&&\"auto\"!==");
        out.print("t?t:0)},set:function(e){Ee.fx.step[e.prop]?Ee.fx.step[e.prop](e):1!==e.elem.nodeType||!Ee.cssHooks[e.prop]&&null==e.elem.style[F(e.prop)]?e.elem[e.prop]=e.now:Ee.style(e.elem,e.prop,e.now+e.");
        out.print("unit)}}},U.propHooks.scrollTop=U.propHooks.scrollLeft={set:function(e){e.elem.nodeType&&e.elem.parentNode&&(e.elem[e.prop]=e.now)}},Ee.easing={linear:function(e){return e},swing:function(e){");
        out.print("return 0.5-Math.cos(e*Math.PI)/2},_default:\"swing\"},Ee.fx=U.prototype.init,Ee.fx.step={};var Ct,St,Et=/^(?:toggle|show|hide)$/,kt=/queueHooks$/;Ee.Animation=Ee.extend(K,{tweeners:{\"*\":[funct");
        out.print("ion(e,t){var n=this.createTween(e,t);return y(n.elem,e,Ge.exec(t),n),n}]},tweener:function(e,t){be(e)?(t=e,e=[\"*\"]):e=e.match(Re);for(var n,r=0,i=e.length;i>r;r++){n=e[r],K.tweeners[n]=K.twe");
        out.print("eners[n]||[],K.tweeners[n].unshift(t)}},prefilters:[Q],prefilter:function(e,t){t?K.prefilters.unshift(e):K.prefilters.push(e)}}),Ee.speed=function(e,t,n){var r=e&&\"object\"==typeof e?Ee.exten");
        out.print("d({},e):{complete:n||!n&&t||be(e)&&e,duration:e,easing:n&&t||t&&!be(t)&&t};return Ee.fx.off?r.duration=0:\"number\"!=typeof r.duration&&(r.duration in Ee.fx.speeds?r.duration=Ee.fx.speeds[r.du");
        out.print("ration]:r.duration=Ee.fx.speeds._default),(null==r.queue||r.queue===!0)&&(r.queue=\"fx\"),r.old=r.complete,r.complete=function(){be(r.old)&&r.old.call(this),r.queue&&Ee.dequeue(this,r.queue)},");
        out.print("r},Ee.fn.extend({fadeTo:function(e,t,n,r){return this.filter(Ze).css(\"opacity\",0).show().end().animate({opacity:t},e,n,r)},animate:function(e,t,n,r){var i=Ee.isEmptyObject(e),o=Ee.speed(t,n,");
        out.print("r),a=function(){var t=K(this,Ee.extend({},e),o);(i||_e.get(this,\"finish\"))&&t.stop(!0)};return a.finish=a,i||o.queue===!1?this.each(a):this.queue(o.queue,a)},stop:function(e,t,n){var r=funct");
        out.print("ion(e){var t=e.stop;delete e.stop,t(n)};return\"string\"!=typeof e&&(n=t,t=e,e=void 0),t&&this.queue(e||\"fx\",[]),this.each(function(){var t=!0,i=null!=e&&e+\"queueHooks\",o=Ee.timers,a=_e.get(th");
        out.print("is);if(i){a[i]&&a[i].stop&&r(a[i])}else{for(i in a){a[i]&&a[i].stop&&kt.test(i)&&r(a[i])}}for(i=o.length;i--;){o[i].elem!==this||null!=e&&o[i].queue!==e||(o[i].anim.stop(n),t=!1,o.splice(i,1");
        out.print("))}(t||!n)&&Ee.dequeue(this,e)})},finish:function(e){return e!==!1&&(e=e||\"fx\"),this.each(function(){var t,n=_e.get(this),r=n[e+\"queue\"],i=n[e+\"queueHooks\"],o=Ee.timers,a=r?r.length:0;for(n.");
        out.print("finish=!0,Ee.queue(this,e,[]),i&&i.stop&&i.stop.call(this,!0),t=o.length;t--;){o[t].elem===this&&o[t].queue===e&&(o[t].anim.stop(!0),o.splice(t,1))}for(t=0;a>t;t++){r[t]&&r[t].finish&&r[t].f");
        out.print("inish.call(this)}delete n.finish})}}),Ee.each([\"toggle\",\"show\",\"hide\"],function(e,t){var n=Ee.fn[t];Ee.fn[t]=function(e,r,i){return null==e||\"boolean\"==typeof e?n.apply(this,arguments):this.");
        out.print("animate(G(t,!0),e,r,i)}}),Ee.each({slideDown:G(\"show\"),slideUp:G(\"hide\"),slideToggle:G(\"toggle\"),fadeIn:{opacity:\"show\"},fadeOut:{opacity:\"hide\"},fadeToggle:{opacity:\"toggle\"}},function(e,t)");
        out.print("{Ee.fn[e]=function(e,n,r){return this.animate(t,e,n,r)}}),Ee.timers=[],Ee.fx.tick=function(){var e,t=0,n=Ee.timers;for(Ct=Date.now();t<n.length;t++){e=n[t],e()||n[t]!==e||n.splice(t--,1)}n.l");
        out.print("ength||Ee.fx.stop(),Ct=void 0},Ee.fx.timer=function(e){Ee.timers.push(e),Ee.fx.start()},Ee.fx.interval=13,Ee.fx.start=function(){St||(St=!0,X())},Ee.fx.stop=function(){St=null},Ee.fx.speeds=");
        out.print("{slow:600,fast:200,_default:400},Ee.fn.delay=function(t,n){return t=Ee.fx?Ee.fx.speeds[t]||t:t,n=n||\"fx\",this.queue(n,function(n,r){var i=e.setTimeout(n,t);r.stop=function(){e.clearTimeout(i");
        out.print(")}})},function(){var e=Te.createElement(\"input\"),t=Te.createElement(\"select\"),n=t.appendChild(Te.createElement(\"option\"));e.type=\"checkbox\",xe.checkOn=\"\"!==e.value,xe.optSelected=n.selected,");
        out.print("e=Te.createElement(\"input\"),e.value=\"t\",e.type=\"radio\",xe.radioValue=\"t\"===e.value}();var At,Nt=Ee.expr.attrHandle;Ee.fn.extend({attr:function(e,t){return We(this,Ee.attr,e,t,arguments.lengt");
        out.print("h>1)},removeAttr:function(e){return this.each(function(){Ee.removeAttr(this,e)})}}),Ee.extend({attr:function(e,t,n){var r,i,o=e.nodeType;if(3!==o&&8!==o&&2!==o){return void 0===e.getAttribut");
        out.print("e?Ee.prop(e,t,n):(1===o&&Ee.isXMLDoc(e)||(i=Ee.attrHooks[t.toLowerCase()]||(Ee.expr.match.bool.test(t)?At:void 0)),void 0!==n?null===n?void Ee.removeAttr(e,t):i&&\"set\" in i&&void 0!==(r=i.se");
        out.print("t(e,n,t))?r:(e.setAttribute(t,n+\"\"),n):i&&\"get\" in i&&null!==(r=i.get(e,t))?r:(r=Ee.find.attr(e,t),null==r?void 0:r))}},attrHooks:{type:{set:function(e,t){if(!xe.radioValue&&\"radio\"===t&&o(e");
        out.print(",\"input\")){var n=e.value;return e.setAttribute(\"type\",t),n&&(e.value=n),t}}}},removeAttr:function(e,t){var n,r=0,i=t&&t.match(Re);if(i&&1===e.nodeType){for(;n=i[r++];){e.removeAttribute(n)}}");
        out.print("}),At={set:function(e,t,n){return t===!1?Ee.removeAttr(e,n):e.setAttribute(n,n),n}},Ee.each(Ee.expr.match.bool.source.match(/\\w+/g),function(e,t){var n=Nt[t]||Ee.find.attr;Nt[t]=function(e,");
        out.print("t,r){var i,o,a=t.toLowerCase();return r||(o=Nt[a],Nt[a]=i,i=null!=n(e,t,r)?a:null,Nt[a]=o),i}});var jt=/^(?:input|select|textarea|button)$/i,Dt=/^(?:a|area)$/i;Ee.fn.extend({prop:function(e,");
        out.print("t){return We(this,Ee.prop,e,t,arguments.length>1)},removeProp:function(e){return this.each(function(){delete this[Ee.propFix[e]||e]})}}),Ee.extend({prop:function(e,t,n){var r,i,o=e.nodeType;");
        out.print("if(3!==o&&8!==o&&2!==o){return 1===o&&Ee.isXMLDoc(e)||(t=Ee.propFix[t]||t,i=Ee.propHooks[t]),void 0!==n?i&&\"set\" in i&&void 0!==(r=i.set(e,n,t))?r:e[t]=n:i&&\"get\" in i&&null!==(r=i.get(e,t))");
        out.print("?r:e[t]}},propHooks:{tabIndex:{get:function(e){var t=Ee.find.attr(e,\"tabindex\");return t?parseInt(t,10):jt.test(e.nodeName)||Dt.test(e.nodeName)&&e.href?0:-1}}},propFix:{\"for\":\"htmlFor\",\"cla");
        out.print("ss\":\"className\"}}),xe.optSelected||(Ee.propHooks.selected={get:function(e){var t=e.parentNode;return t&&t.parentNode&&t.parentNode.selectedIndex,null},set:function(e){var t=e.parentNode;t&&(");
        out.print("t.selectedIndex,t.parentNode&&t.parentNode.selectedIndex)}}),Ee.each([\"tabIndex\",\"readOnly\",\"maxLength\",\"cellSpacing\",\"cellPadding\",\"rowSpan\",\"colSpan\",\"useMap\",\"frameBorder\",\"contentEditabl");
        out.print("e\"],function(){Ee.propFix[this.toLowerCase()]=this}),Ee.fn.extend({addClass:function(e){var t,n,r,i,o,a;return be(e)?this.each(function(t){Ee(this).addClass(e.call(this,t,ee(this)))}):(t=te(");
        out.print("e),t.length?this.each(function(){if(r=ee(this),n=1===this.nodeType&&\" \"+Z(r)+\" \"){for(o=0;o<t.length;o++){i=t[o],n.indexOf(\" \"+i+\" \")<0&&(n+=i+\" \")}a=Z(n),r!==a&&this.setAttribute(\"class\",a)");
        out.print("}}):this)},removeClass:function(e){var t,n,r,i,o,a;return be(e)?this.each(function(t){Ee(this).removeClass(e.call(this,t,ee(this)))}):arguments.length?(t=te(e),t.length?this.each(function(){");
        out.print("if(r=ee(this),n=1===this.nodeType&&\" \"+Z(r)+\" \"){for(o=0;o<t.length;o++){for(i=t[o];n.indexOf(\" \"+i+\" \")>-1;){n=n.replace(\" \"+i+\" \",\" \")}}a=Z(n),r!==a&&this.setAttribute(\"class\",a)}}):this):");
        out.print("this.attr(\"class\",\"\")},toggleClass:function(e,t){var n,r,i,o,a=typeof e,s=\"string\"===a||Array.isArray(e);return be(e)?this.each(function(n){Ee(this).toggleClass(e.call(this,n,ee(this),t),t)}");
        out.print("):\"boolean\"==typeof t&&s?t?this.addClass(e):this.removeClass(e):(n=te(e),this.each(function(){if(s){for(o=Ee(this),i=0;i<n.length;i++){r=n[i],o.hasClass(r)?o.removeClass(r):o.addClass(r)}}el");
        out.print("se{(void 0===e||\"boolean\"===a)&&(r=ee(this),r&&_e.set(this,\"__className__\",r),this.setAttribute&&this.setAttribute(\"class\",r||e===!1?\"\":_e.get(this,\"__className__\")||\"\"))}}))},hasClass:funct");
        out.print("ion(e){var t,n,r=0;for(t=\" \"+e+\" \";n=this[r++];){if(1===n.nodeType&&(\" \"+Z(ee(n))+\" \").indexOf(t)>-1){return !0}}return !1}});var qt=/\\r/g;Ee.fn.extend({val:function(e){var t,n,r,i=this[0];i");
        out.print("f(arguments.length){return r=be(e),this.each(function(n){var i;1===this.nodeType&&(i=r?e.call(this,n,Ee(this).val()):e,null==i?i=\"\":\"number\"==typeof i?i+=\"\":Array.isArray(i)&&(i=Ee.map(i,fun");
        out.print("ction(e){return null==e?\"\":e+\"\"})),t=Ee.valHooks[this.type]||Ee.valHooks[this.nodeName.toLowerCase()],t&&\"set\" in t&&void 0!==t.set(this,i,\"value\")||(this.value=i))})}if(i){return t=Ee.valHo");
        out.print("oks[i.type]||Ee.valHooks[i.nodeName.toLowerCase()],t&&\"get\" in t&&void 0!==(n=t.get(i,\"value\"))?n:(n=i.value,\"string\"==typeof n?n.replace(qt,\"\"):null==n?\"\":n)}}}),Ee.extend({valHooks:{option");
        out.print(":{get:function(e){var t=Ee.find.attr(e,\"value\");return null!=t?t:Z(Ee.text(e))}},select:{get:function(e){var t,n,r,i=e.options,a=e.selectedIndex,s=\"select-one\"===e.type,u=s?null:[],l=s?a+1:i");
        out.print(".length;for(r=0>a?l:s?a:0;l>r;r++){if(n=i[r],(n.selected||r===a)&&!n.disabled&&(!n.parentNode.disabled||!o(n.parentNode,\"optgroup\"))){if(t=Ee(n).val(),s){return t}u.push(t)}}return u},set:fu");
        out.print("nction(e,t){for(var n,r,i=e.options,o=Ee.makeArray(t),a=i.length;a--;){r=i[a],(r.selected=Ee.inArray(Ee.valHooks.option.get(r),o)>-1)&&(n=!0)}return n||(e.selectedIndex=-1),o}}}}),Ee.each([\"");
        out.print("radio\",\"checkbox\"],function(){Ee.valHooks[this]={set:function(e,t){return Array.isArray(t)?e.checked=Ee.inArray(Ee(e).val(),t)>-1:void 0}},xe.checkOn||(Ee.valHooks[this].get=function(e){retu");
        out.print("rn null===e.getAttribute(\"value\")?\"on\":e.value})}),xe.focusin=\"onfocusin\" in e;var Lt=/^(?:focusinfocus|focusoutblur)$/,Ht=function(e){e.stopPropagation()};Ee.extend(Ee.event,{trigger:functi");
        out.print("on(t,n,r,i){var o,a,s,u,l,c,f,p,d=[r||Te],h=me.call(t,\"type\")?t.type:t,g=me.call(t,\"namespace\")?t.namespace.split(\".\"):[];if(a=p=s=r=r||Te,3!==r.nodeType&&8!==r.nodeType&&!Lt.test(h+Ee.event");
        out.print(".triggered)&&(h.indexOf(\".\")>-1&&(g=h.split(\".\"),h=g.shift(),g.sort()),l=h.indexOf(\":\")<0&&\"on\"+h,t=t[Ee.expando]?t:new Ee.Event(h,\"object\"==typeof t&&t),t.isTrigger=i?2:3,t.namespace=g.join");
        out.print("(\".\"),t.rnamespace=t.namespace?RegExp(\"(^|\\\\.)\"+g.join(\"\\\\.(?:.*\\\\.|)\")+\"(\\\\.|$)\"):null,t.result=void 0,t.target||(t.target=r),n=null==n?[t]:Ee.makeArray(n,[t]),f=Ee.event.special[h]||{},i||");
        out.print("!f.trigger||f.trigger.apply(r,n)!==!1)){if(!i&&!f.noBubble&&!we(r)){for(u=f.delegateType||h,Lt.test(u+h)||(a=a.parentNode);a;a=a.parentNode){d.push(a),s=a}s===(r.ownerDocument||Te)&&d.push(s");
        out.print(".defaultView||s.parentWindow||e)}for(o=0;(a=d[o++])&&!t.isPropagationStopped();){p=a,t.type=o>1?u:f.bindType||h,c=(_e.get(a,\"events\")||Object.create(null))[t.type]&&_e.get(a,\"handle\"),c&&c.a");
        out.print("pply(a,n),c=l&&a[l],c&&c.apply&&Be(a)&&(t.result=c.apply(a,n),t.result===!1&&t.preventDefault())}return t.type=h,i||t.isDefaultPrevented()||f._default&&f._default.apply(d.pop(),n)!==!1||!Be(");
        out.print("r)||l&&be(r[h])&&!we(r)&&(s=r[l],s&&(r[l]=null),Ee.event.triggered=h,t.isPropagationStopped()&&p.addEventListener(h,Ht),r[h](),t.isPropagationStopped()&&p.removeEventListener(h,Ht),Ee.event.");
        out.print("triggered=void 0,s&&(r[l]=s)),t.result}},simulate:function(e,t,n){var r=Ee.extend(new Ee.Event,n,{type:e,isSimulated:!0});Ee.event.trigger(r,null,t)}}),Ee.fn.extend({trigger:function(e,t){re");
        out.print("turn this.each(function(){Ee.event.trigger(e,t,this)})},triggerHandler:function(e,t){var n=this[0];return n?Ee.event.trigger(e,t,n,!0):void 0}}),xe.focusin||Ee.each({focus:\"focusin\",blur:\"fo");
        out.print("cusout\"},function(e,t){var n=function(e){Ee.event.simulate(t,e.target,Ee.event.fix(e))};Ee.event.special[t]={setup:function(){var r=this.ownerDocument||this.document||this,i=_e.access(r,t);i");
        out.print("||r.addEventListener(e,n,!0),_e.access(r,t,(i||0)+1)},teardown:function(){var r=this.ownerDocument||this.document||this,i=_e.access(r,t)-1;i?_e.access(r,t,i):(r.removeEventListener(e,n,!0),_");
        out.print("e.remove(r,t))}}});var Ot=e.location,Pt={guid:Date.now()},Rt=/\\?/;Ee.parseXML=function(t){var n,r;if(!t||\"string\"!=typeof t){return null}try{n=(new e.DOMParser).parseFromString(t,\"text/xml\")");
        out.print("}catch(i){}return r=n&&n.getElementsByTagName(\"parsererror\")[0],(!n||r)&&Ee.error(\"Invalid XML: \"+(r?Ee.map(r.childNodes,function(e){return e.textContent}).join(\"\\n\"):t)),n};var Mt=/\\[\\]$/,I");
        out.print("t=/\\r?\\n/g,Wt=/^(?:submit|button|image|reset|file)$/i,Ft=/^(?:input|select|textarea|keygen)/i;Ee.param=function(e,t){var n,r=[],i=function(e,t){var n=be(t)?t():t;r[r.length]=encodeURICompone");
        out.print("nt(e)+\"=\"+encodeURIComponent(null==n?\"\":n)};if(null==e){return\"\"}if(Array.isArray(e)||e.jquery&&!Ee.isPlainObject(e)){Ee.each(e,function(){i(this.name,this.value)})}else{for(n in e){ne(n,e[n");
        out.print("],t,i)}}return r.join(\"&\")},Ee.fn.extend({serialize:function(){var e=this.serializeArray(),t=$(\"input[type=radio],input[type=checkbox]\",this),n={};return $.each(t,function(){n.hasOwnProperty");
        out.print("(this.name)||0==$(\"input[name='\"+this.name+\"']:checked\").length&&(n[this.name]=\"\",e.push({name:this.name,value:\"\"}))}),Ee.param(e)},serializeArray:function(){return this.map(function(){var e");
        out.print("=Ee.prop(this,\"elements\");return e?Ee.makeArray(e):this}).filter(function(){var e=this.type;return this.name&&!Ee(this).is(\":disabled\")&&Ft.test(this.nodeName)&&!Wt.test(e)&&(this.checked||!");
        out.print("tt.test(e))}).map(function(e,t){var n=Ee(this).val();return null==n?null:Array.isArray(n)?Ee.map(n,function(e){return{name:t.name,value:e.replace(It,\"\\r\\n\")}}):{name:t.name,value:n.replace(I");
        out.print("t,\"\\r\\n\")}}).get()}});var $t=/%20/g,Bt=/#.*$/,_t=/([?&])_=[^&]*/,zt=/^(.*?):[ \\t]*([^\\r\\n]*)$/gm,Ut=/^(?:about|app|app-storage|.+-extension|file|res|widget):$/,Xt=/^(?:GET|HEAD)$/,Vt=/^\\/\\//");
        out.print(",Gt={},Yt={},Qt=\"*/\".concat(\"*\"),Jt=Te.createElement(\"a\");Jt.href=Ot.href,Ee.extend({active:0,lastModified:{},etag:{},ajaxSettings:{url:Ot.href,type:\"GET\",isLocal:Ut.test(Ot.protocol),global");
        out.print(":!0,processData:!0,async:!0,contentType:\"application/x-www-form-urlencoded; charset=UTF-8\",accepts:{\"*\":Qt,text:\"text/plain\",html:\"text/html\",xml:\"application/xml, text/xml\",json:\"applicatio");
        out.print("n/json, text/javascript\"},contents:{xml:/\\bxml\\b/,html:/\\bhtml/,json:/\\bjson\\b/},responseFields:{xml:\"responseXML\",text:\"responseText\",json:\"responseJSON\"},converters:{\"* text\":String,\"text ");
        out.print("html\":!0,\"text json\":JSON.parse,\"text xml\":Ee.parseXML},flatOptions:{url:!0,context:!0}},ajaxSetup:function(e,t){return t?oe(oe(e,Ee.ajaxSettings),t):oe(Ee.ajaxSettings,e)},ajaxPrefilter:re(");
        out.print("Gt),ajaxTransport:re(Yt),ajax:function(t,n){function r(t,n,r,s){var l,p,d,b,w,T=n;c||(c=!0,u&&e.clearTimeout(u),i=void 0,a=s||\"\",C.readyState=t>0?4:0,l=t>=200&&300>t||304===t,r&&(b=ae(h,C,r)");
        out.print("),!l&&Ee.inArray(\"script\",h.dataTypes)>-1&&Ee.inArray(\"json\",h.dataTypes)<0&&(h.converters[\"text script\"]=function(){}),b=se(h,b,C,l),l?(h.ifModified&&(w=C.getResponseHeader(\"Last-Modified\")");
        out.print(",w&&(Ee.lastModified[o]=w),w=C.getResponseHeader(\"etag\"),w&&(Ee.etag[o]=w)),204===t||\"HEAD\"===h.type?T=\"nocontent\":304===t?T=\"notmodified\":(T=b.state,p=b.data,d=b.error,l=!d)):(d=T,(t||!T)&&");
        out.print("(T=\"error\",0>t&&(t=0))),C.status=t,C.statusText=(n||T)+\"\",l?v.resolveWith(g,[p,T,C]):v.rejectWith(g,[C,T,d]),C.statusCode(x),x=void 0,f&&m.trigger(l?\"ajaxSuccess\":\"ajaxError\",[C,h,l?p:d]),y.");
        out.print("fireWith(g,[C,T]),f&&(m.trigger(\"ajaxComplete\",[C,h]),--Ee.active||Ee.event.trigger(\"ajaxStop\")))}\"object\"==typeof t&&(n=t,t=void 0),n=n||{};var i,o,a,s,u,l,c,f,p,d,h=Ee.ajaxSetup({},n),g=h.");
        out.print("context||h,m=h.context&&(g.nodeType||g.jquery)?Ee(g):Ee.event,v=Ee.Deferred(),y=Ee.Callbacks(\"once memory\"),x=h.statusCode||{},b={},w={},T=\"canceled\",C={readyState:0,getResponseHeader:functi");
        out.print("on(e){var t;if(c){if(!s){for(s={};t=zt.exec(a);){s[t[1].toLowerCase()+\" \"]=(s[t[1].toLowerCase()+\" \"]||[]).concat(t[2])}}t=s[e.toLowerCase()+\" \"]}return null==t?null:t.join(\", \")},getAllResp");
        out.print("onseHeaders:function(){return c?a:null},setRequestHeader:function(e,t){return null==c&&(e=w[e.toLowerCase()]=w[e.toLowerCase()]||e,b[e]=t),this},overrideMimeType:function(e){return null==c&&");
        out.print("(h.mimeType=e),this},statusCode:function(e){var t;if(e){if(c){C.always(e[C.status])}else{for(t in e){x[t]=[x[t],e[t]]}}}return this},abort:function(e){var t=e||T;return i&&i.abort(t),r(0,t),");
        out.print("this}};if(v.promise(C),h.url=((t||h.url||Ot.href)+\"\").replace(Vt,Ot.protocol+\"//\"),h.type=n.method||n.type||h.method||h.type,h.dataTypes=(h.dataType||\"*\").toLowerCase().match(Re)||[\"\"],null=");
        out.print("=h.crossDomain){l=Te.createElement(\"a\");try{l.href=h.url,l.href=l.href,h.crossDomain=Jt.protocol+\"//\"+Jt.host!=l.protocol+\"//\"+l.host}catch(S){h.crossDomain=!0}}if(h.data&&h.processData&&\"st");
        out.print("ring\"!=typeof h.data&&(h.data=Ee.param(h.data,h.traditional)),ie(Gt,h,n,C),c){return C}f=Ee.event&&h.global,f&&0===Ee.active++&&Ee.event.trigger(\"ajaxStart\"),h.type=h.type.toUpperCase(),h.ha");
        out.print("sContent=!Xt.test(h.type),o=h.url.replace(Bt,\"\"),h.hasContent?h.data&&h.processData&&0===(h.contentType||\"\").indexOf(\"application/x-www-form-urlencoded\")&&(h.data=h.data.replace($t,\"+\")):(d=");
        out.print("h.url.slice(o.length),h.data&&(h.processData||\"string\"==typeof h.data)&&(o+=(Rt.test(o)?\"&\":\"?\")+h.data,delete h.data),h.cache===!1&&(o=o.replace(_t,\"$1\"),d=(Rt.test(o)?\"&\":\"?\")+\"_=\"+Pt.guid");
        out.print("+++d),h.url=o+d),h.ifModified&&(Ee.lastModified[o]&&C.setRequestHeader(\"If-Modified-Since\",Ee.lastModified[o]),Ee.etag[o]&&C.setRequestHeader(\"If-None-Match\",Ee.etag[o])),(h.data&&h.hasConte");
        out.print("nt&&h.contentType!==!1||n.contentType)&&C.setRequestHeader(\"Content-Type\",h.contentType),C.setRequestHeader(\"Accept\",h.dataTypes[0]&&h.accepts[h.dataTypes[0]]?h.accepts[h.dataTypes[0]]+(\"*\"!");
        out.print("==h.dataTypes[0]?\", \"+Qt+\"; q=0.01\":\"\"):h.accepts[\"*\"]);for(p in h.headers){C.setRequestHeader(p,h.headers[p])}if(h.beforeSend&&(h.beforeSend.call(g,C,h)===!1||c)){return C.abort()}if(T=\"abo");
        out.print("rt\",y.add(h.complete),C.done(h.success),C.fail(h.error),i=ie(Yt,h,n,C)){if(C.readyState=1,f&&m.trigger(\"ajaxSend\",[C,h]),c){return C}h.async&&h.timeout>0&&(u=e.setTimeout(function(){C.abort(");
        out.print("\"timeout\")},h.timeout));try{c=!1,i.send(b,r)}catch(S){if(c){throw S}r(-1,S)}}else{r(-1,\"No Transport\")}return C},getJSON:function(e,t,n){return Ee.get(e,t,n,\"json\")},getScript:function(e,t){");
        out.print("return Ee.get(e,void 0,t,\"script\")}}),Ee.each([\"get\",\"post\"],function(e,t){Ee[t]=function(e,n,r,i){return be(n)&&(i=i||r,r=n,n=void 0),Ee.ajax(Ee.extend({url:e,type:t,dataType:i,data:n,succe");
        out.print("ss:r},Ee.isPlainObject(e)&&e))}}),Ee.ajaxPrefilter(function(e){var t;for(t in e.headers){\"content-type\"===t.toLowerCase()&&(e.contentType=e.headers[t]||\"\")}}),Ee._evalUrl=function(e,t,n){ret");
        out.print("urn Ee.ajax({url:e,type:\"GET\",dataType:\"script\",cache:!0,async:!1,global:!1,converters:{\"text script\":function(){}},dataFilter:function(e){Ee.globalEval(e,t,n)}})},Ee.fn.extend({wrapAll:func");
        out.print("tion(e){var t;return this[0]&&(be(e)&&(e=e.call(this[0])),t=Ee(e,this[0].ownerDocument).eq(0).clone(!0),this[0].parentNode&&t.insertBefore(this[0]),t.map(function(){for(var e=this;e.firstEle");
        out.print("mentChild;){e=e.firstElementChild}return e}).append(this)),this},wrapInner:function(e){return be(e)?this.each(function(t){Ee(this).wrapInner(e.call(this,t))}):this.each(function(){var t=Ee(t");
        out.print("his),n=t.contents();n.length?n.wrapAll(e):t.append(e)})},wrap:function(e){var t=be(e);return this.each(function(n){Ee(this).wrapAll(t?e.call(this,n):e)})},unwrap:function(e){return this.pare");
        out.print("nt(e).not(\"body\").each(function(){Ee(this).replaceWith(this.childNodes)}),this}}),Ee.expr.pseudos.hidden=function(e){return !Ee.expr.pseudos.visible(e)},Ee.expr.pseudos.visible=function(e){r");
        out.print("eturn !!(e.offsetWidth||e.offsetHeight||e.getClientRects().length)},Ee.ajaxSettings.xhr=function(){try{return new e.XMLHttpRequest}catch(t){}};var Kt={0:200,1223:204},Zt=Ee.ajaxSettings.xhr(");
        out.print(");xe.cors=!!Zt&&\"withCredentials\" in Zt,xe.ajax=Zt=!!Zt,Ee.ajaxTransport(function(t){var n,r;return xe.cors||Zt&&!t.crossDomain?{send:function(i,o){var a,s=t.xhr();if(s.open(t.type,t.url,t.a");
        out.print("sync,t.username,t.password),t.xhrFields){for(a in t.xhrFields){s[a]=t.xhrFields[a]}}t.mimeType&&s.overrideMimeType&&s.overrideMimeType(t.mimeType),t.crossDomain||i[\"X-Requested-With\"]||(i[\"X");
        out.print("-Requested-With\"]=\"XMLHttpRequest\");for(a in i){s.setRequestHeader(a,i[a])}n=function(e){return function(){n&&(n=r=s.onload=s.onerror=s.onabort=s.ontimeout=s.onreadystatechange=null,\"abort\"=");
        out.print("==e?s.abort():\"error\"===e?\"number\"!=typeof s.status?o(0,\"error\"):o(s.status,s.statusText):o(Kt[s.status]||s.status,s.statusText,\"text\"!==(s.responseType||\"text\")||\"string\"!=typeof s.response");
        out.print("Text?{binary:s.response}:{text:s.responseText},s.getAllResponseHeaders()))}},s.onload=n(),r=s.onerror=s.ontimeout=n(\"error\"),void 0!==s.onabort?s.onabort=r:s.onreadystatechange=function(){4=");
        out.print("==s.readyState&&e.setTimeout(function(){n&&r()})},n=n(\"abort\");try{s.send(t.hasContent&&t.data||null)}catch(u){if(n){throw u}}},abort:function(){n&&n()}}:void 0}),Ee.ajaxPrefilter(function(e");
        out.print("){e.crossDomain&&(e.contents.script=!1)}),Ee.ajaxSetup({accepts:{script:\"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript\"},contents:{script:/\\b(?:ja");
        out.print("va|ecma)script\\b/},converters:{\"text script\":function(e){return Ee.globalEval(e),e}}}),Ee.ajaxPrefilter(\"script\",function(e){void 0===e.cache&&(e.cache=!1),e.crossDomain&&(e.type=\"GET\")}),Ee");
        out.print(".ajaxTransport(\"script\",function(e){if(e.crossDomain||e.scriptAttrs){var t,n;return{send:function(r,i){t=Ee(\"<script>\").attr(e.scriptAttrs||{}).prop({charset:e.scriptCharset,src:e.url}).on(\"");
        out.print("load error\",n=function(e){t.remove(),n=null,e&&i(\"error\"===e.type?404:200,e.type)}),Te.head.appendChild(t[0])},abort:function(){n&&n()}}}});var en=[],tn=/(=)\\?(?=&|$)|\\?\\?/;Ee.ajaxSetup({jso");
        out.print("np:\"callback\",jsonpCallback:function(){var e=en.pop()||Ee.expando+\"_\"+Pt.guid++;return this[e]=!0,e}}),Ee.ajaxPrefilter(\"json jsonp\",function(t,n,r){var i,o,a,s=t.jsonp!==!1&&(tn.test(t.url)");
        out.print("?\"url\":\"string\"==typeof t.data&&0===(t.contentType||\"\").indexOf(\"application/x-www-form-urlencoded\")&&tn.test(t.data)&&\"data\");return s||\"jsonp\"===t.dataTypes[0]?(i=t.jsonpCallback=be(t.json");
        out.print("pCallback)?t.jsonpCallback():t.jsonpCallback,s?t[s]=t[s].replace(tn,\"$1\"+i):t.jsonp!==!1&&(t.url+=(Rt.test(t.url)?\"&\":\"?\")+t.jsonp+\"=\"+i),t.converters[\"script json\"]=function(){return a||Ee.");
        out.print("error(i+\" was not called\"),a[0]},t.dataTypes[0]=\"json\",o=e[i],e[i]=function(){a=arguments},r.always(function(){void 0===o?Ee(e).removeProp(i):e[i]=o,t[i]&&(t.jsonpCallback=n.jsonpCallback,en");
        out.print(".push(i)),a&&be(o)&&o(a[0]),a=o=void 0}),\"script\"):void 0}),xe.createHTMLDocument=function(){var e=Te.implementation.createHTMLDocument(\"\").body;return e.innerHTML=\"<form></form><form></form");
        out.print(">\",2===e.childNodes.length}(),Ee.parseHTML=function(e,t,n){if(\"string\"!=typeof e){return[]}\"boolean\"==typeof t&&(n=t,t=!1);var r,i,o;return t||(xe.createHTMLDocument?(t=Te.implementation.cre");
        out.print("ateHTMLDocument(\"\"),r=t.createElement(\"base\"),r.href=Te.location.href,t.head.appendChild(r)):t=Te),i=De.exec(e),o=!n&&[],i?[t.createElement(i[1])]:(i=C([e],t,o),o&&o.length&&Ee(o).remove(),E");
        out.print("e.merge([],i.childNodes))},Ee.fn.load=function(e,t,n){var r,i,o,a=this,s=e.indexOf(\" \");return s>-1&&(r=Z(e.slice(s)),e=e.slice(0,s)),be(t)?(n=t,t=void 0):t&&\"object\"==typeof t&&(i=\"POST\"),a");
        out.print(".length>0&&Ee.ajax({url:e,type:i||\"GET\",dataType:\"html\",data:t}).done(function(e){o=arguments,a.html(r?Ee(\"<div>\").append(Ee.parseHTML(e)).find(r):e)}).always(n&&function(e,t){a.each(functio");
        out.print("n(){n.apply(this,o||[e.responseText,t,e])})}),this},Ee.expr.pseudos.animated=function(e){return Ee.grep(Ee.timers,function(t){return e===t.elem}).length},Ee.offset={setOffset:function(e,t,n)");
        out.print("{var r,i,o,a,s,u,l,c=Ee.css(e,\"position\"),f=Ee(e),p={};\"static\"===c&&(e.style.position=\"relative\"),s=f.offset(),o=Ee.css(e,\"top\"),u=Ee.css(e,\"left\"),l=(\"absolute\"===c||\"fixed\"===c)&&(o+u).in");
        out.print("dexOf(\"auto\")>-1,l?(r=f.position(),a=r.top,i=r.left):(a=parseFloat(o)||0,i=parseFloat(u)||0),be(t)&&(t=t.call(e,n,Ee.extend({},s))),null!=t.top&&(p.top=t.top-s.top+a),null!=t.left&&(p.left=t");
        out.print(".left-s.left+i),\"using\" in t?t.using.call(e,p):f.css(p)}},Ee.fn.extend({offset:function(e){if(arguments.length){return void 0===e?this:this.each(function(t){Ee.offset.setOffset(this,e,t)})}v");
        out.print("ar t,n,r=this[0];if(r){return r.getClientRects().length?(t=r.getBoundingClientRect(),n=r.ownerDocument.defaultView,{top:t.top+n.pageYOffset,left:t.left+n.pageXOffset}):{top:0,left:0}}},posit");
        out.print("ion:function(){if(this[0]){var e,t,n,r=this[0],i={top:0,left:0};if(\"fixed\"===Ee.css(r,\"position\")){t=r.getBoundingClientRect()}else{for(t=this.offset(),n=r.ownerDocument,e=r.offsetParent||n.");
        out.print("documentElement;e&&(e===n.body||e===n.documentElement)&&\"static\"===Ee.css(e,\"position\");){e=e.parentNode}e&&e!==r&&1===e.nodeType&&(i=Ee(e).offset(),i.top+=Ee.css(e,\"borderTopWidth\",!0),i.le");
        out.print("ft+=Ee.css(e,\"borderLeftWidth\",!0))}return{top:t.top-i.top-Ee.css(r,\"marginTop\",!0),left:t.left-i.left-Ee.css(r,\"marginLeft\",!0)}}},offsetParent:function(){return this.map(function(){for(var");
        out.print(" e=this.offsetParent;e&&\"static\"===Ee.css(e,\"position\");){e=e.offsetParent}return e||Qe})}}),Ee.each({scrollLeft:\"pageXOffset\",scrollTop:\"pageYOffset\"},function(e,t){var n=\"pageYOffset\"===t;");
        out.print("Ee.fn[e]=function(r){return We(this,function(e,r,i){var o;return we(e)?o=e:9===e.nodeType&&(o=e.defaultView),void 0===i?o?o[t]:e[r]:void (o?o.scrollTo(n?o.pageXOffset:i,n?i:o.pageYOffset):e[");
        out.print("r]=i)},e,r,arguments.length)}}),Ee.each([\"top\",\"left\"],function(e,t){Ee.cssHooks[t]=I(xe.pixelPosition,function(e,n){return n?(n=M(e,t),ct.test(n)?Ee(e).position()[t]+\"px\":n):void 0})}),Ee.e");
        out.print("ach({Height:\"height\",Width:\"width\"},function(e,t){Ee.each({padding:\"inner\"+e,content:t,\"\":\"outer\"+e},function(n,r){Ee.fn[r]=function(i,o){var a=arguments.length&&(n||\"boolean\"!=typeof i),s=n");
        out.print("||(i===!0||o===!0?\"margin\":\"border\");return We(this,function(t,n,i){var o;return we(t)?0===r.indexOf(\"outer\")?t[\"inner\"+e]:t.document.documentElement[\"client\"+e]:9===t.nodeType?(o=t.document");
        out.print("Element,Math.max(t.body[\"scroll\"+e],o[\"scroll\"+e],t.body[\"offset\"+e],o[\"offset\"+e],o[\"client\"+e])):void 0===i?Ee.css(t,n,s):Ee.style(t,n,i,s)},t,a?i:void 0,a)}})}),Ee.each([\"ajaxStart\",\"ajax");
        out.print("Stop\",\"ajaxComplete\",\"ajaxError\",\"ajaxSuccess\",\"ajaxSend\"],function(e,t){Ee.fn[t]=function(e){return this.on(t,e)}}),Ee.fn.extend({bind:function(e,t,n){return this.on(e,null,t,n)},unbind:fun");
        out.print("ction(e,t){return this.off(e,null,t)},delegate:function(e,t,n,r){return this.on(t,e,n,r)},undelegate:function(e,t,n){return 1===arguments.length?this.off(e,\"**\"):this.off(t,e||\"**\",n)},hover");
        out.print(":function(e,t){return this.mouseenter(e).mouseleave(t||e)}}),Ee.each(\"blur focus focusin focusout resize scroll click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouse");
        out.print("leave change select submit keydown keypress keyup contextmenu\".split(\" \"),function(e,t){Ee.fn[t]=function(e,n){return arguments.length>0?this.on(t,null,e,n):this.trigger(t)}});var nn=/^[\\s\\u");
        out.print("FEFF\\xA0]+|([^\\s\\uFEFF\\xA0])[\\s\\uFEFF\\xA0]+$/g;Ee.proxy=function(e,t){var n,r,i;return\"string\"==typeof t&&(n=e[t],t=e,e=n),be(e)?(r=ce.call(arguments,2),i=function(){return e.apply(t||this,r");
        out.print(".concat(ce.call(arguments)))},i.guid=e.guid=e.guid||Ee.guid++,i):void 0},Ee.holdReady=function(e){e?Ee.readyWait++:Ee.ready(!0)},Ee.isArray=Array.isArray,Ee.parseJSON=JSON.parse,Ee.nodeName=");
        out.print("o,Ee.isFunction=be,Ee.isWindow=we,Ee.camelCase=h,Ee.type=r,Ee.now=Date.now,Ee.isNumeric=function(e){var t=Ee.type(e);return(\"number\"===t||\"string\"===t)&&!isNaN(e-parseFloat(e))},Ee.trim=func");
        out.print("tion(e){return null==e?\"\":(e+\"\").replace(nn,\"$1\")},\"function\"==typeof define&&define.amd&&define(\"jquery\",[],function(){return Ee});var rn=e.jQuery,on=e.$;return Ee.noConflict=function(t){re");
        out.print("turn e.$===Ee&&(e.$=on),t&&e.jQuery===Ee&&(e.jQuery=rn),Ee},void 0===t&&(e.jQuery=e.$=Ee),Ee});");
        out.print("</script>");
    }

    private void write(ServletOutputStream out, String str) throws IOException {
        BUFFER_CACHE = str.getBytes(CHARSET_OUT);
        this.write(out, BUFFER_CACHE);
        out.flush();
    }

    private void write(ServletOutputStream out, InputStream inputStream) throws IOException {
        int length = -1;
        while ((length = inputStream.read(BUFFER_CACHE)) > 0) {
            out.write(BUFFER_CACHE);
        }
        out.flush();
    }

    private void write(ServletOutputStream out, byte[] data) throws IOException {
        out.write(data);
    }
}
