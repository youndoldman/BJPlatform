package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.donno.nj.util.Clock;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

/*销售现金日报表*/

@RestController
public class SaleCashRptController
{
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SalesRptService salesRptService;


    @Autowired
    private DepositDetailService depositDetailService;

    @Autowired
    private SaleContactsWriteOffRptService   saleContactsWriteOffRptService;

    @RequestMapping(value = "/api/Report/SaleCash", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店销售销售现金报表(现金销款、往日赊销、往日月结、气票款、今日存银行款、今日结存现金)")
    public ResponseEntity retrieve(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                    @RequestParam(value = "endTime", defaultValue = "") String endTime)
    {
        Map params = new HashMap<String,String>();

        SaleCashRpt saleCashRpt = new SaleCashRpt();

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));

            Optional<Department> departmentOptional = departmentService.findByCode(departmentCode);
            if (!departmentOptional.isPresent())
            {
                throw new ServerSideBusinessException("部门信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }

            saleCashRpt.setDepartmentCode(departmentCode);
            saleCashRpt.setDepartmentName(departmentOptional.get().getName());
        }

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        /*现金销售款*/
        Float saleCash = 0f;
        Map saleCashParams = new HashMap<String,String>();
        saleCashParams.putAll(params);
        saleCashParams.putAll(ImmutableMap.of("payType", PayType.PTCash.getIndex()));
        List<SalesRpt> salesRptList = salesRptService.retrieveSaleRpt(saleCashParams,EByType.EByPayType);
        for (SalesRpt salesRpt :salesRptList )
        {
            saleCash = saleCash + salesRpt.getSum();
        }
        saleCashRpt.setSaleCash(saleCash);


        /*气票销售款(含电子、支票、现金)*/
        Float ticketSale = 0f;
        Map ticketSaleParams = new HashMap<String,String>();
        ticketSaleParams.putAll(params);
        if (startTime.trim().length() > 0)
        {
            ticketSaleParams.putAll(ImmutableMap.of("saleStartTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            ticketSaleParams.putAll(ImmutableMap.of("saleEndTime", endTime));
        }
        List<Ticket> ticketList = ticketService.retrieve(ticketSaleParams);
        for (Ticket ticket :ticketList )
        {
            ticketSale = ticketSale + ticket.getPrice();
        }
        saleCashRpt.setTicketSaleCash(ticketSale);

        /*今日存银行款*/
        Float depositCash = 0f;
        List<DepositDetail>  depositDetails = depositDetailService.retrieve(params);
        for (DepositDetail depositDetail :depositDetails )
        {
            depositCash = depositCash + depositDetail.getAmount();
        }
        saleCashRpt.setDepositCash(depositCash);

        /*往日赊销*/
        Float accCredit = 0f;
        Map accCreditParams = new HashMap<String,String>();
        accCreditParams.putAll(params);
        accCreditParams.remove("startTime");
        accCreditParams.putAll(ImmutableMap.of("payType", PayType.PTDebtCredit.getIndex()));
        salesRptList.clear();
        salesRptList  = salesRptService.retrieveSaleRpt(accCreditParams,EByType.EByPayType);
        for (SalesRpt salesRpt :salesRptList )
        {
            accCredit = accCredit + salesRpt.getSum();
        }
        saleCashRpt.setAccCredit(accCredit);

        /*往日月结*/
        Float accMonthlyCredit = 0f;
        Map accMonthlyCreditParams = new HashMap<String,String>();
        accMonthlyCreditParams.putAll(params);
        accMonthlyCreditParams.remove("startTime");
        accMonthlyCreditParams.putAll(ImmutableMap.of("payType", PayType.PTMonthlyCredit.getIndex()));
        salesRptList.clear();
        salesRptList  = salesRptService.retrieveSaleRpt(accMonthlyCreditParams,EByType.EByPayType);
        for (SalesRpt salesRpt :salesRptList )
        {
            accMonthlyCredit = accMonthlyCredit + salesRpt.getSum();
        }
        saleCashRpt.setAccMonthlyCredit(accMonthlyCredit);

        /*今日赊销回款*/
        Float creditWriteOff = 0f;
        Map creditOffParams = new HashMap<String,String>();
        creditOffParams.putAll(params);
        creditOffParams.putAll(ImmutableMap.of("cretitType", CreditType.CTCommonCredit.getIndex()));
        List<SaleContactsRpt> saleContactsRpts = saleContactsWriteOffRptService.retrieve(creditOffParams);
        for (SaleContactsRpt saleContactsRpt :saleContactsRpts )
        {
            creditWriteOff = creditWriteOff + saleContactsRpt.getSum();
        }
        saleCashRpt.setCreditWriteOff(creditWriteOff);


        /*今日月结回款*/
        Float monthlyCreditWriteOff = 0f;
        Map monthlyCreditWriteOffParams = new HashMap<String,String>();
        monthlyCreditWriteOffParams.putAll(params);
        monthlyCreditWriteOffParams.putAll(ImmutableMap.of("cretitType", CreditType.CTMonthlyCredit.getIndex()));
        saleContactsRpts.clear();
        saleContactsRpts = saleContactsWriteOffRptService.retrieve(monthlyCreditWriteOffParams);
        for (SaleContactsRpt saleContactsRpt :saleContactsRpts )
        {
            monthlyCreditWriteOff = monthlyCreditWriteOff + saleContactsRpt.getSum();
        }
        saleCashRpt.setMontlyCreditWriteOff(monthlyCreditWriteOff);

        /*今日结存现金=现金销售+回款+气票现金款-银行存款  */
        Float surplusCash = 0f;
        surplusCash = saleCashRpt.getSaleCash() +  saleCashRpt.getCreditWriteOff() +
                saleCashRpt.getMontlyCreditWriteOff() + saleCashRpt.getTicketSaleCash() - saleCashRpt.getDepositCash();
        saleCashRpt.setSurplusCash(surplusCash);

        return ResponseEntity.ok(saleCashRpt);
    }
}
