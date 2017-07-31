/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 14:12
 */
package net.lizhaoweb.spring.mvc.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <h1>模型 [枚举] - 状态码</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusCode {
    SUCCESS(10000, "请求成功。"),
    UNKNOWN(99999, "未知状态"),

    UNKNOWN_ERROR(10001, "未知错误，请联系技术客服。"),
    UNKNOWN_METHOD_NAME(10002, "未知的方法名"),
    WRONG_BUSINESS_TYPE(10003, "错误的业务类型"),
    FREQUENT_OPERATION(10004, "操作频繁"),
    MATCHED_IN_BLACKLIST(10005, "匹配到黑名单"),
    SERVER_INTERNAL_EXCEPTION(10006, "服务器内部异常"),
    KEYWORDS_WAIT_AUDIT(10007, "关键字等待审核"),
    PARAMETER_NOT_EXIST(10008, "请求数据不存在"),

    DATABASE_OPERATION_FAILED(10009, "数据库操作失败"),

    CONTAINS_ILLEGAL_KEYWORDS(10010, "含有非法关键字"),
    PORT_ILLEGAL(10012, "端口号非法"),
    IP_ILLEGAL(10013, "IP非法"),
    XML_INVALID(10014, "XML 格式不对"),
    URL_INVALID(10015, "URL 格式不正确"),
    JSON_INVALID(10016, "JSON 格式不对"),


    // 用户 1005x
    USER_NOT_EXIST(10050, "用户不存在"),
    USER_NAME_PASS_ERROR(10051, "用户名或者密码错误"),

    // 验证码 1006x
    VERIFICATION_CODE_NULL(10060, "验证码为空或者缺少此参数"),
    VERIFICATION_CODE_INVALID(10061, "验证码格式不对（4-8位数字）"),
    VERIFICATION_CODE_NOT_ALLOWED_SEND(10062, "不允许发验证码"),

    // 请求 1009x
    REQUEST_MODE_ERROR(10090, "请求方式错误"),
    REQUEST_REPEATED_SUBMISSION(10091, "重复提交"),
    REQUEST_ONLY_POST(10092, "请使用 POST"),


    // 请求头参数 101xx
    REQUEST_HEADER_CONTENT_TYPE_ERROR(10100, "请求 Content-Type 错误"),
    REQUEST_HEADER_ACCEPT_ERROR(10101, "请求 Accept 错误"),


    // 请求体参数 102xx
    PARAMETER_EMPTY(10200, "参数为空"),
    PARAMETER_INVALID(10201, "参数非法，如request parameter (key) is missing"),
    PARAMETER_VERIFICATION_FAILS(10202, "参数校验失败"),


    // 时间截 103xx
    TIMESTAMP_NULL(10300, "时间戳为空"),
    TIMESTAMP_ERROR(10301, "时间戳格式错误"),
    TIMESTAMP_EXPIRED(10302, "时间戳已过期"),
    TIMESTAMP_OUT_OF_VALID_RANGE(10303, "时间戳超出有效时间范围"),
    TIME_INVALID(10304, "时间格式不对"),
    TIME_START_LESS_END(10305, "开始时间小于结束时间"),
    TIME_START_AND_END_SAME_DAY(10306, "开始时间和结束时间必须是同一天"),


    // 签名 104xx
    SIGN_NULL(10400, "签名(sig)参数为空"),
    SIGN_ERROR(10401, "sign错误"),


    // 手机号 105xx
    MOBILE_NULL(10500, "手机号为空"),
    MOBILE_INVALID(10501, "手机格式不对"),
    MOBILE_EXIST(10502, "手机号存在"),
    MOBILE_SAME_OLD_AND_NEW(10503, "新手机号和旧手机号相同，不必修改"),


    // 开发者 106xx
    DEVELOPER_ACCOUNT_NULL(10600, "开发者为空或者没有传值"),
    DEVELOPER_ACCOUNT_NOT_EXIST(10601, "开发者余额不足"),
    DEVELOPER_ACCOUNT_BALANCE_INSUFFICIENT(10602, "开发者余额不足"),
    DEVELOPER_ACCOUNT_APPLICATION_NOT_CONTAIN(10604, "开发者不包含此应用"),
    DEVELOPER_ACCOUNT_ID_ERROR(10605, "developerId 请求错误"),
    DEVELOPER_ACCOUNT_BALANCE_FROZEN(10606, "开发者余额已被冻结"),


    // 子账号 107xx
    SUB_ACCOUNT_NULL(10700, "子账号为空或者没有传值"),
    SUB_ACCOUNT_NOT_EXIST(10701, "子账号不存在"),
    SUB_ACCOUNT_BALANCE_INSUFFICIENT(10702, "子账号余额不足"),
    SUB_ACCOUNT_NAME_EXIST(10703, "子账号名称已存在"),
    SUB_ACCOUNT_NAME_TOO_LONG(10704, "子账号名称过长"),
    SUB_ACCOUNT_NOT_BOUND_MOBILE(10705, "子账户没有绑定手机号"),
    SUB_ACCOUNT_SHUT_DOWN(10706, "子账号已被关闭"),


    // 订单 108xx
    ORDER_EXIST(10800, "订单已存在"),


//        (10029,"回调开发者服务器异常"),
//        (10030,"回调地址为空"),
//        (10090,"回调地址为空"),
//        (10050,"短信或者语音验证码错误"),
//        (10055,"应用不包含此子账号"),
//        (10031,"appId为空或者没有传值"),
//        (10061,"app不存在"),
//        (10063,"app未上线"),
//        (10070,"手机号未绑定"),
//        (10071,"通知类型已停用或者未创建"),
//        (10081,"通知计费系统失败"),
//        (10083,"充值失败"),
//        (10084,"子账号没有托管 不能进行充值"),
//        (10085,"开发者不包含子帐号"),
//        (10086,"DEMO不能进行充值"),
//        (10087,"IQ类型错误"),
//        (10091,"没有语音"),
//        (10093,"没有这个语音文件或者审核没通过"),
//        (10094,"每批发送的手机号数量不得超过100个"),
//        (10095,"未开通邮件短信功能"),
//        (10096,"邮件模板未审核通过"),
//        (10097,"邮件模板未启用"),
//        (10098,"同一手机号每天只能发送n条相同的内容"),
//        (10099,"相同的应用每天只能给同一手机号发送n条不同的内容"),
//        (10100,"短信内容不能含有关键字"),
//        (10101,"配置短信端口号失败"),
//        (10102,"一个开发者只能配置一个端口"),
//        (10103,"应用的邮件模板不存在"),
//        (10104,"相同的应用当天给同一手机号发送短信的条数小于等于n"),
//        (10105,"本开发者只能发短信给移动手机"),
//        (10110,"解析post数据失败，post数据不符合格式要求"),
//        (10037,"limit格式不对"),
//        (10038,"start格式不对"),
//        (10072,"balance格式不对（必须为大于等于0的double）"),
//        (10073,"charge格式不对（必须为大于等于0的double）"),
//        (10112,"accountSid参数为空"),
//        (10113,"短信内容和模板匹配度过低"),
//        (10114,"clientNumber参数为空"),
//        (10115,"charge参数为空"),
//        (10116,"charge格式不对，不能解析成double"),
//        (10117,"fromTime参数为空"),
//        (10118,"toTime参数为空"),
//        (10119,"fromTime参数格式不正确"),
//        (10120,"toTime参数格式不正确"),
//        (10122,"date参数为空"),
//        (10123,"date的值不在指定范围内"),
//        (10124,"没有查询到话单（所以没有生成下载地址）"),
//        (10125,"emailTemplateId参数为空"),
//        (10126,"to参数为空"),
//        (10127,"param参数为空"),
//        (10128,"templateId参数为空"),
//        (10129,"模板类型错误"),
//        (10130,"serviceType参数为空"),
//        (10131,"content参数为空"),
//        (10132,"本接口的邮件短信业务只能发送移动手机"),
//        (10134,"没有和内容匹配的模板"),
//        (10135,"应用没有属于指定类型业务并且已审核通过、已启用的模板"),
//        (10136,"开发者不能调用此接口"),
//        (10137,"没有权限自定义邮件内容"),
//        (10138,"短信没有签名不能发送"),
//        (10139,"短信签名已进入黑名单不能发送"),
//        (10140,"邮件短信发送间隔太小"),
//        (10141,"一小时内发送给单个手机次数超过限制"),
//        (10142,"一天内发送给单个手机次数超过限制"),
//        (10146,"minutes格式不对（必须为大于等于0的double）"),
//        (10147,"被叫次数超限"),
//        (10148,"主叫次数超限"),
//        (10149,"流量包大小格式错误"),
//        (10150,"找不到匹配的流量包"),
//        (10151,"该签名下的手机号码黑名单"),
//        (10152,"端口号已被关闭"),
//        (10153,"未知的手机号运营商"),
//        (10154,"开发者无权限给此号码发短信"),
//        (10155,"流量充值提交失败"),
//        (10156,"packageId为空或者没有传值"),
//        (10157,"packageId不存在"),
//        (10159,"超过每秒发送频率限制"),
//        (10160,"没有发送会员通知推广类短信权限"),
//        (10161,"短信签名没有报备"),
//        (10162,"没有发送营销短信权限"),
//        (10163,"会员营销短信内容必须包含退订"),

//        (10032,"主叫号码为空或者没有传值"),
//        (10033,"被叫号码为空或者没有传值"),
//        (10035,"主叫号码和被叫号码相同"),
//        (10052,"回拨主叫号码格式错误"),
//        (10053,"被叫号码格式错误"),
//        (10051,"显示号码与被叫号码一样，不允许呼叫"),
//        (10054,"显号格式错误"),
//        (10074,"主叫和子账户绑定的手机号不相同"),
    ;

    /**
     * 状态码
     */
    @Getter
    @NonNull
    int code;

    /**
     * 信息
     */
    @Getter
    @NonNull
    String message;

    public static StatusCode fromCode(int code) {
        for (StatusCode type : values()) {
            if (type.code == code) {
                return type;
            }
        }
//        throw new IllegalArgumentException(String.format("The code is '%s'", code));
        return UNKNOWN;
    }
}
