package com.github.paulcwarren.spring.cloud.connector.nfs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.connector.nfs.NFSServiceConnector;
import org.springframework.cloud.connector.nfs.NFSServiceInfo;
import org.springframework.cloud.connector.nfs.VolumeMount;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Provides an implementation of {@link AbstractServiceConnectorCreator} that detects NFS service information
 * and creates an {@link NFSServiceConnector} instance with an array of {@link VolumeMount}s.
 */
public class NFSServiceConnectorCreator
        extends AbstractServiceConnectorCreator<NFSServiceConnector, NFSServiceInfo> {
    private static Log log = LogFactory.getLog(NFSServiceConnectorCreator.class);

    /**
     * Creates an {@link NFSServiceConnector} instance  with an array of {@link VolumeMount}s from
     * {@link NFSServiceInfo}.
     * @param serviceInfo NFSServiceInfo provided by {@link org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator}
     *                    implementation included within the application.
     * @param serviceConnectorConfig Configuration information to be applied to the connection
     * @return service connector
     */
    @Override
    public NFSServiceConnector create(NFSServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
        log.debug(String.format("Creating NFS Service connector for nfs service %s", serviceInfo.getId()));
        return new NFSServiceConnector(serviceInfo.getVolumeMounts());
    }
}