package com.donno.nj.service.impl;

import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.service.TestService;

import com.donno.nj.service.UserService;
import com.donno.nj.service.WorkFlowService;
import com.donno.nj.util.DistanceHelper;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TestServiceImpl implements TestService
{
    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Autowired
    private SettlementTypeDao settlementTypeDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerCompanyDao customerCompanyDao;

    @Autowired
    private CustomerTypeDao customerTypeDao;

    @Autowired
    private CustomerLevelDao customerLevelDao;

    @Autowired
    private CustomerSourceDao customerSourceDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private OrderDetailDao  orderDetailDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private  SysUserDao sysUserDao;

    @Override
    public void run()
    {
        try
        {
            // AddGasCylinder();

            //AddCustomer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void AddOrder(Customer customer)
    {
        Order order = new Order();
        order.setUrgent(Boolean.FALSE);
        order.setOrderStatus(OrderStatus.OSUnprocessed.getIndex());
        order.setPayStatus(PayStatus.PSUnpaid);
        order.setAccessType(AccessType.ATWeixin);
        order.setRecvLongitude(0.0);
        order.setRecvLatitude(0.0);
        order.setCustomer(customer);
        order.setRecvPhone(customer.getPhone());

        order.setRecvAddr(customer.getAddress());
        order.setRecvName(customer.getName());

        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        order.setOrderSn(dateFmt);

        order.setOriginalAmount(0f);
        order.setOrderAmount(0f);

        setPayType(order.getCustomer().getSettlementType(),order);

        Map params = new HashMap<String,String>();
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        List<Goods> goodses = goodsDao.getList(params);
        for (Goods goods :goodses)
        {
            OrderDetail orderDetailTarget = new OrderDetail();
            orderDetailTarget.setGoods(goods);
            orderDetailTarget.setQuantity(2);

            orderDetails.add(orderDetailTarget);
        }

        order.setOrderDetailList(orderDetails);

        /*插入订单总表*/
        orderDao.insert(order);

        Float dealAmount = 0f; //订单实际成交总金额
        Float originalAmount = 0f;//订单原始价格总金额
        /*插入详单表*/
        for(OrderDetail orderDetail : order.getOrderDetailList())
        {
            Float dealPrice = 0.0f;   //订单中每个商品成交单价
            Goods good = goodsDao.findByCode(orderDetail.getGoods().getCode());
            orderDetail.setGoods(good);

            dealPrice = good.getPrice()  ;

            Float originalSubtotal = good.getPrice() *  orderDetail.getQuantity();//原价小计
            originalAmount = originalAmount + originalSubtotal ; //原价总计

            Float dealSubtotal = dealPrice * orderDetail.getQuantity();//成交价格小计
            dealAmount = dealAmount + dealSubtotal; //成交价格总计

            orderDetail.setOriginalPrice(good.getPrice());
            orderDetail.setDealPrice(dealPrice);
            orderDetail.setSubtotal(dealSubtotal);
            orderDetail.setOrderIdx(order.getId());
            orderDetail.setGoods(good);

            orderDetailDao.insert(orderDetail);//插入数据库订单详情表
        }

        /*更新订单总金额*/
        Order orderUpdateAmount = new Order();
        orderUpdateAmount.setId(order.getId());
        orderUpdateAmount.setOriginalAmount(originalAmount);
        orderUpdateAmount.setOrderAmount(dealAmount);
        orderDao.update(orderUpdateAmount);

        /*启动流程*/
        createWorkFlow(order);
    }

    void  AddCustomer()
    {
        Integer targetCount = 500000;
        Map params = new HashMap<String,String>();

        List<SettlementType> settlementTypes = settlementTypeDao.getList(params);
        for (SettlementType settlementType : settlementTypes)
        {
            params.clear();
            params.putAll(ImmutableMap.of("settlementTypeCode", settlementType.getCode()));
            Integer startCount = customerDao.count(params);

            Integer count = startCount;
            while(true)
            {
                count++;
                Customer customer = new Customer();

                customer.setSettlementType(settlementType);

                String userId = String.format("test%d",count);
                customer.setUserId(userId);
                customer.setName(userId);
                customer.setNumber(userId);
                customer.setIdentity(userId);

                CustomerSource customerSource = customerSourceDao.findByCode("00000");
                customer.setCustomerSource(customerSource);

                CustomerType customerType = customerTypeDao.findByCode("00001");
                customer.setCustomerType(customerType);

                CustomerLevel customerLevel = customerLevelDao.findByCode("00001");
                customer.setCustomerLevel(customerLevel);

                CustomerCompany customerCompany = customerCompanyDao.findByCode("00001");
                customer.setCustomerCompany(customerCompany);

                customer.setPassword("111111");

                customer.setStatus(1);

                String phone = String.format("%d",count);
                customer.setPhone(phone);



                customer.setHaveCylinder(false);

                String addressDetail = String.format("蒜村新区%d栋202",count);
                CustomerAddress customerAddress = new CustomerAddress();
                customerAddress.setProvince("云南省");
                customerAddress.setCity("昆明市");
                customerAddress.setCounty("盘龙区");
                customerAddress.setDetail(addressDetail);
                customer.setAddress(customerAddress);

                Group group = groupDao.findByCode("00004");
                customer.setUserGroup(group);

                if (settlementType.getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER) )
                {
                    customerSource = customerSourceDao.findByCode("00001");
                    customer.setCustomerSource(customerSource);

                    customerType = customerTypeDao.findByCode("00002");
                    customer.setCustomerType(customerType);

                    customerLevel = customerLevelDao.findByCode("00002");
                    customer.setCustomerLevel(customerLevel);

                    customerCompany = customerCompanyDao.findByCode("00002");
                    customer.setCustomerCompany(customerCompany);

                    customer.setHaveCylinder(true);

                    addressDetail = String.format("新港中路%d号",count);
                    customerAddress.setProvince("广东省");
                    customerAddress.setCity("广州市");
                    customerAddress.setCounty("海珠区");
                    customerAddress.setDetail(addressDetail);
                    customer.setAddress(customerAddress);
                }
                else if (settlementType.getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT) )
                {
                    customerType = customerTypeDao.findByCode("00001");
                    customer.setCustomerType(customerType);

                    customerLevel = customerLevelDao.findByCode("00003");
                    customer.setCustomerLevel(customerLevel);

                    customerCompany = customerCompanyDao.findByCode("00003");
                    customer.setCustomerCompany(customerCompany);

                    addressDetail = String.format("新港中路%d号",count);
                    customerAddress.setProvince("广东省");
                    customerAddress.setCity("广州市");
                    customerAddress.setCounty("海珠区");
                    customerAddress.setDetail(addressDetail);
                    customer.setAddress(customerAddress);
                }
                else
                {
                    customerLevel = customerLevelDao.findByCode("00004");
                    customer.setCustomerLevel(customerLevel);

                    customerCompany = customerCompanyDao.findByCode("00004");
                    customer.setCustomerCompany(customerCompany);

                }



                User user = customer;
                userDao.insert(user);
                customerDao.insert(customer);

                AddOrder(customer);
                if (count - startCount > targetCount)
                {
                    break;
                }
            }
        }
    }

    void AddGasCylinder()
    {
        Integer targetCount = 500000;
        Map params = new HashMap<String,String>();
        List<GasCylinderSpec> gasCylinderSpecList = gasCylinderSpecDao.getList(params);

        for(GasCylinderSpec gasCylinderSpec :gasCylinderSpecList)
        {
            params.clear();
            params.putAll(ImmutableMap.of("specCode", gasCylinderSpec.getCode()));
            Integer startCount = gasCylinderDao.count(params);

            Integer count = startCount;
            while(true)
            {
                count++;

                GasCylinder gasCylinder = new GasCylinder();
                String number = String.format("1100%d",count);
                gasCylinder.setNumber(number);
                gasCylinder.setSpec(gasCylinderSpec);
                gasCylinder.setServiceStatus(GasCynServiceStatus.StationStock);
                gasCylinder.setLifeStatus(DeviceStatus.DevEnabled);
                gasCylinder.setLoadStatus(LoadStatus.LSHeavy);
                gasCylinder.setNextVerifyDate(new Date());
                gasCylinder.setProductionDate(new Date());
                gasCylinder.setVerifyDate(new Date());
                gasCylinder.setScrapDate(new Date());
   
                gasCylinder.setNote("test");

                gasCylinderDao.insert(gasCylinder);
                if (count - startCount > targetCount)
                {
                    break;
                }
            }

        }
    }

    public void setPayType(SettlementType settlementType,Order order)
    {
        if (settlementType == null)
        {
            throw new ServerSideBusinessException("客户数据错误，缺少结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        String code = settlementType.getCode();
        if (code == null || code.trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户数据错误，缺少结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_TICKET) )//气票用户
        {
            order.setPayType(PayType.PTTicket);

        }
        else if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT))//月结用户
        {
            order.setPayType(PayType.PTMonthlyCredit);
        }
        else  if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER) )//普通用户
        {
            //order.setPayType(PayType.PTOnLine);
            //不做处理
        }
        else
        {
            throw new ServerSideBusinessException("客户结算类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public void createWorkFlow(Order order)
    {
        /*启动流程*/
        Map<String, Object> variables = new HashMap<String, Object>();
        Group  group = groupDao.findByCode(ServerConstantValue.GP_CUSTOMER_SERVICE);

        if(group == null)
        {
            throw new ServerSideBusinessException("创建定单失败，系统用户组信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*指定可办理的组*/
        variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_GROUPS,String.valueOf(group.getId()));

         /*取系统中可推送派送员的范围参数*/
        Integer dispatchRange = 5;
        if (dispatchRange == null)
        {
            dispatchRange = 5;
        }

        /*指定可办理该流程用户,根据经纬度寻找合适的派送工*/
        Map findDispatchParams = new HashMap<String,String>();
        findDispatchParams.putAll(ImmutableMap.of("groupCode", ServerConstantValue.GP_DISPATCH));
        List<SysUser> sysUsersList = sysUserDao.getList(findDispatchParams);
        if (sysUsersList.size() > 0)
        {
            String candUser = "";
            for (SysUser sysUser:sysUsersList)
            {
                if (sysUser.getUserPosition() != null)
                {
                    Double distance = DistanceHelper.Distance(order.getRecvLatitude(),order.getRecvLongitude(),sysUser.getUserPosition().getLatitude(),sysUser.getUserPosition().getLongitude());
                    if ( distance < dispatchRange)
                    {
                        if (candUser.trim().length() > 0 )
                        {
                            candUser = candUser + ",";
                        }
                        candUser = candUser + sysUser.getUserId();
                    }
                }
            }

            variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_USERS,candUser);
        }

        workFlowService.createWorkFlow(WorkFlowTypes.GAS_ORDER_FLOW,order.getCustomer().getUserId(),variables,order.getOrderSn());

    }
}
