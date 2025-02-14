package com.heima.kafka.pojo;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.kafka.pojo
 * @Author: yanhongwei
 * @CreateTime: 2025-02-14  16:19
 * @Description: TODO
 * @Version: 1.0
 */
public class User {
    private String username;
    private Integer age;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
