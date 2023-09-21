package com.kuangkie.device.serverless;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.kuangkie.carbon.common.IntegrationMsg;
import com.kuangkie.carbon.fg.ImproveResultFactory;
import com.kuangkie.carbon.fg.ops.ProRecordOpsBuilder;
import com.kuangkie.carbon.panel.CarbonPanel;
import com.kuangkie.carbon.panel.RecordIntegration;
import com.kuangkie.carbon.record.FGRecord;
import com.kuangkie.carbon.record.FGRecordBuilder;
import com.kuangkie.carbon.record.ProRecord;
import com.kuangkie.device.alibaba.OssClient;
import com.kuangkie.device.alibaba.sae.SLBClient;
import com.kuangkie.device.alibaba.sae.SaeClient;
import com.kuangkie.device.fg.BaseConstant;
import com.kuangkie.device.fg.EnumKeyValue;
import com.kuangkie.device.fg.item.DeployConfigHGVBE6828Item;
import com.kuangkie.device.fg.item.TmplMetadataHGVBE6805Item;
import com.kuangkie.hydrogenv2.algorithm.Serverless;

/**
 * 	阿里SAE 
 * @author lhb
 */
@Component
public class SaeServerless implements Serverless{
	 Logger logger = LoggerFactory.getLogger(SaeServerless.class);
	 
	@Override
	public void publish(ImproveResultFactory improveResultFactory,	
			String jarName, String jarPath, FGRecord deployConfig) {
		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
		
		String accessKeyId = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeyId);
		if (accessKeyId == null || StringUtils.isBlank(accessKeyId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeyId！");
			return;
		}
		
		String accessKeySecret = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeySecret);
		if (accessKeySecret == null || StringUtils.isBlank(accessKeySecret)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeySecret！");
			return;
		}
		
		// 上传jar 到oss
		OssClient ossClient = new OssClient(accessKeyId, accessKeySecret);
		String url = ossClient.uploading(jarName, jarPath);
		if (url == null) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "上传失败！");
			return;
		} else {
			// 上传成功， 组装oss 文件 url
			currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态, EnumKeyValue.版本状态_已发布_yfb);
			currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_ossurl, url);
		}
	}

	@Override
	public void onlineDeploy(ProRecord proRecord, FGRecord deployConfig, ImproveResultFactory improveResultFactory) {
		
		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
		
		String accessKeyId = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeyId);
		if (accessKeyId == null || StringUtils.isBlank(accessKeyId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeyId！");
			return;
		}
		
		String accessKeySecret = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeySecret);
		if (accessKeySecret == null || StringUtils.isBlank(accessKeySecret)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeySecret！");
			return;
		}
		
		String internetSlbId = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_internetSlbId);
		if (internetSlbId == null || StringUtils.isBlank(internetSlbId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写internetSlbId！");
			return;
		}
		
		String recordCode = proRecord.getRecordCode();
		String version = proRecord.getString(TmplMetadataHGVBE6805Item.基本属性组_版本);
		String port = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线端口);
		if (port == null || StringUtils.isBlank(port)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写上线端口！");
			return;
		}
		String shangxianVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线版本编码);
		if (shangxianVersionOnlyCode == null || StringUtils.isBlank(shangxianVersionOnlyCode)) {
			
		} else {
			// 如果存在正在运行的应用， 需要先删除应用
			stopApp(deployConfig, improveResultFactory, shangxianVersionOnlyCode, true);
		}
		String appId = createApp(proRecord, improveResultFactory, version,
				accessKeyId, accessKeySecret, port, internetSlbId);
		
		currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态,EnumKeyValue.版本状态_运行中_yxz);
		 
		 // 更新状态
		FGRecordBuilder deployConfigBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_部署配置, deployConfig.getRecordCode());
		
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本号, version);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本编码, recordCode);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线appid, appId);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线访问地址, "47.111.159.127:" + port);
		
		 RecordIntegration integration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_部署配置);
		 IntegrationMsg msg = integration.integrate(deployConfigBuilder.getRecord()); 
		 boolean success = msg.success();
		 if (success) {
			 
		 } else {
			 
		 }
	}

	@Override
	public void testDeploy(ProRecord proRecord, ImproveResultFactory improveResultFactory, String recordCode,
				String version, FGRecord deployConfig) {
		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
			if (deployConfig == null) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请先填写【部署配置】信息！");
				return;
			}
			String accessKeyId = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeyId);
			if (accessKeyId == null || StringUtils.isBlank(accessKeyId)) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeyId！");
				return;
			}
			String accessKeySecret = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeySecret);
			if (accessKeySecret == null || StringUtils.isBlank(accessKeySecret)) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeySecret！");
				return;
			}
			String port = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试端口);
			if (port == null || StringUtils.isBlank(port)) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写测试端口！");
				return;
			}
			String internetSlbId = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_internetSlbId);
			if (internetSlbId == null || StringUtils.isBlank(internetSlbId)) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写internetSlbId！");
				return;
			}
			
			String testVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试版本编码);
			if (testVersionOnlyCode == null || StringUtils.isBlank(testVersionOnlyCode)) {
				
			} else {
				// 如果存在正在运行的应用， 需要先删除应用
				stopApp(deployConfig, improveResultFactory, testVersionOnlyCode, true);
			}
			String appId = createApp(proRecord, improveResultFactory, version,
					accessKeyId, accessKeySecret, port, internetSlbId);
			currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态,EnumKeyValue.版本状态_测试中_csz);
			
			 // 更新状态
			FGRecordBuilder deployConfigBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_部署配置, deployConfig.getRecordCode());
			
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本号, version);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本编码, recordCode);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试appid, appId);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试访问地址, "47.111.159.127:" + port);
			RecordIntegration integration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_部署配置);
			 IntegrationMsg msg = integration.integrate(deployConfigBuilder.getRecord()); 
			 boolean success = msg.success();
			 if (success) {
			 } else {
			 }
		
		}

	/**
	 * 	删除正在运行的应用
	 * @param improveResultFactory
	 * @param updateStatus 是否更新版本的状态
	 * @param version
	 */
	@Override
	public boolean stopApp(FGRecord deployConfig, ImproveResultFactory improveResultFactory, String versionOnlyCode, boolean updateStatus) {
		
			if (deployConfig == null) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请先填写【部署配置】信息！");
				return false;
			}
			String accessKeyId = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeyId);
			if (accessKeyId == null || StringUtils.isBlank(accessKeyId)) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeyId！");
				return false;
			}
			String accessKeySecret = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_accessKeySecret);
			if (accessKeySecret == null || StringUtils.isBlank(accessKeySecret)) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写accessKeySecret！");
				return false;
			}
			//上线版本编码
			String shangxianVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线版本编码);
			//测试版本编码
			String testVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试版本编码);
			String appid = null;
			FGRecordBuilder deployConfigBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_部署配置, deployConfig.getRecordCode());
			
			if (versionOnlyCode.equals(shangxianVersionOnlyCode)) {
				appid = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线appid);
				// 清空配置信息
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本号, null);
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本编码, null);
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线appid, null);
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线访问地址, null);
			} else if (versionOnlyCode.equals(testVersionOnlyCode)) {
				appid = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试appid);
				// 清空配置信息
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本号, null);
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本编码, null);
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试appid, null);
				deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试访问地址, null);
			}
			
			SaeClient saeClient = new SaeClient(accessKeyId, accessKeySecret);
			boolean deleteApplication = saeClient.deleteApplication(appid);
			if (!deleteApplication) {
//						improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "应用停运失败！");
//						return;
			}
			
			 RecordIntegration integration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_部署配置);
			 IntegrationMsg msg = integration.integrate(deployConfigBuilder.getRecord()); 
			 boolean success = msg.success();
			 if (success) {
			 } else {
			 }
			
			 if (updateStatus) {
				 FGRecordBuilder metaBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_模板元数据, versionOnlyCode);
				metaBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态, EnumKeyValue.版本状态_已发布_yfb);
				RecordIntegration metaIntegration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_模板元数据);
				 IntegrationMsg metaMsg = metaIntegration.integrate(metaBuilder.getRecord()); 
				 boolean metaSuccess = metaMsg.success();
				 if (metaSuccess) {
				 } else {
					 
				 }
			 }
			return true;
		}
	
	/**
	 * 创建一个应用
	 * @param proRecord
	 * @param improveResultFactory
	 * @param version
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @param port
	 * @param internetSlbId
	 * @return
	 */
	private String createApp(ProRecord proRecord, ImproveResultFactory improveResultFactory, String version,
	 String accessKeyId, String accessKeySecret, String port,
			String internetSlbId) {
		
//		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
		
		String packageUrl = proRecord.getString(TmplMetadataHGVBE6805Item.基本属性组_ossurl);
		packageUrl = packageUrl.trim();
		
		SaeClient saeClient = new SaeClient(accessKeyId, accessKeySecret);
		String appId = saeClient.createApplication(version, packageUrl);
//		currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_appid, appId);

		// 检测应用是否部署成功
		boolean flag = saeClient.checkApp(appId);
		if (flag) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("开始绑定公网ip ！");
			// 绑定SLB 公网地址
			SLBClient slbClient = new SLBClient();
			
			boolean bindSlb = slbClient.bindSlb(appId, internetSlbId, port, accessKeyId, accessKeySecret);
			
			if (bindSlb) {
				logger.info("绑定执行中 ！");
				boolean checkApp = saeClient.checkApp(appId);
				if (checkApp) {
					logger.info("绑定公网ip 成功！");
//					currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_访问地址, "47.111.159.127:" + port);
				} else {
					improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "绑定公网ip 失败！");
					return null;
				}
			}
		} else {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "项目启动失败， 请前往SAE查看具体启动日志或重新打包上传！");
			return null;
		}
		return appId;
	}
	

}