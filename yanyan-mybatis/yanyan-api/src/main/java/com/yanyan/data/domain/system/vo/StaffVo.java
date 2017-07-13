package com.yanyan.data.domain.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * User: Saintcy
 * Date: 2016/12/26
 * Time: 8:54
 */
@Data
public class StaffVo implements Serializable {
    private Long id;
    private String name;
    private String account;
}
