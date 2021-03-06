package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.*;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Iterator;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import java.util.Date;
@RestController
public class SysUserController
{


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WeiXinPayService weiXinPayService;

//    private static Timer timer;
//
//    public  SysUserController()
//    {
//        timer = new Timer();
//        timer.schedule(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                if (sysUserService != null)
//                {
//                    sysUserService.checkAlive();
//                }
//            }
//        }, 0, 20000);
//    }


    @RequestMapping(value = "/api/sysusers/FindByUserId", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity FindByUserId(@RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;

        Optional<SysUser>  sysUser = sysUserService.findBySysUserId(userId);
        if (!sysUser.isPresent())
        {
            throw new ServerSideBusinessException("用户不存在！",HttpStatus.NOT_FOUND);
        }
        else
        {
            responseEntity = ResponseEntity.ok(sysUser.get());
        }

        return responseEntity;
    }

    @RequestMapping(value = "/api/sysusers/FindCompanyPayCode", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity FindCompanyPayCode(@RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity = null;

        Optional<SysUser>  sysUser = sysUserService.findBySysUserId(userId);
        if (!sysUser.isPresent())
        {
            throw new ServerSideBusinessException("用户不存在！",HttpStatus.NOT_FOUND);
        }
        else
        {
            Department department = sysUser.get().getDepartment();
            boolean findedTarget = false;
            while(department.getParentDepartment() != null)
            {
                if (department.getCode().endsWith(ServerConstantValue.SUB_COMPANY_PAY_CODE))
                {
                    String payCode = department.getCode();
                    payCode = department.getCode().substring(0,payCode.indexOf("-"))+"-";
                    payCode = payCode + department.getCode().substring(department.getCode().indexOf(ServerConstantValue.SUB_COMPANY_PAY_CODE),department.getCode().length());

                    responseEntity = ResponseEntity.ok(payCode);
                    findedTarget = true;

                    break;
                }
                else
                {
                    department = department.getParentDepartment();
                }
            }

            if (!findedTarget)
            {
                throw new ServerSideBusinessException("未找到该用户的支付码！",HttpStatus.NOT_FOUND);
            }
        }

        return responseEntity;
    }

    @RequestMapping(value = "/api/sysusers/login", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity login(@RequestParam(value = "userId", defaultValue = "") String userId,
                                @RequestParam(value = "password", defaultValue = "") String password)
    {
        ResponseEntity responseEntity;

        Optional<User> validUser = sysUserService.findByUserIdPwd(userId,password);

        if (!validUser.isPresent())
        {
            throw new ServerSideBusinessException("用户不存在或密码错误！",HttpStatus.UNAUTHORIZED);
        }
        else
        {
            Optional<SysUser>  sysUser = sysUserService.findBySysUserId(userId);
            if (sysUser.isPresent())
            {
                AppUtil.setCurrentLoginUser(validUser.get());
                responseEntity = ResponseEntity.ok(sysUser.get());

                /*设置用户在线*/
                SysUser sysUserOnLine = new SysUser();
                sysUserOnLine.setId(sysUser.get().getId());
                sysUserOnLine.setAliveStatus(AliveStatus.ASOnline);
                sysUserOnLine.setUpdateTime(new Date());
                sysUserOnLine.setAliveUpdateTime(new Date());
                sysUserService.update(sysUserOnLine.getId(),sysUserOnLine);
            }
            else
            {
                throw new ServerSideBusinessException("非系统用户！",HttpStatus.UNAUTHORIZED);
            }
        }

        return responseEntity;
    }

//    @RequestMapping(value = "/api/sysusers/wx/login", method = RequestMethod.GET, produces = "application/json")
//    public ResponseEntity login( @RequestParam(value = "userCode", defaultValue = "") String userCode)
//            throws IOException
//    {
//        ResponseEntity responseEntity;
//
//        String wxOpenId = weiXinPayService.getOpenId(userCode);
//        if (wxOpenId == null || wxOpenId.trim().length() == 0)
//        {
//            throw new ServerSideBusinessException("OpenId获取失败！",HttpStatus.NOT_FOUND);
//        }
//        else
//        {
//            Optional<User> userOptional = sysUserService.findByWxOpenId(wxOpenId);
//
//            if (userOptional.isPresent())
//            {
//                AppUtil.setCurrentLoginUser(userOptional.get());
//                responseEntity = ResponseEntity.ok(userOptional.get());
//
//                /*设置用户在线*/
//                Optional<SysUser>  sysUser = sysUserService.findBySysUserId(userOptional.get().getUserId());
//                SysUser sysUserOnLine = new SysUser();
//                sysUserOnLine.setId(sysUser.get().getId());
//                sysUserOnLine.setAliveStatus(AliveStatus.ASOnline);
//                sysUserOnLine.setUpdateTime(new Date());
//                sysUserOnLine.setAliveUpdateTime(new Date());
//                sysUserService.update(sysUserOnLine.getId(),sysUserOnLine);
//            }
//            else
//            {
//                throw new ServerSideBusinessException("用户未绑定微信！",HttpStatus.UNAUTHORIZED);
//            }
//        }
//
//        return responseEntity;
//    }


    @RequestMapping(value = "/api/sysusers/KeepAlive/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity keepAlive(@PathVariable("userId") String userId)
    {
        /*设置用户在线*/
        Optional<SysUser> userOptional = sysUserService.findBySysUserId(userId);
        if (userOptional.isPresent())
        {
            userOptional.get().setAliveStatus(AliveStatus.ASOnline);
            userOptional.get().setUpdateTime(new Date());
            userOptional.get().setAliveUpdateTime(new Date());
            sysUserService.update(userOptional.get().getId(),userOptional.get());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/api/sysusers/logout/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity logout(@PathVariable("userId") String userId,HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        AppUtil.clearCurrentLoginUser();

        /*设置用户离线*/
        Optional<SysUser> userOptional = sysUserService.findBySysUserId(userId);
        if (userOptional.isPresent())
        {
            userOptional.get().setAliveStatus(AliveStatus.ASOffline);
            userOptional.get().setUpdateTime(new Date());
            sysUserService.update(userOptional.get().getId(),userOptional.get());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @RequestMapping(value = "/api/sysusers", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取客户列表")
    public ResponseEntity retrieve(@RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "userName", defaultValue = "") String userName,
                                   @RequestParam(value = "jobNumber", defaultValue = "") String jobNumber,
                                   @RequestParam(value = "identity", defaultValue = "") String identity,
                                   @RequestParam(value = "groupCode", defaultValue = "") String groupCode,
                                   @RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                   @RequestParam(value = "mobilePhone", defaultValue = "") String mobilePhone,
                                   @RequestParam(value = "officePhone", defaultValue = "") String officePhone,
                                   @RequestParam(value = "aliveStatus", required = false) Integer aliveStatus,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();
        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (userName.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userName", userName));
        }

        if (jobNumber.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("jobNumber", jobNumber));
        }

        if (identity.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("identity", identity));
        }

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (groupCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("groupCode", groupCode));
        }

        if (mobilePhone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("mobilePhone", mobilePhone));
        }

        if (officePhone.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("officePhone", officePhone));
        }

        if (aliveStatus != null)
        {
            params.putAll(ImmutableMap.of("aliveStatus", aliveStatus));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SysUser> users = sysUserService.retrieve(params);
        Integer count = sysUserService.count(params);

        return ResponseEntity.ok(ListRep.assemble(users, count));
    }

    @OperationLog(desc = "创建用户")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.POST)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN })
    public ResponseEntity create(@RequestBody SysUser sysUser, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        sysUserService.create(sysUser);
        URI uri = ucBuilder.path("/api/sysusers/{userId}").buildAndExpand(sysUser.getUserId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改用户信息")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.PUT)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN })
    public ResponseEntity update(@RequestParam(value = "userId", defaultValue = "",required = true) String userId, @RequestBody SysUser newUser)
    {
        ResponseEntity responseEntity;

        Optional<User> user = sysUserService.findByUserId(userId);
        if (user.isPresent())
        {
            sysUserService.update(user.get().getId(), newUser);
            responseEntity = ResponseEntity.ok().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "删除用户信息")
    @RequestMapping(value = "/api/sysusers", method = RequestMethod.DELETE)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN })
    public ResponseEntity delete(  @RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;

        Optional<User> user = sysUserService.findByUserId(userId);
        if (user.isPresent())
        {
            sysUserService.delete(user.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }


    @OperationLog(desc = "更新位置信息")
    @RequestMapping(value = "/api/sysusers/position", method = RequestMethod.POST)
    public ResponseEntity updatePostion(@RequestParam(value = "userId", defaultValue = "",required = true) String userId,
                                        @RequestBody UserPosition userPosition )
    {
        ResponseEntity responseEntity;
        sysUserService.updatePosition(userId,userPosition);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "查询部门负责人")
    @RequestMapping(value = "/api/sysusers/GetDepLeader", method = RequestMethod.GET)
    public ResponseEntity getDepLeaderByUserId(@RequestParam(value = "groupCode", defaultValue = "",required = true) String groupCode,
                                               @RequestParam(value = "userId", defaultValue = "",required = true) String userId)
    {
        ResponseEntity responseEntity;

        List<SysUser> sysUsers = sysUserService.getDepLeaderByUserId(userId,groupCode);

        responseEntity = ResponseEntity.ok(ListRep.assemble(sysUsers, sysUsers.size()));

        return responseEntity;
    }



    @OperationLog(desc = "上传用户照片")
    @RequestMapping(value = "/api/sysusers/photo/{userId}", method = RequestMethod.PUT)
    public ResponseEntity uploadUserPhoto(HttpServletRequest request,@PathVariable("userId") String userId)
    {
        ResponseEntity responseEntity;

        Optional<User> user = sysUserService.findByUserId(userId);
        if (user.isPresent())
        {
            MultipartFile file = null;
            MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
            List<MultipartFile> files = multipartRequest.getFiles("file");
            if(files.size()==0){
                responseEntity = ResponseEntity.badRequest().build();
                return responseEntity;
            }

            for (int i = 0; i < files.size(); ++i) {
                file = files.get(i);
                if (!file.isEmpty()) {
                    try {
                        byte[] photo = file.getBytes();
                        sysUserService.uploadPhoto(userId, photo);

                    } catch (Exception e) {
                        System.out.println("创建失败");
                        responseEntity = ResponseEntity.badRequest().build();
                        return responseEntity;
                    }
                } else {
                    responseEntity = ResponseEntity.noContent().build();
                    return responseEntity;
                }
            }
            responseEntity = ResponseEntity.ok().build();

        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "下载用户照片")
    @RequestMapping(value = "/api/sysusers/photo/{userId}", method = RequestMethod.GET)
    public void downloadUserPhoto(HttpServletResponse httpServletResponse, @PathVariable("userId") String userId) throws IOException {
        //从数据库中获取流程图的二进制数据
        Optional<User> user = sysUserService.findByUserId(userId);
        if (user.isPresent())
        {
            byte[] photo = sysUserService.downloadPhoto(userId);
            //如果是空，就选取admin的头像
            if(photo==null){
                photo = sysUserService.downloadPhoto("admin");
            }
            if(photo==null) {
                httpServletResponse.setStatus(404);
            }
            httpServletResponse.setContentType("image/png");
            httpServletResponse.setStatus(200);
            OutputStream os = httpServletResponse.getOutputStream();
            os.write(photo);
            os.flush();
            os.close();




        }
        else
        {
            httpServletResponse.setStatus(404);
        }
    }



}
