package com.cai.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cai
 * @className ServiceException
 * @description 服务异常
 * @dateTime 2023/8/10 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {

    private int code;
    private String msg;
}
