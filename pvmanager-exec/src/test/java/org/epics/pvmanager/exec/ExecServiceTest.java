/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.epics.pvmanager.service.Service;
import org.epics.pvmanager.service.ServiceUtil;
import org.epics.vtype.VString;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ExecServiceTest {

    @Test
    public void runCommand1() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Service service = new ExecService(new ExecServiceDescription("execSample", "A simple exec service")
                .executorService(executor)
                .addServiceMethod(new ExecServiceMethodDescription("echo", "A simple command")
                                 .command("echo This is a test!")));
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = ServiceUtil.syncExecuteMethod(service.getServiceMethods().get("echo"), params);
        VString output = (VString) result.get("output");
        assertThat(output.getValue(), equalTo("This is a test!\n"));
    }

}
