package com.cn.chen.distributed.dao;
import com.cn.chen.distributed.domain.PermissionDto;
import com.cn.chen.distributed.domain.Role;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
/**
 * 多表联查
 * @author Administrator
 * @version 1.0
 **/
@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //根据账号查询用户信息
    public UserDTO getUserByUsername(String username){
        String sql = "select id,username,password,fullname,mobile from t_user where username = ?";
        //连接数据库查询用户
        List<UserDTO> list = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(UserDTO.class));
        if(list !=null && list.size()==1){
            return list.get(0);
        }
        return null;
    }
    //根据用户id查询用户权限
    public List<String> findPermissionsByUserId(String userId){
// 没优化的sql
    /*    String sql = "SELECT * FROM t_permission WHERE id IN(\n" +
                "\n" +
                "SELECT permission_id FROM t_role_permission WHERE role_id IN(\n" +
                "  SELECT role_id FROM t_user_role WHERE user_id = ? \n" +
                ")\n" +
                ")\n";*/
// 优化后的sql
        String sql = " SELECT p.* FROM  t_user AS u\n" +
                "  LEFT JOIN t_user_role AS ur\n" +
                "    ON u.id = ur.user_id\n" +
                "  LEFT JOIN t_role AS r\n" +
                "    ON r.id = ur.role_id\n" +
                "  LEFT JOIN t_role_permission AS rp\n" +
                "    ON r.id = rp.role_id\n" +
                "  LEFT JOIN t_permission AS p\n" +
                "    ON p.id = rp.permission_id\n" +
                "WHERE u.id = ?";
        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));
        return permissions;
    }
    /*
 SELECT p.*
FROM
  t_user AS u
  LEFT JOIN t_user_role AS ur
    ON u.id = ur.user_id
  LEFT JOIN t_role AS r
    ON r.id = ur.role_id
  LEFT JOIN t_role_permission AS rp
    ON r.id = rp.role_id
  LEFT JOIN t_permission AS p
    ON p.id = rp.permission_id
WHERE u.id = 2
*/
 //    根据用户id查询用户角色
    public List<Role> findRolesByUserId(String userId){
        String sql = "SELECT role_name FROM t_role WHERE role_id IN(SELECT role_id FROM t_user_role WHERE user_id = ?)";
        List<Role> list = jdbcTemplate.query(sql,new Object[]{userId}, new BeanPropertyRowMapper<>(Role.class));
        return list;
    }
}
