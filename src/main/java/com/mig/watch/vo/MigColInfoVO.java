package com.mig.watch.vo;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("migColInfoVO")
public class MigColInfoVO {

    private String colName;
    private String colDataType;

}
