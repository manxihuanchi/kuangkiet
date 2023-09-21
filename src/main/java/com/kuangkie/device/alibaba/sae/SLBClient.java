package com.kuangkie.device.alibaba.sae;
import com.aliyun.sae20190506.models.BindSlbResponse;
import com.aliyun.sae20190506.models.BindSlbResponseBody;
import com.aliyun.tea.*;
/**
 * SLB 
 * @author lhb
 *
 */
public class SLBClient {
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
	    
	    public boolean bindSlb(String appId, String internetSlbId, String port, String accessKeyId, String accessKeySecret) {
	    	 try {
	    	// 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
	        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
	        com.aliyun.sae20190506.Client client = this.createClient(accessKeyId, accessKeySecret);
	        com.aliyun.sae20190506.models.BindSlbRequest bindSlbRequest = new com.aliyun.sae20190506.models.BindSlbRequest()
	                .setAppId(appId)
	                .setInternetSlbId(internetSlbId)
	                .setInternet("[{\"port\":"+port+",\"targetPort\":8080,\"protocol\":\"TCP\"}]");;
	        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
	        java.util.Map<String, String> headers = new java.util.HashMap<>();
	       
	            // 复制代码运行请自行打印 API 的返回值
	            BindSlbResponse bindSlbWithOptions = client.bindSlbWithOptions(bindSlbRequest, headers, runtime);
	        
	            BindSlbResponseBody body = bindSlbWithOptions.getBody();
	            Boolean success = body.getSuccess();
	            return success;
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
	}
