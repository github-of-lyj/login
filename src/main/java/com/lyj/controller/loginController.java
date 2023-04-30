package com.lyj.controller;


import com.lyj.md5.SaltMD5Util;
import com.lyj.util.DBUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Controller
@RequestMapping("/login")
public class loginController {
    @RequestMapping("/index")
    public String index(){
        return "index";

    }

    @RequestMapping("/denglu")
    @ResponseBody
    public String denglu(HttpServletRequest request) throws Exception{
        String Salt = null,passwordhash = null;
        Connection connection = DBUtil.GetConnection();
        Statement statement = connection.createStatement();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("select Salt,passwordhash from users where username=" + "\"" +  username +"\"");
        ResultSet resultSet = statement.executeQuery("select Salt,passwordhash from users where username=" + "\"" +  username +"\"");
        while (resultSet.next()){
            Salt = resultSet.getString("Salt");
            passwordhash = resultSet.getString("passwordhash");
        }
        if (SaltMD5Util.generateSaltPassword(password, Salt).equals(passwordhash))
            return "密码正确";
        return "密码错误";
    }

    @RequestMapping("/zhuce")
    public String zhuce(){
        return "zhuce";
    }

    @RequestMapping("/submitdo")
    @ResponseBody
    public String submitdo(HttpServletRequest request) throws Exception{
        Connection connection = DBUtil.GetConnection();
        Statement statement = connection.createStatement();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String salt = SaltMD5Util.generateSalt();
        String saltPassword = SaltMD5Util.generateSaltPassword(password, salt);
        statement.executeUpdate("insert into users values('"+username+"','"+saltPassword+"','"+salt+"')");
        return "注册成功";
    }
}
