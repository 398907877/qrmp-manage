package com.yanyan.data.domain.console.vo;

import lombok.Data;
import org.hyperic.sigar.FileSystemUsage;

import java.io.Serializable;

/**
 * User: Saintcy
 * Date: 2017/4/7
 * Time: 10:09
 */
@Data
public class DiskVo implements Serializable {
    private String name;
    private String type;
    private FileSystemUsage usage;
}
