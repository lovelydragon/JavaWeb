package com.mapper;

import com.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Mapper
public interface UserMapper {

    @Select("SELETE * From user")
    public List<User> selectAll();
}
