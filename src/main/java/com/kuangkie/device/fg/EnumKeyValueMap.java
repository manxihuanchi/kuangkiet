package com.kuangkie.device.fg;

import java.util.HashMap;
import java.util.Map;

public class EnumKeyValueMap {
	
	private static Map<Long,String> map=new HashMap<>();
	
	static {
		map.put(EnumKeyValue.执行确认类型_默认_mr, "default");
		map.put(EnumKeyValue.执行确认类型_不确认_bqr, "none");
		map.put(EnumKeyValue.执行确认类型_危险确认_wxqr, "danger");
		
		map.put(EnumKeyValue.图类型_柱状图_zzt, "column");
		map.put(EnumKeyValue.图类型_饼图_bt, "pie");
		map.put(EnumKeyValue.图类型_折线图_zxt, "line");
		
		map.put(EnumKeyValue.保存跳转选项_列表_lb, "list");
		map.put(EnumKeyValue.保存跳转选项_添加_tj, "add");
		map.put(EnumKeyValue.保存跳转选项_编辑_bj, "edit");
		
		map.put(EnumKeyValue.客户端_PC_, "PC");
		map.put(EnumKeyValue.客户端_APP_, "APP");
		map.put(EnumKeyValue.客户端_H5_, "H5");
		map.put(EnumKeyValue.客户端_微信_wx, "WX");
		map.put(EnumKeyValue.客户端_钉钉_dd, "DDING");
		
		map.put(EnumKeyValue.模型类型_普通模型_ptmx, "普通模型");
		map.put(EnumKeyValue.模型类型_SQL统计模型_tjmx, "SQL统计模型");
		map.put(EnumKeyValue.模型类型_SQL查询模型_cxmx, "SQL查询模型");
		map.put(EnumKeyValue.模型类型_文档模型_wdmx, "文档模型");
		map.put(EnumKeyValue.模型类型_服务模型_fwmx, "服务模型");

		map.put(EnumKeyValue.按钮类型_primary_, "primary");
		map.put(EnumKeyValue.按钮类型_ghost_, "ghost");
		map.put(EnumKeyValue.按钮类型_dashed_, "dashed");
		map.put(EnumKeyValue.按钮类型_link_, "link");
		map.put(EnumKeyValue.按钮类型_text_, "text");
		map.put(EnumKeyValue.按钮类型_default_, "default");

		map.put(EnumKeyValue.按钮形状_default_, "default");
		map.put(EnumKeyValue.按钮形状_circle_, "circle");
		map.put(EnumKeyValue.按钮形状_round_, "round");

		map.put(EnumKeyValue.编辑按钮位置_右侧固定_ycgd, "fixedBottomRight");
		map.put(EnumKeyValue.编辑按钮位置_最下靠左_zxkz, "bottomLeft");
		map.put(EnumKeyValue.编辑按钮位置_最下居中_zxjz, "bottomCenter");
		map.put(EnumKeyValue.编辑按钮位置_最下靠右_zxky, "bottomRight");
	}
	
	public static String mapping(Long key) {
		if(key==null) {
			return null;
		}
		return map.get(key);
	}

}
