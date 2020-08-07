package com.luffy.comic.controller;

import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "TestRedisController", description = "redis连接测试")
@RestController
@RequestMapping("/test/redis")
public class TestRedisController {
    private RedisService redisService;

    @ApiOperation("Set value测试")
    @GetMapping("/set")
    public CommonResult setKey(@RequestParam String key, @RequestParam String value) {
        redisService.set(key, value);
        return CommonResult.success(String.format("Set %s success", key));
    }

    @ApiOperation("Get value测试")
    @GetMapping("/get")
    public CommonResult getKey(@RequestParam String key) {
        String value = redisService.get(key);
        return CommonResult.success(String.format("get %s success, and value = %s", key, value));
    }

    @ApiOperation("Increment value测试")
    @GetMapping("/increment")
    public CommonResult incrementKey(String key, int delta) {
        long result = redisService.increment(key, delta);
        return CommonResult.success(String.format("increment %s success, and result = %d", key, result));
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
