package com.heima.model.common.dtos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageRequestDto {

     /*
      * @Title:
      * @Author: pyzxW
      * @Date: 2025-01-12 15:04:57
      * @Params:
      * @Return: null
      * @Description: 分页的信息会被传入到这个地方
      */
    protected Integer size;
    protected Integer page;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }
}
