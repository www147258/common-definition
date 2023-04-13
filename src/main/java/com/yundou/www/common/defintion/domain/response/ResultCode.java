package com.yundou.www.common.defintion.domain.response;

public enum ResultCode {

    /**
     * 操作成功
     */
    SUCCESS(HttpStatus.OK.value(), "操作成功"),

    /***
     * 业务异常
     */
    BIZ_EXCEPTION(422, "业务异常"),

    /**
     * 未知异常，完全无法预料到的异常才能使用该错误代码，
     * 一旦发现这种异常，必须马上着手处理，当未知异常被正常捕获处理后，就不会用到这个错误代码了，
     * 所以这个错误代码相当于是兼容错误协议用的
     */
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常"),

    /***
     * 认证异常(未登入)
     */
    AUTHENTICATION_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED"),


    /***
     * 没有权限
     */
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN.value(), "Forbidden"),

    /**
     * 参数验证失败时通用的错误代码，因为参数实在是太多了，
     * 如果算作业务异常则编码量很大，故收归为通用错误代码
     * <p>
     * 传递具体的出错域
     */
    PARAM_INVALID(HttpStatus.BAD_REQUEST.value(), "Param-Invalid"),

    /**
     * 路由未找到，不是业务上的实体未找到，而是路由未找到
     */
    PATH_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Path-Not-Found"),

    /**
     * 路由找到了，但是HTTP Method不受支持
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method-Not-Allowed"),

    /**
     * 远程提交了一个不受支持的content-type，例如提供了一个：
     * Content-Type: application/vnd.messagepack; charset=utf-8
     * 这个时候没办法正常解析
     * 或者要求必须@RequestBody (application/json)，但是提供了一个application/x-www-form-urlencoded
     */
    MEDIA_TYPE_UNSUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Media-Type-Unsupported"),

    /**
     * 远程要求本端返回一个不受支持的Mime类型，例如本服务器暂时只支持application/json，
     * 但是有一个新的服务器要求返回application/vnd.protobuf，此时是无法进行正常返回的
     */
    MEDIA_TYPE_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE.value(), "Not-Acceptable"),

    /**
     * 请求抵达时，Spring从Spring Beans中取出转换器或者合适的编辑器，
     * 此时若找不到或多次命中，无法定位到唯一Bean，就会抛出此异常
     */
    CONVERSION_UNSUPPORTED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Conversion-Unsupported"),

    /**
     * HTTP的信息无法被正确读取，可能并不是HTTP协议，或者使用了错误的流输入
     */
    MESSAGE_UNREADABLE(HttpStatus.BAD_REQUEST.value(), "Message-Unreadable"),

    /**
     * HTTP的信息无法被正确写出，极其严重的问题，很可能是输出的数据类成环导致的
     */
    MESSAGE_UNWRITABLE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Message-Unwritable"),

    /**
     * 对于一个有时间限制的异步请求，未能及时给出响应，此时视为服务不可用，
     * 并无法知道重试时间，认为是服务器内部异常
     */
    ASYNC_REQUEST_TIMEOUT(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Async-Request-Timeout");


    /**
     * code编码
     */
    final private int code;
    /**
     * 中文信息描述
     */
    final private String message;


    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
