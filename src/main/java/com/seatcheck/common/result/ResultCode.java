package com.seatcheck.common.result;

/**
 * @author: wjj
 * @date:
 * @time:
 * @description:
 */

/**
 * RESTful API返回json中的code定义
 */
public enum  ResultCode {
    /* 成功状态码 */
    SUCCESS(200, "成功"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),
    AUDIT_RESULT_ERROR(10005, "审核结果参数不合法"),
    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_REGISTER_EXISTED(20003, "账号或密码不存在"),
    USER_ACCOUNT_FORBIDDEN(20004, "账号已被禁用"),
    USER_NOT_EXIST(20005, "用户不存在"),
    USER_HAS_EXISTED(20006, "用户已存在"),
    USER_MESSAGE(20007, "用户数据缺失"),
    USER_COMPANY_INFOERROR(20008, "单位不存在"),
    ROLE_NOT_EXIST(20009, "角色不存在"),
    ROLE_HAS_EXISTED(20010, "角色已存在"),
    AUTHORITY_NOT_EXIST(20011, "权限不存在"),
    ROLEAUTHORITY_HAS_EXISTED(20012, "用户权限存在"),
    COMPANY_NOT_EXIST(20013, "单位不存在"),
    COMPANY_HAS_EXISTED(20014, "单位存在"),
    PARENTCOMPANY_NOT_EXIST(20015, "父单位不存在"),
    PROJECT_NOT_EXIST(20016, "项目不存在"),
    PROJECTEVALUATION_NOT_EXIST(20017, "评价指标不存在"),
    PROJECT_NOT_NEW(20018, "项目不处于新建状态，不允许更改"),
    PROJECT_WAIT_FOR_AUDIT(20019, "请勿重复提交，请等待审核完成，再提交下一阶段审核资料"),
    PROJECT_FATHER_FOR_CHOOSE_NONE(20020, "根据单位id查询，可选父项目为空"),
    PROJECTAUDITRECORD_NONE(20021, "项目所处结点的待审核审计记录不存在，请提交送审记录后再进行审核"),
    PROJECT_STATUS_AND_AUDITRESULT_ERROR(20022, "项目所处状态与审核结果冲突，请核对后再尝试"),
    AUDIT_RETURN_FILE_ERROR(20023, "请上传审计报告或退回函"),
    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "某业务出现问题"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),

    /* 数据错误：50001-599999 */
    RESULT_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),
    RESULT_DATA_UPDATE_ERROR(50004, "数据更新失败"),
    RESULT_DATA_WRITE_ERROR(50005, "数据写入失败"),
    RESULT_DATA_DELETE_ERROR(50006, "数据删除失败"),
    RESULT_PART_DATA_DELETE_ERROR(50007, "部分数据删除失败"),
    RESULE_DATA_NONE(5008, "数据不存在"),
    DELETE_EVA_ERROR(50009, "删除失败，您需要删除的评价指标中含有项目评价正在使用的评价指标,请解决冲突后再尝试删除！"),
    RESULT_DATA_CROSS(50010, "数据的值超出数据列表大小"),
    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    FILE_SAVE_ERROR(80001, "文件存储错误"),
    FILE_DELETE_ERROR(80002, "文件删除错误"),
    FILE_EXTENSIONS_ERROR(80003, "上传文件类型错误"),
    FILE_NULL_ERROR(80004, "上传文件不能为空"),
    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限");

    /*文件存储错误*/


    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }


    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
//    //校验重复的code值
//    public static void main(String[] args) {
//        String a="123456";
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String hashedPassword = passwordEncoder.encode(a);
//        System.out.println(hashedPassword);
//        ResultCode[] ApiResultCodes = ResultCode.values();
//        List<Integer> codeList = new ArrayList<Integer>();
//        for (ResultCode ApiResultCode : ApiResultCodes) {
//            if (codeList.contains(ApiResultCode.code)) {
//                System.out.println(ApiResultCode.code);
//            } else {
//                codeList.add(ApiResultCode.code());
//            }
//        }
//    }
//
//    @Override
//    public String getMessage() {
//        return message;
//    }
//
//    @Override
//    public Integer getCode() {
//        return code;
//    }}
//
