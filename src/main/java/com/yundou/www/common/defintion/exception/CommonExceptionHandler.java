package com.yundou.www.common.defintion.exception;

import com.yundou.www.common.defintion.domain.response.ErrorData;
import com.yundou.www.common.defintion.domain.response.Result;
import com.yundou.www.common.defintion.domain.response.ResultCode;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

@RestControllerAdvice
public class CommonExceptionHandler {

    private Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @Value("${spring.application.name:application}")
    private String serverName;



    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<ErrorData> handleBindException(MethodArgumentNotValidException exception
            , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        final BindingResult result = exception.getBindingResult();
        final List<FieldError> allErrors = result.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError errorMessage : allErrors) {
            sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
        }
        log.warn("请求参数验证错误：", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        ErrorData ErrorData = buildErrorData(httpServletRequest, httpServletResponse,
                exception, ResultCode.PARAM_INVALID.getCode(), sb.toString());
        return Result.failedResult(ErrorData, ResultCode.PARAM_INVALID.getCode(), sb.toString());
    }

    /**
     * 处理请求参数转换异常
     * @param exception
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({HttpMessageConversionException.class})
    public Result<ErrorData> handleHttpMessageConversionException(HttpMessageConversionException exception
            , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        log.warn("请求参数异常：", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        ErrorData ErrorData = buildErrorData(httpServletRequest, httpServletResponse,
                exception, ResultCode.PARAM_INVALID.getCode(), exception.getMessage());
        return Result.failedResult(ErrorData, ResultCode.PARAM_INVALID.getCode(), exception.getMessage());
    }




    /***
     * 请求参数 媒体类型不支持
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Result<ErrorData> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception,
                                                                    HttpServletRequest httpServletRequest,
                                                                    HttpServletResponse httpServletResponse) {
        log.warn("媒体类型不支持", exception);
        httpServletResponse.setStatus(ResultCode.MEDIA_TYPE_UNSUPPORTED.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.MEDIA_TYPE_UNSUPPORTED.getCode(), exception.getMessage()),
                ResultCode.MEDIA_TYPE_UNSUPPORTED);
    }


    /***
     * 请求媒体不支持
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public Result<ErrorData> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException exception,
                                                                         HttpServletRequest httpServletRequest,
                                                                         HttpServletResponse httpServletResponse) {
        log.warn("请求MediaTypeNotAcceptable类型不支持", exception);
        httpServletResponse.setStatus(ResultCode.MEDIA_TYPE_NOT_ACCEPTABLE.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.MEDIA_TYPE_NOT_ACCEPTABLE.getCode(), exception.getMessage()),
                ResultCode.MEDIA_TYPE_NOT_ACCEPTABLE.getCode(), exception.getMessage());
    }


    /***
     *
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({ServletRequestBindingException.class})
    public Result<ErrorData> handleServletRequestBindingException(ServletRequestBindingException exception,
                                                                    HttpServletRequest httpServletRequest,
                                                                    HttpServletResponse httpServletResponse) {
        log.warn("传入的参数不满足要求：", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.PARAM_INVALID.getCode(), exception.getMessage()),
                ResultCode.PARAM_INVALID.getCode(), exception.getMessage());
    }


    /**
     * 请求HTTP 方法不支持
     *
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result<ErrorData> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                                            HttpServletRequest httpServletRequest,
                                                                            HttpServletResponse httpServletResponse) {
        log.warn("该路由不支持该方法:", exception);
        httpServletResponse.setStatus(ResultCode.METHOD_NOT_ALLOWED.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception
                        , ResultCode.METHOD_NOT_ALLOWED.getCode(), exception.getMessage())
                , ResultCode.METHOD_NOT_ALLOWED.getCode(), exception.getMessage());
    }


    /**
     * 参数类型转换错误
     *
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({ConversionNotSupportedException.class})
    public Result<ErrorData> handleConversionNotSupportedException(ConversionNotSupportedException exception,
                                                                     HttpServletRequest httpServletRequest,
                                                                     HttpServletResponse httpServletResponse) {
        log.warn("请求未找到合适转换器,参数类型不正确", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception
                        , ResultCode.PARAM_INVALID.getCode(), exception.getMessage())
                , ResultCode.PARAM_INVALID.getCode(), exception.getMessage());
    }

    /**
     * 方法参数类型转换错误
     *
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public Result<ErrorData> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
                                                                         HttpServletRequest httpServletRequest,
                                                                         HttpServletResponse httpServletResponse) {
        log.warn("请求未找到合适转换器,参数类型不正确", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.PARAM_INVALID.getCode(), exception.getMessage()),
                ResultCode.PARAM_INVALID.getCode(), exception.getMessage());
    }

    /**
     * 方法参数类型转换错误
     *
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({TypeMismatchException.class})
    public Result<ErrorData> handleTypeMismatchException(TypeMismatchException exception,
                                                           HttpServletRequest httpServletRequest,
                                                           HttpServletResponse httpServletResponse) {
        log.warn("参数类型转换出错", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.PARAM_INVALID.getCode(), exception.getMessage()),
                ResultCode.PARAM_INVALID.getCode(), exception.getMessage());
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result<ErrorData> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                                                     HttpServletRequest httpServletRequest,
                                                                     HttpServletResponse httpServletResponse) {
        log.warn(" HTTP的信息无法被正确读取，可能并不是HTTP协议，或者使用了错误的流输入", exception);
        httpServletResponse.setStatus(ResultCode.MESSAGE_UNREADABLE.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.MESSAGE_UNREADABLE.getCode(), ResultCode.MESSAGE_UNREADABLE.getMessage())
                , ResultCode.MESSAGE_UNREADABLE);
    }

    @ExceptionHandler({HttpMessageNotWritableException.class})
    public Result<ErrorData> handleHttpMessageNotWritableException(HttpMessageNotWritableException exception,
                                                                     HttpServletRequest httpServletRequest,
                                                                     HttpServletResponse httpServletResponse) {
        log.error(" HTTP的信息无法被正确读取，可能并不是HTTP协议，或者使用了错误的流输入", exception);
        httpServletResponse.setStatus(ResultCode.MESSAGE_UNWRITABLE.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception
                        , ResultCode.MESSAGE_UNWRITABLE.getCode(), ResultCode.MESSAGE_UNWRITABLE.getMessage())
                , ResultCode.MESSAGE_UNWRITABLE);
    }

//    /****
//     * 参数校验异常
//     * @param exception
//     * @param httpServletResponse
//     * @return
//     */
//    @ExceptionHandler({ConstraintViolationException.class})
//    public Result<ErrorData> handleConstraintViolationException(ConstraintViolationException exception,
//                                                                  HttpServletRequest httpServletRequest,
//                                                                  HttpServletResponse httpServletResponse) {
//        log.warn("参数校验异常", exception);
//        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
//        String msg = getViolationsMessage(exception.getConstraintViolations());
//        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
//                        ResultCode.PARAM_INVALID.getCode(), msg),
//                ResultCode.PARAM_INVALID.getCode(),
//                msg);
//    }

    /****
     * 参数校验异常
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({BindException.class})
    public Result<ErrorData> handleBindException(BindException exception,
                                                   HttpServletRequest httpServletRequest,
                                                   HttpServletResponse httpServletResponse) {
        log.warn("参数校验异常", exception);
        httpServletResponse.setStatus(ResultCode.PARAM_INVALID.getCode());
        String msg = exception.getMessage();
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.PARAM_INVALID.getCode(), msg),
                ResultCode.PARAM_INVALID.getCode(),
                msg);
    }


//    /***
//     * 获取校验异常信息
//     * @param constraintViolations
//     * @return
//     */
//    private String getViolationsMessage(Set<ConstraintViolation<?>> constraintViolations) {
//        return constraintViolations.stream()
//                .map(violation -> violation.getPropertyPath() + ":" + violation.getMessage())
//                .collect(Collectors.joining(","));
//    }


    /**
     * 未找到资源 404 你懂的
     *
     * @param exception
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public Result<ErrorData> handleNoHandlerFoundException(NoHandlerFoundException exception,
                                                             HttpServletRequest httpServletRequest,
                                                             HttpServletResponse httpServletResponse) {

        log.warn("请求路由未找到", exception);
        httpServletResponse.setStatus(ResultCode.PATH_NOT_FOUND.getCode());

        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.PATH_NOT_FOUND.getCode(), ResultCode.PATH_NOT_FOUND.getMessage()),
                ResultCode.PATH_NOT_FOUND);
    }


//    /***
//     * 调用远程服务出现未知http转态不正常异常
//     * @param exception
//     * @param httpServletRequest
//     * @param httpServletResponse
//     * @return
//     */
//    @ExceptionHandler({RemoteUnknownException.class})
//    public Result<ErrorData> handleRemoteUnknownException(RemoteUnknownException exception,
//                                                            HttpServletRequest httpServletRequest,
//                                                            HttpServletResponse httpServletResponse) {
//        log.error("调用远程服务出现未知异常,body: {},:", exception.getBody(), exception);
//        httpServletResponse.setStatus(ResultCode.UNKNOWN.getCode());
//        String className = exception.getClass().getName();
//        ErrorData ErrorData = new ErrorData();
//        ErrorData.setError_name(exception.getMessage());
//        ErrorData.setTrace_id(TraceContext.traceId());
//        return Result.failedResult(ErrorData, ResultCode.UNKNOWN);
//    }

    /**
     * 调用远程获取了有效的http 状态响应,且解析出正常的 ErrorData 对象
     *
     * @param exception
     * @param httpServletResponse
     * @return
     */
    @ExceptionHandler({RemoteBadRequestException.class, RemoteRuntimeException.class})
    public Result<ErrorData> handleRemoteException(RuntimeException exception, HttpServletResponse httpServletResponse) {
        ErrorData ErrorData = new ErrorData();
        if (exception instanceof RemoteBadRequestException) {
            RemoteBadRequestException remoteBadRequestException = (RemoteBadRequestException) exception;
            log.warn("远程自定义业务异常 {},:", remoteBadRequestException.getErrorData(), exception);
            httpServletResponse.setStatus(remoteBadRequestException.getHttpStatus().value());
            ErrorData = remoteBadRequestException.getErrorData();
            return Result.failedResult(ErrorData, getCodeValue(ErrorData.getErrorCode()), ErrorData.getErrorMessage());
        }
        if (exception instanceof RemoteRuntimeException) {
            RemoteRuntimeException remoteBadRequestException = (RemoteRuntimeException) exception;
            log.error("远程调用远程服务出现500异常 {} :", remoteBadRequestException.getErrorData(), exception);
            httpServletResponse.setStatus(remoteBadRequestException.getHttpStatus().value());
            ErrorData = remoteBadRequestException.getErrorData();
            return Result.failedResult(ErrorData, ResultCode.UNKNOWN);
        }
        log.error("出现非远程的异常", exception);
        return Result.failedResult(ErrorData, ResultCode.UNKNOWN);
    }

    /**
     * 获取错误码值
     *
     * @param errorCode 错误码
     * @return
     */
    private Integer getCodeValue(String errorCode) {
        try {
            return Integer.parseInt(errorCode);
        } catch (Exception e) {
            // ingen
            return 500;
        }
    }


    @ExceptionHandler(Exception.class)
    public Result handleException(Exception exception, HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse) {

        if (exception instanceof LocalBusinessException) {
            log.warn("本地服务出现的业务异常：", exception);
            httpServletResponse.setStatus(ResultCode.BIZ_EXCEPTION.getCode());
            ErrorData ErrorData = buildErrorData(httpServletRequest, httpServletResponse, exception,
                    ((LocalBusinessException) exception).getBusinessErrorCode(), ((LocalBusinessException) exception).getBusinessErrorMessage());
            return Result.failedResult(ErrorData, ((LocalBusinessException) exception).getBusinessErrorCode(), ((LocalBusinessException) exception).getBusinessErrorMessage());
        }

        if(SecurityExceptionHelper.AUTHENTICATION_EXCEPTION.isInstanceOf(exception)){
            httpServletResponse.setStatus(ResultCode.AUTHENTICATION_EXCEPTION.getCode());
            return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                            ResultCode.AUTHENTICATION_EXCEPTION.getCode(), exception.getMessage()),
                    ResultCode.AUTHENTICATION_EXCEPTION);
        }

        if(SecurityExceptionHelper.ACCESSDENIED_EXCEPTION.isInstanceOf(exception)){
            httpServletResponse.setStatus(ResultCode.ACCESS_DENIED_EXCEPTION.getCode());
            return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                            ResultCode.ACCESS_DENIED_EXCEPTION.getCode(), exception.getMessage()),
                    ResultCode.ACCESS_DENIED_EXCEPTION);
        }
        log.error("本地服务出现未知系统异常:", exception);
        httpServletResponse.setStatus(ResultCode.UNKNOWN.getCode());
        return Result.failedResult(buildErrorData(httpServletRequest, httpServletResponse, exception,
                        ResultCode.UNKNOWN.getCode(), exception.getMessage()),
                ResultCode.UNKNOWN);
    }

    /**
     * 构造异常信息用于rpc处理
     *
     * @param exception
     * @param response
     * @param errorCode
     * @return
     */
    public ErrorData buildErrorData(HttpServletRequest request, HttpServletResponse response, Exception exception,
                                    Serializable errorCode, String message) {
        ErrorData ErrorData = new ErrorData();
        ErrorData.setTraceId(TraceContext.traceId());
        ErrorData.setErrorClassName(exception.getClass().getName());
        ErrorData.setErrorCode(String.valueOf(errorCode));
        ErrorData.setErrorMessage(message);
        if (request != null) {
            ErrorData.setErrorPath(request.getRequestURI());
        }
        ErrorData.setServiceName(getServerName());
        return ErrorData;
    }


    public  enum  SecurityExceptionHelper{

        AUTHENTICATION_EXCEPTION(401,"org.springframework.security.core.AuthenticationException"),
        ACCESSDENIED_EXCEPTION(403,"org.springframework.security.access.AccessDeniedException")
        ;


        private final int httpStatus;
        private final String clazz;
        private Class<?> clazzObject;

        SecurityExceptionHelper(int httpStatus, String clazz) {
            this.httpStatus = httpStatus;
            this.clazz = clazz;
            try {
                clazzObject = Class.forName(clazz);
            } catch (Exception e) {
                //ignore
            }
        }

        private  boolean isInstanceOf(Object object){
            return clazzObject != null && clazzObject.isInstance(object);
        }
    }


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }



}
