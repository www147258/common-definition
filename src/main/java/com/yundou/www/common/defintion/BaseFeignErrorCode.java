package com.yundou.www.common.defintion;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class BaseFeignErrorCode implements ErrorDecoder {

    private Logger logger = LoggerFactory.getLogger(BaseFeignErrorCode.class);

    @Override
    public Exception decode(String s, Response response) {

        int status = response.status();
        logger.info("status :{} ", status);

        String responseStr;

        try {
            InputStream inputStream = response.body().asInputStream();
            int available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes);

            responseStr = new String(bytes, Charset.defaultCharset());


        } catch (IOException e) {
            logger.error("读取远程服务响应错误：", e);
            return e;
        }

        logger.info("responseStr :{} ", responseStr);
        //后段一般要求json 格式传输

        return null;
    }
}
