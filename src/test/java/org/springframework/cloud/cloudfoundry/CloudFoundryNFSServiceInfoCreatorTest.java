package org.springframework.cloud.cloudfoundry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;
import org.springframework.cloud.connector.nfs.Mode;
import org.springframework.cloud.connector.nfs.NFSServiceInfo;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(Ginkgo4jRunner.class)
public class CloudFoundryNFSServiceInfoCreatorTest {

    private CloudFoundryConnector testCloudConnector = new CloudFoundryConnector();
    @Mock private EnvironmentAccessor mockEnvironment;

    private CloudFoundryNFSServiceInfoCreatorTest instance = this;

    private String containerDir;
    private String rw;

    {
        Describe("NFSServiceInfoCreator", () -> {
            BeforeEach(() -> {
                MockitoAnnotations.initMocks(this);
                testCloudConnector.setCloudEnvironment(mockEnvironment);
            });
            Context("given a VCAP_SERVICES with an nfs service", () -> {
                BeforeEach(() -> {
                    containerDir = "/var/vcap/data/2d36aecc-6479-447b-b851-426262fbf0af";
                    rw = "rw";

                    when(instance.mockEnvironment.getEnvValue("VCAP_SERVICES"))
                            .thenReturn(getServicesPayload(getNFSServicePayload("myNfsVolume", containerDir, rw)));
                });
                It("should return a correctly formed NFSServiceInfo", () -> {
                    List<ServiceInfo> serviceInfos = instance.testCloudConnector.getServiceInfos();
                    NFSServiceInfo info1 = (NFSServiceInfo) getServiceInfo(serviceInfos, "myNfsVolume");
                    assertServiceFoundOfType(info1, NFSServiceInfo.class);
                    assertThat(info1.getVolumeMounts().length, is(1));
                    assertThat(info1.getVolumeMounts()[0].getContainerDir(), is(containerDir));
                    assertThat(info1.getVolumeMounts()[0].getMode(), is(Mode.ReadWrite));
                });
            });
        });
    }

    protected static ServiceInfo getServiceInfo(List<ServiceInfo> serviceInfos, String serviceId) {
        for (ServiceInfo serviceInfo : serviceInfos) {
            if (serviceInfo.getId().equals(serviceId)) {
                return serviceInfo;
            }
        }
        return null;
    }

    protected static String readTestDataFile(String fileName) {
        Scanner scanner = null;
        try {
            Reader fileReader = new InputStreamReader(fileName.getClass().getResourceAsStream(fileName));
            scanner = new Scanner(fileReader);
            return scanner.useDelimiter("\\Z").next();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    protected static String getServicesPayload(String... servicePayloads) {
        Map<String, List<String>> labelPayloadMap = new HashMap<String, List<String>>();

        for (String payload: servicePayloads) {
            String label = getServiceLabel(payload);

            List<String> payloadsForLabel = labelPayloadMap.get(label);
            if (payloadsForLabel == null) {
                payloadsForLabel = new ArrayList<String>();
                labelPayloadMap.put(label, payloadsForLabel);
            }
            payloadsForLabel.add(payload);
        }

        StringBuilder result = new StringBuilder("{\n");
        int labelSize = labelPayloadMap.size();
        int i = 0;

        for (Map.Entry<String, List<String>> entry : labelPayloadMap.entrySet()) {
            result.append(quote(entry.getKey())).append(":");
            result.append(getServicePayload(entry.getValue()));
            if (i++ != labelSize-1) {
                result.append(",\n");
            }
        }
        result.append("}");

        return result.toString();

    }

    protected static String getServicePayload(List<String> servicePayloads) {
        StringBuilder payload = new StringBuilder("[");

        // In Scala, this would have been servicePayloads mkString "," :-)
        for (int i = 0; i < servicePayloads.size(); i++) {
            payload.append(servicePayloads.get(i));
            if (i != servicePayloads.size() - 1) {
                payload.append(",");
            }
        }
        payload.append("]");

        return payload.toString();
    }

    @SuppressWarnings("unchecked")
    protected static String getServiceLabel(String servicePayload) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> serviceMap = objectMapper.readValue(servicePayload, Map.class);
            return serviceMap.get("label").toString();
        } catch (Exception e) {
            return null;
        }
    }

    protected static String quote(String str) {
        return "\"" + str + "\"";
    }

    protected static void assertServiceFoundOfType(ServiceInfo serviceInfo, Class<? extends ServiceInfo> type) {
        assertNotNull(serviceInfo);
        Assert.assertThat(serviceInfo, instanceOf(type));
    }


    protected static String getNFSServicePayload(String serviceName, String containerDir, String mode) {
        return getRelationalPayload("/test-nfs-service-info.json", serviceName, containerDir, mode);
    }

    protected static String getRelationalPayload(String templateFile, String serviceName, String containerDir, String mode) {
        String payload = readTestDataFile(templateFile);
        payload = payload.replace("$serviceName", serviceName);
        payload = payload.replace("$containerDir", containerDir);
        payload = payload.replace("$mode", mode);
        return payload;
    }

}