package com.wzh.liquid.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.wzh.liquid.common.lang.Result;
import com.wzh.liquid.entity.TcpData;
import com.wzh.liquid.entity.TcpData1;
import com.wzh.liquid.service.TcpData1Service;
import com.wzh.liquid.service.TcpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WZH
 * @Description
 * @date 2022/1/20 - 9:23
 **/
@RestController
public class TcpDataController {
    @Autowired
    TcpDataService tcpDataService;
    @Autowired
    TcpData1Service tcpData1Service;

    /**
     * 分页查询
     * @param currentPage
     * @return
     */
    @GetMapping("/tcpdatas")
    public Result tcpdatas(@RequestParam(defaultValue = "1") Integer currentPage){
        Page page=new Page(currentPage,8);
        IPage pageData = tcpDataService.page(page, new QueryWrapper<TcpData>().orderByDesc("time"));
        return Result.succ(pageData);
    }
    @GetMapping("/alldatas")
    public Result alldatas(){
        List<TcpData> list = tcpDataService.list();
        return Result.succ(list);
    }
    @GetMapping("/alldatas1")
    public Result alldatas1(){
        List<TcpData1> list = tcpData1Service.list();
        return Result.succ(list);
    }
    @GetMapping("/tcpdatas1")
    public Result tcpdatas1(@RequestParam(defaultValue = "1") Integer currentPage){
        Page page=new Page(currentPage,8);
        IPage pageData = tcpData1Service.page(page, new QueryWrapper<TcpData1>().orderByDesc("time"));
        return Result.succ(pageData);
    }
}
