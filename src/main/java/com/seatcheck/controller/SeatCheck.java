package com.seatcheck.controller;

import com.seatcheck.common.result.Result;
import com.seatcheck.service.SeatService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

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
public class SeatCheck {
    @Resource
    SeatService seatService;

    /**
     * @description: 生成指令接口
     */
    @ApiOperation(value = "生成并发送指令给老师",httpMethod = "POST", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "数据库发错误")})
    @RequestMapping(value = "/seats/{orederNum}", method = RequestMethod.POST)
    public Result buildAndSent(@PathVariable String orederNum,
            @RequestBody ArrayList<List<String>> list){
        return seatService.buildAndSentInstruction(list,orederNum);
    }

    /**
     * @description:座位校验接口
     */
    @ApiOperation(value = "座位校验接口",httpMethod = "PUT", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "数据库发错误")})
    @RequestMapping(value = "/seat/{orederNum}", method = RequestMethod.PUT)
    public Result checkSeat(@PathVariable String orederNum,
                               @RequestParam(value = "scaner",required = true)String scaner,
                               @RequestParam(value = "scanned",required = true)String scanned){
        return seatService.checkSeat(orederNum,scaner,scanned);
    }

    /**
     * @description:获取指令
     */
    @ApiOperation(value = "获取指令",httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "数据库发错误")})
    @RequestMapping(value = "/instruction/{orederNum}", method = RequestMethod.GET)
    public Result getMyinstruction(@PathVariable String orederNum,
                               @RequestParam(value = "studentNum",required = true)String studentNum){
        return seatService.getMyinstruction(orederNum,studentNum);
    }

    /**
     * @description:获取座位表
     */
    @ApiOperation(value = "获取座位表",httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "数据库发错误")})
    @RequestMapping(value = "/seats/{orederNum}", method = RequestMethod.GET)
    public Result buildAndSent(@PathVariable String orederNum){
        return seatService.getSeatingChart(orederNum);
    }

    /**
     * @description:座位信息更新
     */
    @ApiOperation(value = "座位信息更新",httpMethod = "PUT", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code = 500,message = "数据库发错误")})
    @RequestMapping(value = "/seats/{orederNum}", method = RequestMethod.PUT)
    public Result reBuildAndSent(@PathVariable String orederNum,
                               @RequestBody ArrayList<List<String>> list){
        return seatService.reBuildAndSentInstruction(list,orederNum);
    }

}
