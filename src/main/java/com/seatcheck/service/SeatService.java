package com.seatcheck.service;

import com.seatcheck.common.result.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:wjj
 * @date: 3.15
 * @time:
 * @description:
 */

public interface SeatService {
    Result buildAndSentInstruction(ArrayList<List<String>> list,String orederNum);
    Result checkSeat(String orderNum,String scaner,String scanned);
    Result getMyinstruction(String orderNum,String studentNum);
    Result getSeatingChart(String orderNum);
    Result reBuildAndSentInstruction(ArrayList<List<String>> list,String orederNum);
}
