/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.maxkey.persistence.service;

import java.time.Duration;
import java.util.List;

import org.apache.mybatis.jpa.persistence.JpaBaseService;
import org.ehcache.UserManagedCache;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.maxkey.entity.apps.Apps;
import org.maxkey.entity.apps.UserApps;
import org.maxkey.persistence.mapper.AppsMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AppsService extends JpaBaseService<Apps>{

	protected final static  UserManagedCache<String, Apps> appsDetailsCacheStore = 
			UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class, Apps.class)
				.withExpiry(
					ExpiryPolicyBuilder.timeToLiveExpiration(
							Duration.ofHours(1)
					)
				)
				.build(true);
	
	public AppsService() {
		super(AppsMapper.class);
	}

	/* (non-Javadoc)
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsMapper getMapper() {
		// TODO Auto-generated method stub
		return (AppsMapper)super.getMapper();
	}
	
	public boolean insertApp(Apps app) {
		return ((AppsMapper)super.getMapper()).insertApp(app)>0;
	};
	public boolean updateApp(Apps app) {
		return ((AppsMapper)super.getMapper()).updateApp(app)>0;
	};
	
	public boolean updateExtendAttr(Apps app) {
		return ((AppsMapper)super.getMapper()).updateExtendAttr(app)>0;
	}
	
    public List<UserApps> queryMyApps(UserApps userApplications){
        return getMapper().queryMyApps(userApplications);
    }
    
    //cache for running
    public void storeCacheAppDetails(String appId, Apps appDetails) {
    	appsDetailsCacheStore.put(appId, appDetails);
	}
	
    public Apps getCacheAppDetails(String appId) {
    	Apps appDetails=appsDetailsCacheStore.get(appId); 
        return appDetails;
    }
}
