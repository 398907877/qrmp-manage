package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * excel导入结果
 * User: Saintcy
 * Date: 2015/3/15
 * Time: 19:47
 */
@Data
public class ExcelImportResult implements Serializable {
    private int successCount;
    private int failCount;
    private int repeatCount;
    private int totalCount;
}
