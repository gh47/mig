package com.mig.watch.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
@Alias("migSqlTblVO")
public class MigSqlTblVO {

    private int seq;
    private String tblAsis;
    private String tblTobe;
    private String colName;
    private String colDataType;
    private String sqlWhere;
    private String sqlOrder;
    private String divPagination;
    private int divOutCnt;
    private int divInCnt;
    private String useYn;
    private Date insDt;
    private String note;

    private String selectStr;
    private String insertStr;

}
