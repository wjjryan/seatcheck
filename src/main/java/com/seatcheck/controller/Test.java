package com.seatcheck.controller;

import com.seatcheck.common.result.Result;
import com.seatcheck.entity.Node;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wjj
 * @date:
 * @time:
 * @description:
 */

@RestController
public class Test {

    @Resource
    ListOperations<String,Object> listOperations;

    @ApiOperation(value = "测试",httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "数据库发错误")})
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result buildAndSent(){
        return Result.success();
    }
}
