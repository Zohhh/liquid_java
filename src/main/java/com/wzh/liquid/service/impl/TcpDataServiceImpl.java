package com.wzh.liquid.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzh.liquid.entity.TcpData;
import com.wzh.liquid.mapper.TcpDataMapper;
import com.wzh.liquid.service.TcpDataService;
import org.springframework.stereotype.Service;

/**
 * @author WZH
 * @Description
 * @date 2022/1/20 - 9:16
 **/
@Service
public class TcpDataServiceImpl extends ServiceImpl<TcpDataMapper, TcpData> implements TcpDataService {
}
