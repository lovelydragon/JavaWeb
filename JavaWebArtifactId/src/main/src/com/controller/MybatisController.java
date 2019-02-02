package com.controller;

import com.bean.User;
import com.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "mybatis")
public class MybatisController {

    private SqlSessionFactory sqlSessionFactory;

//    @Resource
//    private UserMapper userMapper;

    @RequestMapping(value = "user")
    public void test(){
        List<User> userList = new ArrayList<>();
        SqlSession sqlSession = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            inputStream.close();
            sqlSession = sqlSessionFactory.openSession();
            User user = new User();
            user.setPwd("中文");
            user.setUserId(2);
            sqlSession.insert("User.insertUser",user);
            sqlSession.commit();
            userList = sqlSession.selectList("User.selectAll");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        for (User user: userList){
            System.out.println(user.getId()+" "+user.getUserId()+" "+user.getPwd());
        }
    }

    @RequestMapping(value = "mapper")
    public void annotation(){
        System.out.println("中文？");
//        List<User> userList = userMapper.selectAll();
//        for (User user: userList){
//            System.out.println(user.getId()+" "+user.getUserId()+" "+user.getPwd());
//        }
    }
}
