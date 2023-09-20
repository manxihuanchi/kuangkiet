package com.kuangkie.device.alibaba.sae;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.sae20190506.Client;
import com.aliyun.sae20190506.models.DeleteApplicationResponse;
import com.aliyun.sae20190506.models.DeleteApplicationResponseBody;
import com.aliyun.sae20190506.models.DescribeApplicationStatusResponse;
import com.aliyun.sae20190506.models.DescribeApplicationStatusResponseBody;
import com.aliyun.sae20190506.models.ListChangeOrdersResponse;
import com.aliyun.sae20190506.models.ListChangeOrdersResponseBody;
import com.aliyun.sae20190506.models.ListChangeOrdersResponseBody.ListChangeOrdersResponseBodyData;
import com.aliyun.sae20190506.models.ListChangeOrdersResponseBody.ListChangeOrdersResponseBodyDataChangeOrderList;
import com.aliyun.tea.TeaException;
import com.kuangkie.hydrogenv2.bnb.TmplMetadataHGVBE6805BNB;

/**
 * 调用SAE 的接口
 * @author lhb
 */
public class SaeClient {
	  private  String accessKeyId = null;
	  private String accessKeySecret = null;

	  Logger logger = LoggerFactory.getLogger(SaeClient.class);

	  	public SaeClient(String accessKeyId, String accessKeySecret) {
	  		this.accessKeyId = accessKeyId;
	  		this.accessKeySecret = accessKeySecret;
	  	}
	  
	    /**
	     * 使用AK&SK初始化账号Client
	     * @param accessKeyId
	     * @param accessKeySecret
	     * @return Client
	     * @throws Exception
	     */
	    public static com.aliyun.sae20190506.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
	        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
	                // 必填，您的 AccessKey ID
	                .setAccessKeyId(accessKeyId)
	                // 必填，您的 AccessKey Secret
	                .setAccessKeySecret(accessKeySecret);
	        // Endpoint 请参考 https://api.aliyun.com/product/sae
	        config.endpoint = "sae.cn-hangzhou.aliyuncs.com";
	        return new com.aliyun.sae20190506.Client(config);
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

	      /**
	       * 创建应用
	       */
	      public String createApplication(String appName, String packageUrl)  {
	    	  try {
	    		  
	          // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
	          // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
	          Client client = SaeClient.createClient(this.accessKeyId, this.accessKeySecret);
	          com.aliyun.teaopenapi.models.Params params = this.createApiInfo();
	          // query params
	          java.util.Map<String, Object> queries = new java.util.HashMap<>();
	          queries.put("AppName", appName);
	          queries.put("NamespaceId", "cn-hangzhou");
	          queries.put("PackageType", "FatJar");
	          queries.put("PackageVersion", "1.0.0");
	          
	          queries.put("PackageUrl", packageUrl);
	          
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
	          
	          return appId;
	    	  }catch (Exception e) {
					// TODO: handle exception
			}
	    	  
	    	  return null;
	      }
	      
	      /**
	       * 删除应用
	       * @param appid
	       * @return
	       */
	      public boolean deleteApplication(String appid) {
	          // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
	          // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
	    	  try {
	    	  com.aliyun.sae20190506.Client client = this.createClient(accessKeyId, accessKeySecret);
	          com.aliyun.sae20190506.models.DeleteApplicationRequest deleteApplicationRequest = new com.aliyun.sae20190506.models.DeleteApplicationRequest()
	                  .setAppId(appid);
	          com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
	          java.util.Map<String, String> headers = new java.util.HashMap<>();
	         
	              // 复制代码运行请自行打印 API 的返回值
	              DeleteApplicationResponse deleteApplicationWithOptions = client.deleteApplicationWithOptions(deleteApplicationRequest, headers, runtime);
	              DeleteApplicationResponseBody body = deleteApplicationWithOptions.getBody();
	              Boolean success = body.getSuccess();
	              return success.booleanValue();
	          } catch (TeaException error) {
	              // 如有需要，请打印 error
	              com.aliyun.teautil.Common.assertAsString(error.message);
	          } catch (Exception _error) {
	              TeaException error = new TeaException(_error.getMessage(), _error);
	              // 如有需要，请打印 error
	              com.aliyun.teautil.Common.assertAsString(error.message);
	          }
			return false;        
	      }

	      	/**
	      	 * 获取应用的状态信息
	      	 * @param appId
	      	 * @return RUNNING、STOPPED 、UNKNOWN
	      	 */
	          public String describeApplicationStatus(String appId)  {
	              // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
	              // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
	        	  try {
	        	  com.aliyun.sae20190506.Client client = this.createClient(accessKeyId, accessKeySecret);
	              com.aliyun.sae20190506.models.DescribeApplicationStatusRequest describeApplicationStatusRequest = new com.aliyun.sae20190506.models.DescribeApplicationStatusRequest()
	                      .setAppId(appId);
	              com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
	              java.util.Map<String, String> headers = new java.util.HashMap<>();
	             
	                  // 复制代码运行请自行打印 API 的返回值
	                  DescribeApplicationStatusResponse describeApplicationStatusWithOptions = client.describeApplicationStatusWithOptions(describeApplicationStatusRequest, headers, runtime);
	              
	                  DescribeApplicationStatusResponseBody body = describeApplicationStatusWithOptions.getBody();
	                  String currentStatus = body.getData().getCurrentStatus();
	                  
	                  return currentStatus;
	        	  } catch (TeaException error) {
	                  // 如有需要，请打印 error
	                  com.aliyun.teautil.Common.assertAsString(error.message);
	              } catch (Exception _error) {
	                  TeaException error = new TeaException(_error.getMessage(), _error);
	                  // 如有需要，请打印 error
	                  com.aliyun.teautil.Common.assertAsString(error.message);
	              }
				return null;        
	          }
	   
	          /**
	           * 获取变更单列表
	           * @param args_
	           * @throws Exception
	           */
	          public int listChangeOrders(String appId) {
	              // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
	              // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
	        	  try {
	        	  com.aliyun.sae20190506.Client client = this.createClient(accessKeyId, accessKeySecret);
	              com.aliyun.sae20190506.models.ListChangeOrdersRequest listChangeOrdersRequest = new com.aliyun.sae20190506.models.ListChangeOrdersRequest()
	                      .setAppId(appId);
	              com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
	              java.util.Map<String, String> headers = new java.util.HashMap<>();
	             
	                  // 复制代码运行请自行打印 API 的返回值
	                  ListChangeOrdersResponse listChangeOrdersWithOptions = client.listChangeOrdersWithOptions(listChangeOrdersRequest, headers, runtime);
	                  ListChangeOrdersResponseBody body = listChangeOrdersWithOptions.getBody();
	                  ListChangeOrdersResponseBodyData data = body.getData();
	                  List<ListChangeOrdersResponseBodyDataChangeOrderList> changeOrderList = data.getChangeOrderList();
	                  
	                  int result = 2;
	                  
	                  for (ListChangeOrdersResponseBodyDataChangeOrderList listChangeOrdersResponseBodyDataChangeOrderList : changeOrderList) {
	                	  Integer status = listChangeOrdersResponseBodyDataChangeOrderList.getStatus();
	                	  int intValue = status.intValue();
	                	  if (intValue == 0) {
	                		  // 准备
	                	  } else if (intValue == 1) {
	                		  // 执行中
	                		  result = 1;
	                		  break;
	                	  } else if (intValue == 2) {
	                		  // 执行成功。
	                	  } else if (intValue == 3) {
	                		  // 执行失败。
	                		  result = 3;
	                		  break;
	                	  } 
	                  }
	                  return result;
	              } catch (TeaException error) {
	                  // 如有需要，请打印 error
	                  com.aliyun.teautil.Common.assertAsString(error.message);
	              } catch (Exception _error) {
	                  TeaException error = new TeaException(_error.getMessage(), _error);
	                  // 如有需要，请打印 error
	                  com.aliyun.teautil.Common.assertAsString(error.message);
	              }
				return -1;  
	          }
	          
	          /**
	      	 * 检测应用的变更记录
	      	 * @param saeClient
	      	 * @param appId
	      	 * @return
	      	 */
	      	public boolean checkApp(String appId) {
	      		boolean flag = false;
	      		int count = 0;
	      		while (true) {
	      			// 获取应用的状态信息
//	      			String describeApplicationStatus = saeClient.describeApplicationStatus(appId);
//	      			System.out.println("应用状态："+ describeApplicationStatus);
//	      			if ("RUNNING".equals(describeApplicationStatus)) {
//	      				// 成功运行
//	      				flag = true;
//	      				break;
//	      			}
	      			
//	      			count++;
//	      			if (count > 15) {
//	      				flag = false;
//	      				break;
//	      			}
//	      			
//	      			try {
//	      				Thread.sleep(10000);
//	      			} catch (InterruptedException e) {
//	      				e.printStackTrace();
//	      			}
	      			
	      			int listChangeOrders = this.listChangeOrders(appId);
	      			if (listChangeOrders == -1) {
	      				logger.info("部署失败！");
	      				flag = false;
	      				break;
	      			} else if (listChangeOrders == 1) {
	      				// 应用正在执行中
	      				logger.info("正在部署");
	      			}else if (listChangeOrders == 2) {
	      				logger.info("部署成功！");
	      				flag = true;
	      				break;
	      			}else if (listChangeOrders == 3) {
	      				logger.info("部署失败！");
	      				flag = false;
	      				break;
	      			}
	      			
	      			try {
	      				Thread.sleep(10000);
	      			} catch (InterruptedException e) {
	      				e.printStackTrace();
	      			}
	      			
	      		}
	      		return flag;
	      	}
	          
}
