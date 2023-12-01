# 日志
记录接口日志
基于spel，记录日志
可记录接口耗时(end_time - start_time)
注解方式实现，对业务无侵入

### 依赖
- spring boot 3
- jdk 17
- mybatis plus

### module
- core：基础类
- web：使用example

### 使用方式
> 核心是@LogRecord注解。使用方法参考com.bamboo.log.controller.BizController.java

#### Example Code:
``` java
@LogRecord(associateIdExpr = "#id", type = LogBizEnum.BIZ1, operation = "ADD")
@PostMapping("/add")
public String add(@RequestParam(name = "id") String id) {
return "hello " + id;
}
```

### 执行结果
![img.png](img.png)