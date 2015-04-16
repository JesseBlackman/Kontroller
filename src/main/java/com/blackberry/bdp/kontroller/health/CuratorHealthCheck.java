/*
 * Copyright 2015 dariens.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blackberry.bdp.kontroller.health;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import com.codahale.metrics.health.HealthCheck;

public class CuratorHealthCheck extends HealthCheck {
	private final CuratorFramework framework;

	/**
	* Constructor
	* 
	* @param framework
	*            {@link CuratorFramework}
	*/
	public CuratorHealthCheck(@Nonnull final CuratorFramework framework) {
		this.framework = checkNotNull(framework);
	}

	/**
	* Checks that the {@link CuratorFramework} instance is started and that the
	* configured root namespace exists.
	* 
	* @return {@link Result#unhealthy(String)} if the {@link CuratorFramework}
	*         is not started or the configured root namespace does not exist;
	*         otherwise, {@link Result#healthy()}.
	* @throws Exception
	*             if an error occurs checking the health of the ZooKeeper ensemble.
	*/
	@Override
	protected Result check() throws Exception {
		if (framework.getState() != CuratorFrameworkState.STARTED) {
			return Result.unhealthy("Client not started");
		} else if (framework.checkExists().forPath("/") == null) {
			return Result.unhealthy("Root for namespace does not exist");
		}
		return Result.healthy();
	}
}