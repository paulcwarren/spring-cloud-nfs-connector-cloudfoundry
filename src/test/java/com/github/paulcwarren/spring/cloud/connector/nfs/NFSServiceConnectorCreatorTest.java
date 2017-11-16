package com.github.paulcwarren.spring.cloud.connector.nfs;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;
import org.junit.runner.RunWith;
import org.springframework.cloud.connector.nfs.Mode;
import org.springframework.cloud.connector.nfs.NFSServiceConnector;
import org.springframework.cloud.connector.nfs.NFSServiceInfo;
import org.springframework.cloud.connector.nfs.VolumeMount;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsArrayContaining.*;
import static org.junit.Assert.*;

@RunWith(Ginkgo4jRunner.class)
public class NFSServiceConnectorCreatorTest {

    private NFSServiceConnectorCreator nfsServiceConnectorCreator = new NFSServiceConnectorCreator();

    private VolumeMount vm1;
    private VolumeMount vm2;

    {
        Describe("NFSServiceConnectorCreator", () -> {
            Context("given two volumes", () -> {
                BeforeEach(() -> {
                    vm1 = new VolumeMount("/user/specified/dir", Mode.ReadWrite);
                    vm2 = new VolumeMount("/user/specified/other/dir", Mode.ReadOnly);
                });
                It("should return those two volumes", () -> {
                    NFSServiceInfo serviceInfo = new NFSServiceInfo("nfs-1", new VolumeMount[] {vm1, vm2});
                    NFSServiceConnector connector = nfsServiceConnectorCreator.create(serviceInfo, null);
                    assertThat(connector.getVolumeMounts(), is(not(nullValue())));
                    assertThat(connector.getVolumeMounts().length, is(2));
                    assertThat(connector.getVolumeMounts(), hasItemInArray(vm1));
                    assertThat(connector.getVolumeMounts(), hasItemInArray(vm2));
                });
            });
        });
    }
}