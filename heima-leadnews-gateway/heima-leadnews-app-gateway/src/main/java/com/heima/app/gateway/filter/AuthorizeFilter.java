package com.heima.app.gateway.filter;

import com.heima.app.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @BelongsProject: heima-leadnews
 * @BelongsPackage: com.heima.app.gateway.filter
 * @Author: yanhongwei
 * @CreateTime: 2025-01-06  15:38
 * @Description: TODO
 * @Version: 1.0
 */

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {

     /*
      * @Title: filter
      * @Author: pyzxW
      * @Date: 2025-01-06 15:40:34
      * @Params:  
      * @Return: null
      * @Description: 过滤的方法
      */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2.判断是否是登录
        //如果不是登录请求则进行下一步token检验
        if (request.getURI().getPath().contains("/login")){
            //登录则放行
            return chain.filter(exchange);
        }
        //3.获取token
        String token = request.getHeaders().getFirst("token");
        //4.判断token是否存在
        if (StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);//校验失败
            //结束请求
            return response.setComplete();
        }
        //5.判断token是否有效
        try{
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            //判断是否过期
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 && result == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);//token过期，校验失败
                //结束请求
                return response.setComplete();
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);//token过期，校验失败
            //结束请求
            return response.setComplete();
        }

        //6.放行
        return chain.filter(exchange);
    }

     /*
      * @Title: getOrder
      * @Author: pyzxW
      * @Date: 2025-01-06 15:39:57
      * @Params:  
      * @Return: null
      * @Description: 优先级设置，值越小，优先级越高
      */
    @Override
    public int getOrder() {
        return 0;
    }
}
