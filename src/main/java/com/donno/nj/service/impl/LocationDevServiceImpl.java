package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.LocationDeviceDao;
import com.donno.nj.domain.LocationDevice;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.LocatonDevService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class LocationDevServiceImpl implements LocatonDevService
{

    @Autowired
    private LocationDeviceDao locationDeviceDao;


    @Override
    public Optional<LocationDevice> findByNumber(String number)
    {
        return Optional.fromNullable(locationDeviceDao.findByNumber(number));
    }

    @Override
    @OperationLog(desc = "查询定位终端信息")
    public List<LocationDevice> retrieve(Map params)
    {
        List<LocationDevice> locationDevices = locationDeviceDao.getList(params);
        return locationDevices;
    }

    @Override
    @OperationLog(desc = "查询定位终端数量")
    public Integer count(Map params) {
        return locationDeviceDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建定位终端信息")
    public void create(LocationDevice locationDevice)
    {
        /*参数校验*/
        if (locationDevice == null || locationDevice.getNumber() == null  )
        {
            throw new ServerSideBusinessException("定位终端信息不全，请补充定位终端信息！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*定位终端是否已经存在*/
        if (findByNumber(locationDevice.getNumber()).isPresent())
        {
            throw new ServerSideBusinessException("定位终端信息已经存在！", HttpStatus.CONFLICT);
        }

        locationDeviceDao.insert(locationDevice);
    }



    @Override
    @OperationLog(desc = "修改商品信息")
    public void update(String number, LocationDevice newLocationDevice)
    {
//        /*参数校验*/
//        if (code == null || code.trim().length() == 0 || newGoods == null)
//        {
//            throw new ServerSideBusinessException("商品信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*商品是否存在*/
//        Goods goods = findByCode(code).get();
//        if(goods == null)
//        {
//            throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_FOUND);
//        }
//        else
//        {
//            newGoods.setId(goods.getId());
//        }
//
//        /*目标code*/
//        if (newGoods.getCode() != null && newGoods.getCode().trim().length() > 0 )
//        {
//            if (code.equals(newGoods.getCode()))
//            {
//                newGoods.setCode(null);
//            }
//            else
//            {
//                /*目标代码是否存在*/
//                if (findByCode(newGoods.getCode()).isPresent())
//                {
//                    throw new ServerSideBusinessException("商品信息已经存在！", HttpStatus.CONFLICT);
//                }
//            }
//        }
//        else
//        {
//            newGoods.setCode(null);
//        }
//
//        /*商品类型*/
//        if (newGoods.getGoodsType() != null && newGoods.getGoodsType().getCode() != null && newGoods.getGoodsType().getCode().trim().length() > 0)
//        {
//            GoodsType goodsType = goodsTypeDao.findByCode(newGoods.getGoodsType().getCode());
//            if (goodsType != null)
//            {
//                newGoods.setGoodsType(goodsType);
//            }
//            else
//            {
//                throw new ServerSideBusinessException("商品类型信息不存在！", HttpStatus.NOT_ACCEPTABLE);
//            }
//        }
//        else
//        {
//            newGoods.setGoodsType(null);
//        }
//
//        /*更新数据*/
//        goodsDao.update(newGoods);
    }

    @Override
    @OperationLog(desc = "删除商品信息")
    public void deleteById(Integer id)
    {
//        Goods goods = goodsDao.findById(id);
//        if (goods == null)
//        {
//            throw new ServerSideBusinessException("商品信息不存在！",HttpStatus.NOT_FOUND);
//        }
//
//        /*检查，如果有订单记录，则不允许删除*/
//        Map params = new HashMap<String,String>();
//        params.putAll(ImmutableMap.of("goods", goods));
//        if (orderDetailDao.count(params) > 0)
//        {
//            throw new ServerSideBusinessException("商品已有销售记录，不能删除商品信息！",HttpStatus.FORBIDDEN);
//        }
//
//        goodsDao.deleteByIdx(id);
    }
}
