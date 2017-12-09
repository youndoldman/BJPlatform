package com.donno.nj.domain;

/**
* @class ConstantValue
* @brief  常量定义
*/
public interface ServerConstantValue
{
    public String DB_ROW_UNDIFINED_CODE = "00000";  /**<*/
    public String DB_ROW_UNDIFINED_NAME = "未定义";  /**<*/


    /*流程管理*/
    public String ACT_FW_STG_ASSIGN_USERS = "stage1AssignUsers";
    public String ACT_FW_STG_CANDI_GROUPS = "stage1CandiGroups";
    public String ACT_FW_STG_CANDI_USERS = "stage1CandiUsers";

    /*用户组code*/
    public String GP_CUSTOMER_SERVICE = "00002";
    public String GP_DISPATCH = "00003";

}
