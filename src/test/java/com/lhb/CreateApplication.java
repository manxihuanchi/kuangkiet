package com.lhb;
//This file is auto-generated, don't edit it. Thanks.

import java.util.Map;

//This file is auto-generated, don't edit it. Thanks.
import com.aliyun.tea.*;

public class CreateApplication {

	 private final static String accessKeyId = "LTAI5t7jE7cCpGmxay7fQTJi";
	  private final static String accessKeySecret = "2zVcAQRzVll0fQHfLn60LmVl10QLVZ";

	      /**
	       * 使用AK&SK初始化账号Client
	       * @param accessKeyId
	       * @param accessKeySecret
	       * @return Client
	       * @throws Exception
	       */
	      public static com.aliyun.teaopenapi.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
	          com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
	                  // 必填，您的 AccessKey ID
	                  .setAccessKeyId(accessKeyId)
	                  // 必填，您的 AccessKey Secret
	                  .setAccessKeySecret(accessKeySecret);
	          // Endpoint 请参考 https://api.aliyun.com/product/sae
	          config.endpoint = "sae.cn-hangzhou.aliyuncs.com";
	          return new com.aliyun.teaopenapi.Client(config);
	      }

	      /**
	       * API 相关
	       * @param path params
	       * @return OpenApi.Params
	       */
	      public static com.aliyun.teaopenapi.models.Params createApiInfo() throws Exception {
	          com.aliyun.teaopenapi.models.Params params = new com.aliyun.teaopenapi.models.Params()
	                  // 接口名称
	                  .setAction("CreateApplication")
	                  // 接口版本
	                  .setVersion("2019-05-06")
	                  // 接口协议
	                  .setProtocol("HTTPS")
	                  // 接口 HTTP 方法
	                  .setMethod("POST")
	                  .setAuthType("AK")
	                  .setStyle("ROA")
	                  // 接口 PATH
	                  .setPathname("/pop/v1/sam/app/createApplication")
	                  // 接口请求体内容格式
	                  .setReqBodyType("formData")
	                  // 接口响应体内容格式
	                  .setBodyType("json");
	          return params;
	      }

	      public static void main(String[] args_) throws Exception {
	          java.util.List<String> args = java.util.Arrays.asList(args_);
	          // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
	          // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
	          com.aliyun.teaopenapi.Client client = CreateApplication
	        		  .createClient(accessKeyId, accessKeySecret);
	          com.aliyun.teaopenapi.models.Params params = CreateApplication.createApiInfo();
	          // query params
	          java.util.Map<String, Object> queries = new java.util.HashMap<>();
	          queries.put("AppName", "test7");
	          queries.put("NamespaceId", "cn-hangzhou");
	          queries.put("PackageType", "FatJar");
	          queries.put("PackageVersion", "1.0.0");
	          
	          queries.put("PackageUrl", "https://kuangkie.oss-cn-hangzhou.aliyuncs.com/kuangkie3-0.0.2.jar?Expires=1693672447&OSSAccessKeyId=TMP.3KivGxtSX6CQ4z1UrG2HMqQZ8ocoN4hELPsKVU94bt7nECkNcyDfBJbFE6sQufcFhqcYcVTous5QWyE6EkBc1eYnPrHAmB&Signature=a7cnWDnSlwEjq1EerB5YToYZBI0%3D");
	          
	          queries.put("Jdk", "Open JDK 8");
	          queries.put("Cpu", 1000);
	          queries.put("Memory", 2048);
	          queries.put("Replicas", 1);
	          queries.put("Deploy", true);
	          queries.put("Timezone", "Asia/Shanghai");
	          queries.put("SecurityGroupId", "sae.serverless.donot.delete.vpc-bp10npdv5wabr5o0qi2oq");
	          queries.put("AutoConfig", true);
	          queries.put("ProgrammingLanguage", "java");
	          // body params
	          java.util.Map<String, Object> body = new java.util.HashMap<>();
	          body.put("PackageRuntimeCustomBuild", null);
	          body.put("ConfigMapMountDesc", null);
	          body.put("PhpConfig", null);
	          body.put("PhpExtensions", null);
	          body.put("PhpPECLExtensions", null);
	          body.put("OssMountDescs", null);
	          body.put("OssAkId", accessKeyId);
	          body.put("OssAkSecret", accessKeySecret);
	          
	          body.put("Php", null);
	          body.put("AcrInstanceId", null);
	          body.put("EnableImageAccl", null);
	          body.put("AssociateEip", null);
	          // runtime options
	          com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
	          com.aliyun.teaopenapi.models.OpenApiRequest request = new com.aliyun.teaopenapi.models.OpenApiRequest()
	                  .setQuery(com.aliyun.openapiutil.Client.query(queries))
	                  .setBody(body);
	          // 复制代码运行请自行打印 API 的返回值
	          // 返回值为 Map 类型，可从 Map 中获得三类数据：响应体 body、响应头 headers、HTTP 返回的状态码 statusCode。
	          Map<String, ?> callApi = client.callApi(params, request, runtime);
	         
	          String appId = null;
	          Integer statusCode = (Integer)callApi.get("statusCode");
	          if (statusCode.intValue() == 200) {
	        	  // 应用创建成功
	        	  Map<String, ?> bodyMap  = (Map<String, ?>) callApi.get("body");
	        	  Map<String, ?> dataMap = (Map<String, ?>) bodyMap.get("Data");
	        	  appId = (String) dataMap.get("AppId");
	          } else {
	        	  // 失败
	          }
	          
	          
	          System.out.println();
	      }
	  }