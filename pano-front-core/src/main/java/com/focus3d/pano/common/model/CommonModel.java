package com.focus3d.pano.common.model;

import com.focustech.cief.ibatis.domain.BaseEntity;
/**
 *
 * *
 * @author lihaijun
 *
 */
public interface CommonModel extends BaseEntity {
	 public Long getSn();
	 public void setSn(Long sn);
	 public String getEncryptSn();
	 public void setEncryptSn(String encryptSn);
}
