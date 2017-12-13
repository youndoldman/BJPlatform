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
    public String ACT_FW_STG_1_ASSIGN_USERS = "stage1AssignUsers";
    public String ACT_FW_STG_1_CANDI_GROUPS = "stage1CandiGroups";
    public String ACT_FW_STG_1_CANDI_USERS = "stage1CandiUsers";

    public String ACT_FW_STG_2_ASSIGN_USERS = "stage2AssignUsers";
    public String ACT_FW_STG_2_CANDI_GROUPS = "stage2CandiGroups";
    public String ACT_FW_STG_2_CANDI_USERS = "stage2CandiUsers";

    public String ACT_FW_STG_3_ASSIGN_USERS = "stage3AssignUsers";
    public String ACT_FW_STG_3_CANDI_GROUPS = "stage3CandiGroups";
    public String ACT_FW_STG_3_CANDI_USERS = "stage3CandiUsers";


    /*用户组code*/
    public String GP_CUSTOMER = "00004";
    public String GP_CUSTOMER_SERVICE = "00002";
    public String GP_DISPATCH = "00003";

}
