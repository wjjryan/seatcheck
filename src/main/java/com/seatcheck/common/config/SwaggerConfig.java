package com.seatcheck.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: wjj
 * @date:
 * @time:
 * @description:
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /** 接口版本号 **/
    private final String version = "1.0";
    /** 接口大标题 **/
    private final String title = "座位校验";
    /** 具体的描述 **/
    private final String description = "座位校验接口文档";

    /** 接口作者联系方式 **/
    private final Contact contact = new Contact("wjj", "https://www.baidu.cn/", "1226808765@qq.com");

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.seatcheck"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .contact(contact)
                .build();
    }
}
/**
 *  swagger-ui.html#
 *  //表示一个http请求的操作
 *     @ApiOperation(valu = "修改指定产品", httpMethod = "PUT", produces = "application/json")
 *     //@ApiImplicitParams用于方法，包含多个@ApiImplicitParam表示单独的请求参数
 *     @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "产品ID", required = true, paramType = "path")})
 * //Model中的注解示例
 * //表示对类进行说明，用于参数用实体类接收
 * @ApiModel(value = "产品信息")
 * //表示对model属性的说明或者数据操作更改
 *     @ApiModelProperty(required = true, name = "name", value = "产品名称", dataType = "query")
 *
 * */
